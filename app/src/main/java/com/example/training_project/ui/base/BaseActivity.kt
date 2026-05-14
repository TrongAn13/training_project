package com.example.training_project.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.training_project.utils.Resource
import com.example.training_project.R

abstract class BaseActivity : AppCompatActivity() {
    abstract val viewModel: BaseViewModel

    abstract fun initView()
    abstract fun initListener()
    abstract fun observeLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
        observeBaseState()
        observeLiveData()
    }
    private var loadingDialog: android.app.Dialog? = null

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
            loadingDialog = android.app.Dialog(this).apply {
                setContentView(R.layout.layout_loading_dialog)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                setCancelable(false)
            }
        }
        if (loadingDialog?.isShowing == false) {
            loadingDialog?.show()
        }
    }

    protected open fun hideLoading() {
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
    }

    protected open fun showError(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
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