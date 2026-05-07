package com.example.training_project.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training_project.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

open class BaseViewModel : ViewModel() {
    protected fun <T> executeApi(liveData: MutableLiveData<Resource<T>>, apiCall: suspend () -> T) {
        liveData.value = Resource.Loading
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    apiCall()
                }
                liveData.value = Resource.Success(result)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                val errorMessage = when (e) {
                    is UnknownHostException -> "Không có kết nối mạng"
                    is SocketTimeoutException -> "Kết nối quá chậm"
                    is IOException -> "Lỗi kết nối mạng"
                    else -> e.message ?: "Đã xảy ra lỗi"
                }
                liveData.value =
                    Resource.Error(errorMessage)
            }
        }
    }
}