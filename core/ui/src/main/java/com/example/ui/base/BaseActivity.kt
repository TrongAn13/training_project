package com.example.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ui.Resource

abstract class BaseActivity : AppCompatActivity() {
    abstract val viewModel: BaseViewModel

    abstract fun initView()
    abstract fun initListener()
    abstract fun observeLiveData()
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
        observeBaseState()
        observeLiveData()
    }
    private fun observeBaseState() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        viewModel.globalError.observe(this) { message ->
            message?.let {
                showError(it)
                viewModel.globalError.value = null
            }
        }
    }

    protected open fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this)
        }
        loadingDialog?.show()
    }

    protected open fun hideLoading() {
        loadingDialog?.dismiss()
    }

    protected open fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun <T> handleApiState(
        resource: Resource<T>,
        onSuccess: (T) -> Unit
    ) {
        when (resource) {
            is Resource.Success -> {
                onSuccess(resource.data)
            }
            is Resource.Error -> {
            }
            is Resource.Loading -> {
            }
        }
    }
}