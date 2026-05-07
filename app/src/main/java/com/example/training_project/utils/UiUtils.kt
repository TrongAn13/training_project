package com.example.training_project.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
fun <T> Fragment.handleApiState(
    resource: Resource<T>,
    loadingView: View? = null,
    contentView: View? = null,
    onSuccess: (T) -> Unit
) {
    handleResourceState(
        resource,
        contentView,
        loadingView,
        onSuccess
    )
}
fun <T> AppCompatActivity.handleApiState(
    resource: Resource<T>,
    contentView : View? = null,
    loadingView : View? = null,
    onSuccess: (T) -> Unit
) {
    handleResourceState(
        resource,
        contentView,
        loadingView,
        onSuccess
    )
}
private fun <T> handleResourceState(
    resource: Resource<T>,
    contentView: View? = null,
    loadingView: View? = null,
    onSuccess: (T) -> Unit
) {
    when (resource) {
        is Resource.Loading -> {
            loadingView?.visibility = View.VISIBLE
            contentView?.visibility = View.INVISIBLE
        }
        is Resource.Error -> {
            loadingView?.visibility = View.VISIBLE
            contentView?.visibility = View.INVISIBLE
        }
        is Resource.Success -> {
            loadingView?.visibility = View.GONE
            contentView?.visibility = View.VISIBLE
            onSuccess(resource.data)
        }
    }
}

fun <T> ViewModel.executeApi(
    liveData: MutableLiveData<Resource<T>>,
    apiCall: suspend () -> T
) {

    liveData.value = Resource.Loading

    viewModelScope.launch {
        try {
            val result = withContext(Dispatchers.IO) { apiCall() }
            liveData.value = Resource.Success(result)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            val errorMessage = when (e) {
                is UnknownHostException ->
                    "Không có kết nối mạng"
                is SocketTimeoutException ->
                    "Kết nối quá chậm, vui lòng thử lại"
                is IOException ->
                    ""
                else ->
                    e.message ?: "Đã xảy ra lỗi"
            }
            liveData.value = Resource.Error(errorMessage)
        }
    }
}

class NetworkConnectionLiveData(context: Context) : MutableLiveData<Boolean>() {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<Network> = HashSet()
    private fun checkValidNetworks() {
        val isConnected = validNetworks.isNotEmpty()
        if (value != isConnected) {
            postValue(isConnected)
        }
    }

    private val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                val hasInternet = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
                val isValidated = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
                if (hasInternet && isValidated) {
                    validNetworks.add(network)
                }
                checkValidNetworks()
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                val isValidated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                if (hasInternet && isValidated) {
                    validNetworks.add(network)
                } else {
                    validNetworks.remove(network)
                }
                checkValidNetworks()
            }

            override fun onLost(network: Network) {
                validNetworks.remove(network)
                checkValidNetworks()
            }
        }

    override fun onActive() {
        super.onActive()
        
        validNetworks.clear()
        val activeNetwork = connectivityManager.activeNetwork
        if (activeNetwork != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            if (capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            ) {
                validNetworks.add(activeNetwork)
            }
        }
        checkValidNetworks()

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(
            request,
            callback
        )
    }

    override fun onInactive() {
        super.onInactive()

        try {
            connectivityManager.unregisterNetworkCallback(callback)
        } catch (_: Exception) {
        }
    }
}

fun AppCompatActivity.observeNetwork(viewForSnackbar: View,onNetworkAvailable: (() -> Unit)? = null) {
    var snackbar: Snackbar? = null

    NetworkConnectionLiveData(this)
        .observe(this) { isConnected ->
            if (!isConnected) {
                if (snackbar?.isShown != true) {
                    snackbar = Snackbar.make(viewForSnackbar, "Không có kết nối mạng", Snackbar.LENGTH_INDEFINITE)
                        .apply {
                        show()
                    }
                }
            } else {
                snackbar?.dismiss()
                snackbar = null
                onNetworkAvailable?.invoke()
            }
        }
}