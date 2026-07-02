package com.example.movies.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.data.MovieRepositoryImpl
import com.example.movies.data.local.FavoritesLocalDataSource
import com.example.movies.data.remote.FakeMovieApi
import com.example.movies.domain.repository.MovieRepository
import com.example.movies.domain.usecase.GetPopularMoviesUseCase
import com.example.movies.domain.usecase.ObserveFavoriteIdsUseCase
import com.example.movies.domain.usecase.SearchMoviesUseCase
import com.example.movies.domain.usecase.ToggleFavoriteUseCase
import com.example.movies.presentation.MoviesViewModel

/**
 * DI manual e simples pra manter o foco no ViewModel (sem Hilt/kapt).
 */
object ServiceLocator {

    private val repository: MovieRepository by lazy {
        MovieRepositoryImpl(
            api = FakeMovieApi(),
            favorites = FavoritesLocalDataSource(),
        )
    }

    fun moviesViewModelFactory(): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MoviesViewModel(
                    getPopularMovies = GetPopularMoviesUseCase(repository),
                    searchMovies = SearchMoviesUseCase(repository),
                    toggleFavorite = ToggleFavoriteUseCase(repository),
                    observeFavoriteIds = ObserveFavoriteIdsUseCase(repository),
                ) as T
            }
        }
}
