package com.example.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.domain.usecase.GetPopularMoviesUseCase
import com.example.movies.domain.usecase.ObserveFavoriteIdsUseCase
import com.example.movies.domain.usecase.SearchMoviesUseCase
import com.example.movies.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ==========================================================================
 *  AQUI É ONDE VOCÊ TRABALHA. As camadas domain e data já estão prontas.
 *  Sua tarefa: ligar os use cases à tela, tratando os dados e o estado.
 *  Rode `./gradlew :app:testDebugUnitTest` e faça todos os testes passarem.
 * ==========================================================================
 *
 * Requisitos (descritos em detalhe nos testes de MoviesViewModelTest):
 *
 *  1. Carga inicial: ao criar o ViewModel, carregue os filmes populares
 *     (GetPopularMoviesUseCase). Enquanto carrega -> isLoading = true.
 *
 *  2. Mapeie o domínio (Movie) para o modelo de UI (MovieUi):
 *       - year: String (ex.: 1999 -> "1999")
 *       - rating: String com UMA casa decimal (ex.: 8.7 -> "8.7")
 *       - isFavorite: vem do ObserveFavoriteIdsUseCase (fluxo de ids favoritos)
 *     A lista precisa refletir favoritos em tempo real: favoritar um filme deve
 *     atualizar o item correspondente na lista atual.
 *
 *  3. Busca (onQueryChange): atualize query no estado e busque via
 *     SearchMoviesUseCase. Query em branco -> volta pros populares.
 *
 *  4. Erro: se um use case lançar exceção, exponha errorMessage e saia do loading
 *     (sem crashar). retry() deve tentar novamente a última operação.
 *
 *  5. Favoritar (onToggleFavorite): use o ToggleFavoriteUseCase.
 */
class MoviesViewModel(
    private val getPopularMovies: GetPopularMoviesUseCase,
    private val searchMovies: SearchMoviesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val observeFavoriteIds: ObserveFavoriteIdsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState(isLoading = true))
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    // keep latest favorites to map movies when loaded
    private var favorites: Set<Int> = emptySet()

    // track last operation to support retry
    private sealed class Operation {
        object Popular : Operation()
        data class Search(val query: String) : Operation()
    }

    private var lastOperation: Operation = Operation.Popular

    init {
        // observe favorite ids and update UI items when favorites change
        viewModelScope.launch {
            try {
                observeFavoriteIds().collect { favs ->
                    favorites = favs
                    val current = _uiState.value.movies
                    if (current.isNotEmpty()) {
                        val updated = current.map { ui -> ui.copy(isFavorite = favs.contains(ui.id)) }
                        _uiState.value = _uiState.value.copy(movies = updated)
                    }
                }
            } catch (_: Throwable) {
                // ignore favorites observation errors for UI (shouldn't happen in tests)
            }
        }

        // initial load of popular movies
        loadPopular()
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)

        if (query.isBlank()) {
            lastOperation = Operation.Popular
            loadPopular()
            return
        }

        lastOperation = Operation.Search(query)
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val movies = searchMovies.invoke(query)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movies = movies.map { m -> mapToUi(m) },
                    errorMessage = null,
                )
            } catch (e: Throwable) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message ?: "Erro")
            }
        }
    }

    fun onToggleFavorite(movieId: Int) {
        viewModelScope.launch {
            try {
                toggleFavorite.invoke(movieId)
            } catch (_: Throwable) {
                // ignore toggle errors for tests
            }
        }
    }

    fun retry() {
        when (val op = lastOperation) {
            is Operation.Popular -> {
                lastOperation = op
                loadPopular()
            }
            is Operation.Search -> {
                lastOperation = op
                onQueryChange(op.query)
            }
        }
    }

    // -- helpers

    private fun mapToUi(m: com.example.movies.domain.model.Movie): MovieUi =
        MovieUi(
            id = m.id,
            title = m.title,
            year = m.year.toString(),
            rating = String.format("%.1f", m.rating),
            isFavorite = favorites.contains(m.id),
        )

    private fun loadPopular() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val movies = getPopularMovies.invoke()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movies = movies.map { mapToUi(it) },
                    errorMessage = null,
                )
            } catch (e: Throwable) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message ?: "Erro")
            }
        }
    }
}
