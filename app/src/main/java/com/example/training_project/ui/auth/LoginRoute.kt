package com.example.training_project.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uicompose.screen.login.LoginScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    pref: PreferenceManager = koinInject()
    ) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreen(
        uiState = uiState.value,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::login,
    )

    LaunchedEffect(uiState.value.loginSuccess){
        if (uiState.value.loginSuccess) {
            uiState.value.sessionId?.let {
                pref.saveSessionId(it)
            }
            onLoginSuccess()
        }
    }
}