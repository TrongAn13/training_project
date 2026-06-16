package com.example.uicompose.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.example.training_project.ui.auth.LoginUiState
import com.example.uicompose.R
import com.example.uicompose.component.AppButton
import com.example.uicompose.component.AppTextField
import com.example.uicompose.theme.*

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background_dark)
            .padding(horizontal = AppDimens.MarginLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(AppDimens.Dp40))

        Text(
            text = stringResource(R.string.login_title),
            color = white,
            fontSize = AppDimens.TextSizeLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(AppDimens.Dp44))

        Text(
            text = stringResource(R.string.welcome_hello),
            color = white,
            fontSize = AppDimens.TextSizeXXLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(AppDimens.Dp20))

        Text(
            text = stringResource(R.string.welcome_description),
            color = text_hint_white,
            fontSize = AppDimens.TextSizeMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppDimens.Dp40))

        AppTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            hint = stringResource(R.string.hint_email),
            Modifier
                .width(AppDimens.ButtonWidthLogin)
                .height(AppDimens.Dp55),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_mail),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        )

        Spacer(modifier = Modifier.height(AppDimens.Dp24))

        AppTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            hint = stringResource(R.string.hint_password),
            Modifier
                .width(AppDimens.ButtonWidthLogin)
                .height(AppDimens.Dp55),
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        )

        Spacer(modifier = Modifier.height(AppDimens.Dp40))

        AppButton(
            text = stringResource(R.string.login_title),
            onClick = onLoginClick,
            modifier = Modifier
                .width(AppDimens.ButtonWidthLogin)
                .height(AppDimens.ButtonHeightLarge),
            colors = ButtonDefaults.buttonColors(
                containerColor = white,
                contentColor = button_login_text
            )
        )

        Spacer(modifier = Modifier.height(AppDimens.Dp55))

        OrDivider()

        Spacer(modifier = Modifier.height(AppDimens.Dp30))

        AppButton(
            text = stringResource(R.string.gmail_login),
            onClick = onGoogleClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = primary_blue,
                contentColor = white
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_gg),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        )

        Spacer(modifier = Modifier.height(AppDimens.Dp30))

        AppButton(
            text = stringResource(R.string.facebook_login),
            onClick = onFacebookClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = primary_blue,
                contentColor = white
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_fb),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        )

        Spacer(modifier = Modifier.height(AppDimens.MarginLarge))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.already_have_account),
                color = text_secondary_gray,
                fontSize = AppDimens.TextSizeMedium
            )

            Text(
                text = stringResource(R.string.sign_in),
                color = primary_blue,
                fontSize = AppDimens.TextSizeMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun OrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = text_secondary_gray
        )
        Text(
            text = stringResource(R.string.or_text),
            color = white,
            modifier = Modifier.padding(horizontal = AppDimens.Dp10)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = text_secondary_gray
        )
    }
}