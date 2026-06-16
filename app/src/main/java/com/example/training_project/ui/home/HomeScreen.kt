package com.example.training_project.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.example.domain.model.Movie
import com.example.domain.model.MovieTab
import com.example.uicompose.R
import com.example.uicompose.component.AppSearchBar
import com.example.uicompose.theme.AppDimens
import com.example.uicompose.theme.background_dark
import com.example.uicompose.theme.primary_blue
import com.example.uicompose.theme.search_background
import com.example.uicompose.theme.text_secondary_gray
import com.example.uicompose.theme.white
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    currentTab: MovieTab,
    pagingItems: LazyPagingItems<Movie>,
    onTabSelected: (MovieTab) -> Unit,
    onMovieClick: (Long) -> Unit,
    onSearchClick: () -> Unit,
    onRefresh: () -> Unit
) {
    val density = LocalDensity.current

    var maxScrollPx by remember { mutableStateOf(0f) }
    var maxScrollDp by remember { mutableStateOf(AppDimens.None) }
    var tabsHeightDp by remember { mutableStateOf(AppDimens.None) }
    var headerOffsetPx by remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember(maxScrollPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (maxScrollPx == 0f) return Offset.Zero
                val delta = available.y
                val newOffset = headerOffsetPx + delta
                headerOffsetPx = newOffset.coerceIn(-maxScrollPx, 0f)
                return Offset.Zero
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background_dark)
                .nestedScroll(nestedScrollConnection)
        ) {
            val progress = if (maxScrollPx > 0f) 1f + (headerOffsetPx / maxScrollPx) else 1f

            val dynamicPaddingTopDp = remember(headerOffsetPx, maxScrollDp, tabsHeightDp) {
                val currentOffsetDp = with(density) { headerOffsetPx.toDp() }
                maxScrollDp + tabsHeightDp + currentOffsetDp
            }

            HomeMovieGrid(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = dynamicPaddingTopDp,
                    start = AppDimens.MarginMedium,
                    end = AppDimens.MarginMedium,
                    bottom = AppDimens.MarginMedium
                ),
                pagingItems = pagingItems,
                onMovieClick = onMovieClick
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, headerOffsetPx.roundToInt()) }
                    .background(background_dark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            if (coordinates.size.height > 0) {
                                maxScrollPx = coordinates.size.height.toFloat()
                                maxScrollDp = with(density) { coordinates.size.height.toDp() }
                            }
                        }
                        .graphicsLayer {
                            alpha = if (progress > 0.5f) (progress - 0.5f) * 2f else 0f
                        }
                ) {
                    HomeHeader(onSearchClick = onSearchClick)
                    Spacer(modifier = Modifier.height(AppDimens.MarginLarge))
                    TrendingSection(
                        movies = uiState.trendingMovies,
                        onMovieClick = onMovieClick
                    )
                    Spacer(modifier = Modifier.height(AppDimens.MarginLarge))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            if (coordinates.size.height > 0) {
                                tabsHeightDp = with(density) { coordinates.size.height.toDp() }
                            }
                        }
                        .background(background_dark)
                ) {
                    HomeTabs(
                        currentTab = currentTab,
                        onTabSelected = onTabSelected
                    )
                }
            }
        }
    }
}

@Composable
fun HomeMovieGrid(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    pagingItems: LazyPagingItems<Movie>,
    onMovieClick: (Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        items(pagingItems.itemCount) { index ->
            pagingItems[index]?.let { movie ->
                HomeMovieItem(
                    movie = movie,
                    onClick = {
                        onMovieClick(movie.id ?: -1)
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppDimens.MarginLarge
            )
    ) {
        Spacer(Modifier.height(AppDimens.Dp30))
        Text(
            text = stringResource(R.string.home_welcome),
            color = white,
            fontSize = AppDimens.TextSizeXLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(AppDimens.Dp20))
        AppSearchBar(
            query = "",
            onQueryChange = {},
            readOnly = true,
            onClick = onSearchClick
        )
    }
}

@Composable
fun TrendingSection(
    movies: List<Movie>,
    onMovieClick: (Long) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(
            horizontal = AppDimens.Dp28
        ),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.Dp30)
    ) {
        itemsIndexed(movies) { index, movie ->
            TrendingMovieItem(
                movie = movie,
                rank = index + 1,
                onClick = {
                    onMovieClick(movie.id ?: -1)
                }
            )
        }
    }
}

@Composable
fun TrendingMovieItem(
    movie: Movie,
    rank: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(AppDimens.Dp160)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.Dp240)
                .clip(
                    RoundedCornerShape(
                        AppDimens.CornerRadiusDefault
                    )
                ),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-AppDimens.Dp10), y = AppDimens.Dp40)
        ) {
            Text(
                text = rank.toString(),
                fontSize = AppDimens.Sp90,
                color = primary_blue,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    drawStyle = Stroke(
                        miter = 10f,
                        width = 6f,
                        join = StrokeJoin.Round
                    )
                )
            )
            Text(
                text = rank.toString(),
                fontSize = AppDimens.Sp90,
                color = background_dark,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun HomeTabs(
    currentTab: MovieTab,
    onTabSelected: (MovieTab) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(
            top = AppDimens.MarginMedium,
            start = AppDimens.MarginMedium,
            end = AppDimens.MarginMedium,
            bottom = AppDimens.Dp4
        ),
    ) {
        items(MovieTab.entries) { tab ->
            Column(
                modifier = Modifier
                    .clickable { onTabSelected(tab) }
                    .padding(
                        horizontal = AppDimens.PaddingSmall,
                        vertical = AppDimens.PaddingSmall
                    )
                    .width(IntrinsicSize.Max)
            ) {
                Text(
                    text = tab.title,
                    color =
                        if (tab == currentTab)
                            white
                        else
                            text_secondary_gray
                )
                Spacer(modifier = Modifier.height(AppDimens.MarginSmall))
                if (tab == currentTab) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppDimens.Dp4)
                            .background(search_background)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeMovieItem(
    movie: Movie,
    onClick: () -> Unit
) {
    AsyncImage(
        model = movie.posterUrl,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .padding(horizontal = AppDimens.Dp6, vertical = AppDimens.Dp10)
            .clickable(onClick = onClick)
            .clip(
                RoundedCornerShape(
                    AppDimens.CornerRadiusDefault
                )
            ),
        contentScale = ContentScale.Crop
    )
}
