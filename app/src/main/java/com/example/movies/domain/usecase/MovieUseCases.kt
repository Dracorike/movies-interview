package com.example.movies.domain.usecase

import com.example.movies.domain.model.Movie
import com.example.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetPopularMoviesUseCase(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(): List<Movie> = repository.getPopular()
}

class SearchMoviesUseCase(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(query: String): List<Movie> = repository.search(query)
}

class ObserveFavoriteIdsUseCase(
    private val repository: MovieRepository,
) {
    operator fun invoke(): Flow<Set<Int>> = repository.observeFavoriteIds()
}

class ToggleFavoriteUseCase(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Int) = repository.toggleFavorite(movieId)
}
