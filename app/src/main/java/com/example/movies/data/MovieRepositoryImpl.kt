package com.example.movies.data

import com.example.movies.data.local.FavoritesLocalDataSource
import com.example.movies.data.remote.FakeMovieApi
import com.example.movies.data.remote.MovieDto
import com.example.movies.domain.model.Movie
import com.example.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val api: FakeMovieApi,
    private val favorites: FavoritesLocalDataSource,
) : MovieRepository {

    override suspend fun getPopular(): List<Movie> =
        api.fetchPopular().map { it.toDomain() }

    override suspend fun search(query: String): List<Movie> =
        api.searchMovies(query).map { it.toDomain() }

    override fun observeFavoriteIds(): Flow<Set<Int>> = favorites.observe()

    override suspend fun toggleFavorite(movieId: Int) = favorites.toggle(movieId)
}

private fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title,
    year = releaseDate.take(4).toIntOrNull() ?: 0,
    rating = voteAverage,
)
