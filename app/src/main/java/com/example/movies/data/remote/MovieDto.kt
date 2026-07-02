package com.example.movies.data.remote

/**
 * Formato "cru" que viria da API. Repare que os nomes/tipos são diferentes do
 * modelo de domínio (releaseDate como String, voteAverage como Double).
 */
data class MovieDto(
    val id: Int,
    val title: String,
    val releaseDate: String, // ex.: "1999-03-31"
    val voteAverage: Double,  // ex.: 8.7
)
