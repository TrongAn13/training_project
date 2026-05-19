package com.example.training_project.ui.auth

import androidx.lifecycle.MutableLiveData
import com.example.training_project.domain.usecase.LoginUseCase
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.utils.Resource
import com.example.training_project.utils.ResourceProvider

class LoginViewModel(private val resourceProvider: ResourceProvider,private val loginUseCase: LoginUseCase) : BaseViewModel(resourceProvider){
    val loginResult = MutableLiveData<Resource<String>>()
    fun login(username: String, password: String) {
        executeApi(loginResult) {
            loginUseCase(username, password)
        }
    }
}