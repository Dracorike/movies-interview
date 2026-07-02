package com.example.movies.presentation

/**
 * Modelo pronto pra UI (o que a tela consome). Diferente do domínio: [year] e
 * [rating] são Strings já formatadas, e existe o campo [isFavorite].
 */
data class MovieUi(
    val id: Int,
    val title: String,
    val year: String,
    val rating: String,
    val isFavorite: Boolean,
)

data class MoviesUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val movies: List<MovieUi> = emptyList(),
    val errorMessage: String? = null,
)
