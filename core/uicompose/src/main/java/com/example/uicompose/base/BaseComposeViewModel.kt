package com.example.uicompose.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.R
import com.example.ui.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

data class BaseUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

open class BaseComposeViewModel(private val resourceProvider: ResourceProvider) : ViewModel() {
    private val _baseUiState = MutableStateFlow(BaseUiState())
    val baseUiState: StateFlow<BaseUiState> = _baseUiState.asStateFlow()

    protected fun showLoading() {
        _baseUiState.value = _baseUiState.value.copy(
            isLoading = true,
            error = null
        )
    }

    protected fun hideLoading() {
        _baseUiState.value = _baseUiState.value.copy(
            isLoading = false
        )
    }

    protected fun showError(message: String) {
        _baseUiState.value = _baseUiState.value.copy(
            isLoading = false,
            error = message
        )
    }

    fun clearError() {
        _baseUiState.value = _baseUiState.value.copy(
            error = null
        )
    }

    protected fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is UnknownHostException -> resourceProvider.getString(R.string.error_no_network)
            is SocketTimeoutException -> resourceProvider.getString(R.string.error_timeout)
            is IOException -> resourceProvider.getString(R.string.error_network_connection)
            is HttpException -> {
                when (e.code()) {
                    401 -> resourceProvider.getString(R.string.error_401)
                    404 -> resourceProvider.getString(R.string.error_404)
                    500 -> resourceProvider.getString(R.string.error_500)
                    else -> resourceProvider.getString(R.string.error_http_unknow) + e.code()
                }
            }
            else -> e.message ?: resourceProvider.getString(R.string.error_unknown)
        }
    }

    protected fun <T> executeApi(
        showGlobalLoading: Boolean = true,
        onSuccess: (T) -> Unit,
        onError: ((String) -> Unit)? = null,
        onFinally: (() -> Unit)? = null,
        apiCall: suspend () -> T
    ) {
        viewModelScope.launch {
            try {
                if (showGlobalLoading) showLoading()
                val result = withContext(Dispatchers.IO) {
                    apiCall()
                }
                onSuccess(result)
            } catch (e: Throwable) {
                if (e is CancellationException) throw e

                val message = getErrorMessage(e)

                if (onError != null) {
                    onError(message)
                } else {
                    showError(message)
                }
            } finally {
                if (showGlobalLoading) hideLoading()
                onFinally?.invoke()
            }
        }
    }
    protected fun <T> executeApiState(
        onLoading: (Boolean) -> Unit,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit,
        apiCall: suspend () -> T
    ) {
        viewModelScope.launch {
            try {
                onLoading(true)

                val result = withContext(Dispatchers.IO) {
                    apiCall()
                }

                onSuccess(result)
            } catch (e: Throwable) {
                if (e is CancellationException) throw e
                onError(getErrorMessage(e))
            } finally {
                onLoading(false)
            }
        }
    }
}