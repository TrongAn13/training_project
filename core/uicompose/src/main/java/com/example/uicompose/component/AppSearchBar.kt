package com.example.uicompose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.example.uicompose.R
import com.example.uicompose.theme.AppDimens
import com.example.uicompose.theme.search_background
import com.example.uicompose.theme.text_secondary_gray
import com.example.uicompose.theme.white

@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppDimens.Dp48)
            .background(
                color = search_background,
                shape = RoundedCornerShape(AppDimens.Dp24)
            )
            .clickable(enabled = readOnly) {
                onClick?.invoke()
            }
    ) {

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            readOnly = readOnly,
            enabled = !readOnly,
            textStyle = TextStyle(
                color = white
            ),
            cursorBrush = SolidColor(white),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.Dp48)
                .padding(
                    start = AppDimens.Dp16,
                    end = AppDimens.Dp48
                ),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {

                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_hint),
                            color = text_secondary_gray,
                            fontSize = AppDimens.TextSizeMedium
                        )
                    }
                    innerTextField()
                }
            }
        )

        Icon(
            painter = painterResource(R.drawable.ic_search2),
            contentDescription = null,
            tint = text_secondary_gray,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = AppDimens.PaddingMedium)
                .size(AppDimens.IconSizeSmall)
        )
    }
}