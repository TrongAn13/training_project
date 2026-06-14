package com.example.training_project.ui.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.domain.model.Movie
import com.example.training_project.ui.MovieItem
import com.example.ui.R
import com.example.uicompose.theme.AppDimens
import com.example.uicompose.theme.background_dark
import com.example.uicompose.theme.text_secondary_gray
import com.example.uicompose.theme.white

@Composable
fun FavoriteScreen(
    uiState: FavoriteUiState,
    onBackClick: () -> Unit,
    onMovieClick: (Movie) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background_dark)
    ) {
        FavoriteHeader(
            onBackClick = onBackClick
        )
        Spacer(
            modifier = Modifier.height(
                AppDimens.MarginLarge
            )
        )
        when {
            uiState.movies.isEmpty() -> {
                EmptyFavoriteContent()
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = AppDimens.PaddingLarge,
                        vertical = AppDimens.PaddingLarge
                    )
                ) {
                    items(uiState.movies) { movie ->
                        MovieItem(
                            movie = movie,
                            onClick = onMovieClick
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun FavoriteHeader(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppDimens.Dp40)
    ) {

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = AppDimens.MarginLarge)
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_left),
                contentDescription = null,
                tint = white
            )
        }

        Text(
            text = stringResource(R.string.watchlist_tab),
            color = white,
            fontSize = AppDimens.TextSizeXLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
@Composable
fun EmptyFavoriteContent() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(R.drawable.ic_notfound_favorite),
            contentDescription = null,
            modifier = Modifier.size(AppDimens.ImageViewFragmentSearch)
        )

        Spacer(
            modifier = Modifier.height(AppDimens.MarginMedium)
        )

        Text(
            text = stringResource(
                R.string.notfound_favorite_movie
            ),
            color = white,
            fontSize = AppDimens.TextSizeLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(AppDimens.MarginSmall)
        )

        Text(
            text = stringResource(
                R.string.notfound_movie2
            ),
            color = text_secondary_gray,
            fontSize = AppDimens.TextSizeMedium
        )
    }
}
