package com.example.training_project.ui.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.training_project.utils.Resource

open class BaseActivity : AppCompatActivity() {
    fun <T> handleApiState(
        resource: Resource<T>,
        loadingView: View? = null,
        contentView: View? = null,
        showError: Boolean = true,
        onSuccess: (T) -> Unit
    ) {
        when (resource) {
            is Resource.Loading -> {
                loadingView?.visibility = View.VISIBLE
                contentView?.visibility = View.INVISIBLE
            }
            is Resource.Error -> {
                loadingView?.visibility = View.GONE
                contentView?.visibility = View.INVISIBLE
                if (showError && resource.message.isNotEmpty()) {
                    android.widget.Toast.makeText(this, resource.message, android.widget.Toast.LENGTH_SHORT).show()
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