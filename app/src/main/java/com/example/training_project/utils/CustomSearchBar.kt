package com.example.training_project.utils

import android.content.Context
import android.os.IBinder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.training_project.databinding.LayoutCustomSearchBinding

class CustomSearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutCustomSearchBinding.inflate(LayoutInflater.from(context), this)

    fun setonCustomClickListener(listener: () -> Unit){
        binding.etSearchInput.setOnClickListener { listener() }
        binding.ivSearchIcon.setOnClickListener { listener() }
        this.setOnClickListener { listener() }
    }
    fun setReadOnlyMode(onClick: () -> Unit) {
        binding.etSearchInput.isFocusable = false
        binding.etSearchInput.isClickable = true
        binding.etSearchInput.setOnClickListener { onClick() }
        binding.ivSearchIcon.setOnClickListener { onClick() }
        this.setOnClickListener { onClick() }
    }

    fun onTextChanged(action: (String) -> Unit) {
        binding.etSearchInput.addTextChangedListener {
            action(it?.toString()?.trim() ?: "")
        }
    }

    fun onKeyboardSearchClick(action: () -> Unit) {
        binding.etSearchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                action()
                true
            } else {
                false
            }
        }
    }

    fun clearSearchFocus() {
        binding.etSearchInput.clearFocus()
    }

    fun getSearchWindowToken(): IBinder {
        return binding.etSearchInput.windowToken
    }

    fun hideKeyboard() {
        val imm = ContextCompat.getSystemService(context, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(binding.etSearchInput.windowToken, 0)
        clearSearchFocus()
    }
    fun focusAndShowKeyboard() {
        binding.etSearchInput.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearchInput, InputMethodManager.SHOW_IMPLICIT)
    }
}