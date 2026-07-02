package com.example.movies.data.remote

import kotlinx.coroutines.delay

/**
 * "API" fake: não usa internet. Tem latência simulada e lança exceção quando a
 * query é "erro" (pra exercitar o tratamento de falha).
 */
class FakeMovieApi(
    private val latencyMs: Long = 400,
) {
    private val movies = listOf(
        MovieDto(1, "The Matrix", "1999-03-31", 8.7),
        MovieDto(2, "Inception", "2010-07-16", 8.8),
        MovieDto(3, "Interstellar", "2014-11-07", 8.6),
        MovieDto(4, "The Dark Knight", "2008-07-18", 9.0),
        MovieDto(5, "Pulp Fiction", "1994-10-14", 8.9),
        MovieDto(6, "Fight Club", "1999-10-15", 8.8),
        MovieDto(7, "Forrest Gump", "1994-07-06", 8.8),
        MovieDto(8, "The Prestige", "2006-10-20", 8.5),
    )

    suspend fun fetchPopular(): List<MovieDto> {
        delay(latencyMs)
        return movies
    }

    suspend fun searchMovies(query: String): List<MovieDto> {
        delay(latencyMs)
        if (query.equals("erro", ignoreCase = true)) {
            throw RuntimeException("Falha de rede simulada")
        }
        return movies.filter { it.title.contains(query, ignoreCase = true) }
    }
}
