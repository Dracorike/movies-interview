package com.example.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movies.di.ServiceLocator
import com.example.movies.presentation.MoviesScreen
import com.example.movies.presentation.MoviesViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MoviesViewModel by viewModels { ServiceLocator.moviesViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val state by viewModel.uiState.collectAsStateWithLifecycle()
                    MoviesScreen(
                        state = state,
                        onQueryChange = viewModel::onQueryChange,
                        onToggleFavorite = viewModel::onToggleFavorite,
                        onRetry = viewModel::retry,
                    )
                }
            }
        }
    }
}
