package com.example.training_project.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.training_project.utils.Resource

abstract class BaseFragment: Fragment() {
    abstract val viewModel: BaseViewModel
    abstract fun initView()
    abstract fun initListener()
    abstract fun observeLiveData()
    private var loadingDialog: android.app.Dialog? = null

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
            loadingDialog = android.app.Dialog(requireContext()).apply {
                setContentView(com.example.training_project.R.layout.layout_loading_dialog)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                setCancelable(false)
            }
        }

        if (loadingDialog?.isShowing != true) {
            loadingDialog?.show()
        }
    }
    protected open fun hideLoading() {
        loadingDialog?.dismiss()
    }
    protected open fun showError(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
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