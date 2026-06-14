package com.example.training_project.ui.auth

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.training_project.MainActivity
import com.example.uicompose.base.BaseComposeActivity
import com.example.uicompose.screen.login.LoginScreen
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : BaseComposeActivity<LoginViewModel>() {
    override val viewModel: LoginViewModel by viewModel()
    private val pref: PreferenceManager by inject()

    @Composable
    override fun ScreenContent() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LoginScreen(
            email = uiState.email,
            password = uiState.password,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = viewModel::login
        )

        LaunchedEffect(uiState.loginSuccess) {
            if (uiState.loginSuccess) {
                uiState.sessionId?.let {
                    pref.saveSessionId(it)
                }
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
