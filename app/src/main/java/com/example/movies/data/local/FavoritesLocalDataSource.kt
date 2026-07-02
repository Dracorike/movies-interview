package com.example.movies.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Fonte de dados local (em memória) dos favoritos. Num app real seria Room/DataStore.
 */
class FavoritesLocalDataSource {
    private val favoriteIds = MutableStateFlow<Set<Int>>(emptySet())

    fun observe(): StateFlow<Set<Int>> = favoriteIds.asStateFlow()

    fun toggle(movieId: Int) {
        favoriteIds.update { current ->
            if (movieId in current) current - movieId else current + movieId
        }
    }
}
