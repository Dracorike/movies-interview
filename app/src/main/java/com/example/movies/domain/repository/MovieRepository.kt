package com.example.movies.domain.repository

import com.example.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    /** Lista os filmes populares (chamada "de rede"). Pode lançar exceção. */
    suspend fun getPopular(): List<Movie>

    /** Busca filmes por título (chamada "de rede"). Pode lançar exceção. */
    suspend fun search(query: String): List<Movie>

    /** Fluxo com os ids dos filmes marcados como favoritos. */
    fun observeFavoriteIds(): Flow<Set<Int>>

    /** Alterna o estado de favorito de um filme. */
    suspend fun toggleFavorite(movieId: Int)
}
