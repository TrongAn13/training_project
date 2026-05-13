package com.example.training_project.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.training_project.utils.Resource

abstract class BaseFragment: Fragment() {
    abstract fun initView()
    abstract fun initListener()
    abstract fun observeLiveData()

    protected var loadingView: View? = null
    protected var contentView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        observeLiveData()
    }

    fun bindLoadingViews(loading: View?, content: View?) {
        loadingView = loading
        contentView = content
    }
    fun <T> handleApiState(
        resource: Resource<T>,
        showError: Boolean = false,
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
                if (showError && resource.message.isNotEmpty()) {
                    android.widget.Toast.makeText(requireContext(), resource.message, android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            is Resource.Success -> {
                loadingView?.visibility = View.GONE
                contentView?.visibility = View.VISIBLE
                onSuccess(resource.data)
            }
        }
    }
}