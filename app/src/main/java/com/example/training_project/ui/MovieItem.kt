package com.example.training_project.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.uicompose.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.domain.model.Movie
import com.example.uicompose.theme.AppDimens
import com.example.uicompose.theme.rating_orange
import com.example.uicompose.theme.text_secondary_gray
import com.example.uicompose.theme.white

@Composable
fun MovieItem(
    movie: Movie,
    onClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(movie) }
            .padding(bottom = AppDimens.MarginLarge)
    ) {

        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            modifier = Modifier
                .width(AppDimens.Dp100)
                .height(AppDimens.Dp140)
                .clip(RoundedCornerShape(AppDimens.CornerRadiusDefault)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(AppDimens.MarginMedium))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = movie.title,
                color = white,
                fontSize = AppDimens.TextSizeSLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(AppDimens.MarginSSmall))

            InfoRow(
                icon = R.drawable.ic_star,
                text = String.format("%.1f", movie.rating),
                textColor = rating_orange
            )

            Spacer(modifier = Modifier.height(AppDimens.Dp2))

            InfoRow(
                icon = R.drawable.ticket,
                text = movie.genres,
                textColor = text_secondary_gray
            )

            Spacer(modifier = Modifier.height(AppDimens.Dp2))

            InfoRow(
                icon = R.drawable.calendarblank,
                text = movie.releaseDate.take(4),
                textColor = text_secondary_gray
            )

            Spacer(modifier = Modifier.height(AppDimens.Dp2))

            InfoRow(
                icon = R.drawable.clock,
                text = stringResource(R.string.detail_duration),
                textColor = text_secondary_gray
            )
        }
    }
}
@Composable
private fun InfoRow(
    icon: Int,
    text: String,
    textColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(AppDimens.Dp16)
        )

        Spacer(modifier = Modifier.width(AppDimens.PaddingSmall))

        Text(
            text = text,
            color = textColor,
            fontSize = AppDimens.TextSizeMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
