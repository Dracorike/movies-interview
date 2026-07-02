package com.example.movies.presentation

import androidx.lifecycle.ViewModel
import com.example.movies.domain.usecase.GetPopularMoviesUseCase
import com.example.movies.domain.usecase.ObserveFavoriteIdsUseCase
import com.example.movies.domain.usecase.SearchMoviesUseCase
import com.example.movies.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    init {
        // TODO: iniciar a carga dos filmes populares e observar os favoritos.
    }

    fun onQueryChange(query: String) {
        // TODO: atualizar a query e buscar.
    }

    fun onToggleFavorite(movieId: Int) {
        // TODO: alternar favorito.
    }

    fun retry() {
        // TODO: repetir a última operação (populares ou busca).
    }
}
