package com.example.training_project.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.domain.model.Movie
import com.example.uicompose.theme.AppDimens
import com.example.uicompose.theme.white
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.domain.model.Review
import com.example.ui.R
import com.example.uicompose.theme.rating_reviews
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import com.example.domain.model.Cast
import com.example.uicompose.theme.search_background

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