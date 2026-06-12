package com.example.training_project.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import com.example.ui.R
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.domain.model.Movie
import com.example.uicompose.theme.AppDimens
import com.example.uicompose.theme.background_dark
import com.example.uicompose.theme.rating_orange
import com.example.uicompose.theme.search_background
import com.example.uicompose.theme.text_secondary_gray
import com.example.uicompose.theme.white
import kotlinx.coroutines.launch

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
        modifier = Modifier
            .fillMaxWidth()
            .height(AppDimens.DetailHeaderHeight)
    ) {
        AsyncImage(
            model = movie.backdropUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.BannerHeight),
            contentScale = ContentScale.Crop
        )

        Card(
            modifier = Modifier
                .padding(start = AppDimens.MarginLarge)
                .size(
                    width = AppDimens.PosterWidth,
                    height = AppDimens.PosterHeight
                )
                .align(Alignment.TopStart)
                .offset(y = AppDimens.Dp140),
            shape = RoundedCornerShape(AppDimens.CornerRadiusDefault)
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        RatingBadge(
            rating = movie.rating,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = AppDimens.MarginMedium, bottom = AppDimens.Dp70)
        )

        Text(
            text = movie.title,
            color = white,
            fontSize = AppDimens.TextSizeLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    start = AppDimens.MarginLarge + AppDimens.PosterWidth + AppDimens.MarginMedium,
                    end = AppDimens.MarginLarge
                )
                .align(Alignment.BottomStart)
        )
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