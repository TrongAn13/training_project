package com.example.training_project.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.training_project.R
import com.example.training_project.utils.Resource
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
open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val globalError = MutableLiveData<String?>()
    val isLoading = MutableLiveData<Boolean>()

    private fun getErrorMessage(e: Throwable): String {
        val context = getApplication<Application>()
        return when (e) {
            is UnknownHostException -> context.getString(R.string.error_no_network)
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is IOException -> context.getString(R.string.error_network_connection)
            is HttpException -> {
                when (e.code()) {
                    401 -> context.getString(R.string.error_401)
                    404 -> context.getString(R.string.error_404)
                    500 -> context.getString(R.string.error_500)
                    else -> context.getString(R.string.error_http_unknow) + e.code()
                }
            }
            else -> e.message ?: context.getString(R.string.error_unknown)
        }
    }
    protected fun <T> executeApi(liveData: MutableLiveData<Resource<T>>, type : LoadingType = LoadingType.NORMAL, apiCall: suspend () -> T) {
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
            val result = withContext(Dispatchers.IO) {
                apiCall.invoke()
            }
            if (type == LoadingType.NORMAL) isLoading.value = false
            liveData.value = Resource.Success(result)
        }
    }
}