package com.example.ui.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ui.Resource

abstract class BaseFragment: Fragment() {
    abstract val viewModel: BaseViewModel
    abstract fun initView()
    abstract fun initListener()
    abstract fun observeLiveData()
    private var loadingDialog: LoadingDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        observeBaseState()
        observeLiveData()
    }

    private fun observeBaseState() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        viewModel.globalError.observe(viewLifecycleOwner) { message ->
            message?.let {
                showError(it)
                viewModel.globalError.value = null
            }
        }
    }

    protected open fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(requireContext())
            loadingDialog?.IfNotShowing()
        }
    }
    protected open fun hideLoading() {
        loadingDialog?.dismiss()
    }
    protected open fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun <T> handleApiState(
        resource: Resource<T>,
        onSuccess: (T) -> Unit
    ) {
        when (resource) {
            is Resource.Success -> {
                onSuccess(resource.data)
            }
            is Resource.Error -> {}
            is Resource.Loading -> {}
        }
    }

}