package com.example.training_project.ui.favorite

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.example.training_project.ui.detail.DetailActivity
import com.example.uicompose.base.BaseComposeFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoriteFragment : BaseComposeFragment<FavoriteViewModel>() {
    override val viewModel: FavoriteViewModel by activityViewModel()

    @Composable
    override fun ScreenContent() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.getFavoriteMovies()
        }

        FavoriteScreen(
            uiState = uiState,
            onBackClick = {
                findNavController().popBackStack()
            },
            onMovieClick = { movie ->
                startActivity(
                    Intent(requireContext(), DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.id)
                    }
                )
            },
        )
    }
}
