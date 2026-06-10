package com.example.uicompose.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.R
import com.example.uicompose.component.AppButton
import com.example.uicompose.component.AppTextField
import com.example.uicompose.theme.AppDimens
import com.example.uicompose.theme.AppTheme

@Preview(showBackground = true, backgroundColor = 0xFF242A32)
@Composable
private fun AppButtonPreview(){
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppDimens.Dp12),
            modifier = Modifier.padding(AppDimens.Dp16)
        ) {
            AppButton("Login", onClick = {})
            AppButton("Disabled", onClick = {}, enabled = false)
            AppButton("Loading", onClick = {}, loading = true)
        }
    }
}
@Preview(showBackground = true, backgroundColor = 0xFF242A32)
@Composable
private fun AppTextFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(AppDimens.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(AppDimens.PaddingMedium)
        ) {
            AppTextField(
                value = "admin",
                onValueChange = {},
                hint = "Email",
                modifier = Modifier
                    .width(AppDimens.InputWidth)
                    .height(AppDimens.Dp55),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_mail),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            )

            AppTextField(
                value = "123",
                onValueChange = {},
                hint = "Password",
                modifier = Modifier
                    .width(AppDimens.InputWidth)
                    .height(AppDimens.Dp55),
                visualTransformation = PasswordVisualTransformation()
            )
        }
    }
}