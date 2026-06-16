package com.example.training_project.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.example.domain.model.Cast
import com.example.domain.model.Movie
import com.example.domain.model.Review
import com.example.uicompose.R
import com.example.uicompose.component.AppLoading
import com.example.uicompose.theme.AppDimens
import com.example.uicompose.theme.background_dark
import com.example.uicompose.theme.rating_orange
import com.example.uicompose.theme.rating_reviews
import com.example.uicompose.theme.search_background
import com.example.uicompose.theme.text_secondary_gray
import com.example.uicompose.theme.white
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.fillMaxSize().background(background_dark)) {
        when {
            uiState.isLoading -> {
                AppLoading()
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.error, color = white)
                }
            }
            uiState.movie != null -> {
                DetailContent(
                    movie = uiState.movie,
                    isFavorite = uiState.isFavorite,
                    onBackClick = onBackClick,
                    onBookmarkClick = onBookmarkClick
                )
            }
        }
    }
}

@Composable
fun DetailContent(
    movie: Movie,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background_dark)
            .padding(bottom = AppDimens.PaddingXLarge)
    ) {
        DetailTopBar(
            isFavorite = isFavorite,
            onBackClick = onBackClick,
            onBookmarkClick = onBookmarkClick
        )
        DetailHeader(movie = movie)
        DetailMovieInfo(movie = movie)
        DetailTabs(movie = movie)
    }
}
@Composable
private fun DetailTopBar(
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppDimens.DetailTopBarHeight)
            .padding(horizontal = AppDimens.MarginLarge)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(AppDimens.IconSizeSmall)
            )
        }
        Text(
            text = stringResource(R.string.detail_title),
            color = white,
            fontSize = AppDimens.TextSizeLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = onBookmarkClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(
                    id = if (isFavorite) R.drawable.ic_save2 else R.drawable.ic_save
                ),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(AppDimens.IconSizeSmall)
            )
        }
    }
}
@Composable
private fun DetailHeader(movie: Movie) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = AppDimens.MarginLarge)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.BannerHeight)
        ) {
            AsyncImage(
                model = movie.backdropUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            RatingBadge(
                rating = movie.rating,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = AppDimens.MarginMedium, bottom = AppDimens.MarginMedium)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppDimens.BannerHeight - (AppDimens.PosterHeight / 2))
                .padding(horizontal = AppDimens.MarginLarge),
            verticalAlignment = Alignment.Top
        ) {
            Card(
                modifier = Modifier.size(
                    width = AppDimens.PosterWidth,
                    height = AppDimens.PosterHeight
                ),
                shape = RoundedCornerShape(AppDimens.CornerRadiusDefault)
            ) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(AppDimens.MarginMedium))

            Text(
                text = movie.title,
                color = white,
                fontSize = AppDimens.TextSizeSLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = (AppDimens.PosterHeight / 2) + AppDimens.MarginSmall)
            )
        }
    }
}

@Composable
private fun RatingBadge(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = background_dark,
                shape = RoundedCornerShape(AppDimens.CornerRadiusDefault)
            )
            .padding(
                horizontal = AppDimens.PaddingSmall,
                vertical = AppDimens.Dp4
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(AppDimens.IconSizeSmall)
        )

        Spacer(modifier = Modifier.width(AppDimens.PaddingSSmall))

        Text(
            text = String.format("%.1f",rating),
            color = rating_orange,
            fontSize = AppDimens.TextSizeSmall
        )
    }
}
@Composable
private fun DetailMovieInfo(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimens.MarginLarge),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfoItem(
            icon = R.drawable.calendarblank,
            text = movie.releaseDate.take(4)
        )
        Box(
            modifier = Modifier
                .padding(horizontal = AppDimens.Dp12)
                .width(AppDimens.Dp1)
                .height(AppDimens.Dp14)
                .background(text_secondary_gray)
        )
        InfoItem(
            icon = R.drawable.clock,
            text = "${movie.runtime} Minutes"
        )
        Box(
            modifier = Modifier
                .padding(horizontal = AppDimens.Dp12)
                .width(AppDimens.Dp1)
                .height(AppDimens.Dp14)
                .background(text_secondary_gray)
        )
        InfoItem(
            icon = R.drawable.ticket,
            text = movie.genres
        )
    }
}
@Composable
private fun InfoItem(
    icon: Int,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(AppDimens.IconSizeSmall)
        )
        Spacer(modifier = Modifier.width(AppDimens.Dp4))
        Text(
            text = text,
            color = text_secondary_gray,
            fontSize = AppDimens.TextSizeSmall
        )
    }
}
@Composable
private fun DetailTabs(movie: Movie) {
    val tabs = listOf(stringResource(R.string.about_movie), stringResource(R.string.reviews), stringResource(R.string.cast))
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = AppDimens.MarginXLarge)
    ) {
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            contentColor = White,
            divider = {},
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                    height = AppDimens.Dp4,
                    color = search_background
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            color = if (pagerState.currentPage == index) {
                                White
                            } else {
                                text_secondary_gray
                            }
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(AppDimens.MarginSmall))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> AboutContent(movie)
                1 -> ReviewsContent(movie.reviews)
                2 -> CastContent(movie.cast)
            }
        }
    }
}
@Composable
fun EmptyTabContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppDimens.PaddingLarge),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = text_secondary_gray
        )
    }
}

@Composable
fun AboutContent(movie: Movie) {
    Text(
        text = movie.overview,
        color = white,
        fontSize = AppDimens.Sp13,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppDimens.MarginLarge)
    )
}
@Composable
fun ReviewsContent(
    reviews: List<Review>
) {
    if (reviews.isEmpty()) {
        EmptyTabContent(stringResource(R.string.no_review_info))
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(AppDimens.PaddingMedium)
    ) {
        items(reviews) { review ->
            ReviewItem(review)
        }
    }
}
@Composable
fun ReviewItem(
    review: Review
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppDimens.MarginLarge)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.size(AppDimens.Dp44),
                shape = CircleShape
            ) {
                if (review.avatarUrl.isNotBlank()) {
                    AsyncImage(
                        model = review.avatarUrl,
                        contentDescription = review.author,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.rv),
                        placeholder = painterResource(id = R.drawable.rv)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.rv),
                        contentDescription = review.author,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(AppDimens.Dp4)
            )

            Text(
                text = review.rating.toString(),
                color = rating_reviews,
                fontSize = AppDimens.TextSizeSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(
            modifier = Modifier.width(AppDimens.MarginMedium)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = review.author,
                color = white,
                fontSize = AppDimens.TextSizeMedium
            )
            Spacer(
                modifier = Modifier.height(AppDimens.Dp4)
            )
            Text(
                text = review.content,
                color = white,
                fontSize = AppDimens.TextSizeSmall
            )
        }
    }
}
@Composable
fun CastContent(cast: List<Cast>) {
    if (cast.isEmpty()) {
        EmptyTabContent(stringResource(R.string.no_cast_info))
        return
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(AppDimens.PaddingSmall)
    ) {
        items(cast) { actor ->
            CastGridItem(actor)
        }
    }
}

@Composable
private fun CastGridItem(actor: Cast) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppDimens.PaddingSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.Dp100)
                .clip(CircleShape)
                .background(search_background),
            contentAlignment = Alignment.Center
        ) {
            if (actor.profileUrl.isNotBlank()) {
                AsyncImage(
                    model = actor.profileUrl,
                    contentDescription = actor.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.rv),
                    placeholder = painterResource(id = R.drawable.rv)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.rv),
                    contentDescription = actor.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(AppDimens.Dp12))

        Text(
            text = actor.name,
            color = White,
            fontSize = AppDimens.TextSizeMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
