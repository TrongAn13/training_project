package com.example.training_project.ui.auth

import com.example.domain.usecase.LoginUseCase
import com.example.uicompose.ResourceProvider
import com.example.uicompose.base.BaseComposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loginSuccess: Boolean = false,
    val sessionId: String? = null
)

class LoginViewModel(private val resourceProvider: ResourceProvider, private val loginUseCase: LoginUseCase) : BaseComposeViewModel(resourceProvider){
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }
    fun login() {
        executeApi(
            onSuccess = { sessionId ->
                _uiState.value = _uiState.value.copy(
                    loginSuccess = true,
                    sessionId = sessionId
                )
            }
        ){
            loginUseCase(_uiState.value.email, _uiState.value.password)
        }
    }
}