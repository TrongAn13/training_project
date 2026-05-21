package com.example.training_project.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training_project.R
import com.example.training_project.utils.Resource
import com.example.training_project.utils.ResourceProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

enum class LoadingType {
    NONE,
    NORMAL,
    SHIMMER
}
open class BaseViewModel(private val resourceProvider: ResourceProvider) : ViewModel(){
    val globalError = MutableLiveData<String?>()
    val isLoading = MutableLiveData<Boolean>()

    private fun getErrorMessage(e: Throwable): String {
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
    protected fun <T> executeApi(liveData: MutableLiveData<Resource<T>>, type : LoadingType = LoadingType.NORMAL, onFinally: (() -> Unit)? = null, apiCall: suspend () -> T) {
        when (type) {
            LoadingType.NORMAL -> {
                isLoading.value = true
            }
            LoadingType.SHIMMER -> {
                liveData.value = Resource.Loading
            }
            LoadingType.NONE -> Unit
        }

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            if (throwable is CancellationException) throw throwable
            if(type == LoadingType.NORMAL){
                isLoading.value = false
            }
            val message = getErrorMessage(throwable)
            when (type){
                LoadingType.NONE -> liveData.value = Resource.Error(message)
                LoadingType.NORMAL -> globalError.value = message
                LoadingType.SHIMMER -> liveData.value = Resource.Error(message)
            }
        }

        viewModelScope.launch(exceptionHandler) {
            try {
                val result = withContext(Dispatchers.IO) {
                    apiCall.invoke()
                }
                liveData.value = Resource.Success(result)
            }
            finally {
                if (type == LoadingType.NORMAL) isLoading.value = false
                onFinally?.invoke()
            }
        }
    }
}