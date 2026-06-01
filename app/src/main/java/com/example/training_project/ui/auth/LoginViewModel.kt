package com.example.training_project.ui.auth

import androidx.lifecycle.MutableLiveData
import com.example.domain.usecase.LoginUseCase
import com.example.ui.base.BaseViewModel
import com.example.ui.Resource
import com.example.ui.ResourceProvider

class LoginViewModel(private val resourceProvider: ResourceProvider, private val loginUseCase: LoginUseCase) : BaseViewModel(resourceProvider){
    val loginResult = MutableLiveData<Resource<String>>()
    fun login(username: String, password: String) {
        executeApi(loginResult) {
            loginUseCase(username, password)
        }
    }
}