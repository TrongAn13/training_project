package com.example.training_project.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.domain.model.Movie
import com.example.training_project.ui.MovieItem
import com.example.uicompose.R
import com.example.uicompose.component.AppSearchBar
import com.example.uicompose.theme.AppDimens
import com.example.uicompose.theme.background_dark
import com.example.uicompose.theme.primary_blue
import com.example.uicompose.theme.text_secondary_gray
import com.example.uicompose.theme.white

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onBackClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onMovieClick: (Movie) -> Unit,
    onClearHistory: () -> Unit
){
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background_dark)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        SearchHeader(
            onBackClick = onBackClick,
        )
        Spacer(modifier = Modifier.height(AppDimens.Dp20))
        AppSearchBar(
            query = uiState.query,
            onQueryChange = onQueryChange,
            onClick = {
                onBackClick()
            },
            modifier = Modifier.padding(
                horizontal = AppDimens.MarginLarge
            )
        )
        Spacer(
            modifier = Modifier.height(AppDimens.MarginLarge)
        )

        when {

            uiState.isEmpty -> {
                SearchEmptyState()
            }

            uiState.query.isBlank() -> {
                SearchHistoryContent(
                    movies = uiState.searchHistory,
                    onMovieClick = onMovieClick,
                    onClearHistory = onClearHistory
                )
            }

            else -> {
                SearchResultContent(
                    movies = uiState.searchResults,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}
@Composable
private fun SearchHeader(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppDimens.Dp40)
    ){
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
            text = stringResource(R.string.search_tab),
            color = white,
            fontSize = AppDimens.TextSizeXLarge,
            modifier = Modifier.align(Alignment.Center),
            fontWeight = FontWeight.Bold
        )

        Icon(
            painter = painterResource(R.drawable.info_circle),
            contentDescription = null,
            tint = white,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = AppDimens.MarginXXLarge)
        )
    }
}

@Composable
fun SearchResultContent(movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = AppDimens.PaddingLarge,
            vertical = AppDimens.PaddingLarge
        )
    ) {
        items(
            items = movies,
            key = { it.id }
        ) { movie ->
            MovieItem(
                movie = movie,
                onClick = onMovieClick
            )
        }
    }
}
@Composable
private fun SearchHistoryContent(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    onClearHistory: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppDimens.PaddingLarge
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.recent_search),
                color = white,
                fontSize = AppDimens.TextSizeLarge
            )

            Text(
                text = stringResource(R.string.search_clear),
                color = primary_blue,
                modifier = Modifier.clickable {
                    onClearHistory()
                }
            )
        }

        Spacer(
            modifier = Modifier.height(AppDimens.MarginMedium)
        )

        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = AppDimens.PaddingLarge
            )
        ) {
            items(
                items = movies,
                key = { it.id }
            ) { movie ->
                MovieItem(
                    movie = movie,
                    onClick = onMovieClick
                )
            }
        }
    }
}
@Composable
private fun SearchEmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_notfound),
            contentDescription = null
        )
        Spacer(
            modifier = Modifier.height(AppDimens.MarginMedium)
        )
        Text(
            text = stringResource(R.string.notfound_movie1),
            color = white,
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier.height(AppDimens.MarginSmall)
        )
        Text(
            text = stringResource(R.string.notfound_movie2),
            color = text_secondary_gray,
            textAlign = TextAlign.Center
        )
    }
}