package com.example.training_project.ui.detail

import android.os.Bundle
import androidx.compose.runtime.Composable
import com.example.uicompose.base.BaseComposeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : BaseComposeActivity<DetailViewModel>() {
    companion object {
        const val EXTRA_MOVIE_ID = "MOVIE_ID"
    }
    private val movieId: Long by lazy {
        intent.getLongExtra(EXTRA_MOVIE_ID, -1L)
    }
    override val viewModel: DetailViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (movieId != -1L){
            viewModel.fetchMovieDetails(movieId)
        }
    }
    @Composable
    override fun ScreenContent() {
        DetailRoute(
            viewModel = viewModel,
            onBackClick = {
                finish()
            }
        )
    }
}
