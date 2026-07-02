package com.example.movies.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MoviesScreen(
    state: MoviesUiState,
    onQueryChange: (String) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onRetry: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = state.query,
            onValueChange = onQueryChange,
            label = { Text("Buscar filme") },
            modifier = Modifier.fillMaxWidth(),
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.errorMessage != null -> ErrorState(state.errorMessage, onRetry)
                state.movies.isEmpty() -> Text("Nenhum filme encontrado")
                else -> MovieList(state.movies, onToggleFavorite)
            }
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(message)
        Button(onClick = onRetry, modifier = Modifier.padding(top = 8.dp)) {
            Text("Tentar novamente")
        }
    }
}

@Composable
private fun MovieList(movies: List<MovieUi>, onToggleFavorite: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(movies, key = { it.id }) { movie ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(movie.title, fontWeight = FontWeight.Bold)
                    Text(
                        "${movie.year}  •  ⭐ ${movie.rating}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                IconButton(onClick = { onToggleFavorite(movie.id) }) {
                    Icon(
                        imageVector = if (movie.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (movie.isFavorite) "Desfavoritar" else "Favoritar",
                    )
                }
            }
            HorizontalDivider()
        }
    }
}
