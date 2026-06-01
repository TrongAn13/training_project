package com.example.training_project.ui.auth

import android.content.Intent
import com.example.training_project.MainActivity
import com.example.training_project.databinding.ActivityLoginBinding
import com.example.ui.base.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val pref: PreferenceManager by inject()

    override val viewModel: LoginViewModel by viewModel()

    override fun initView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (pref.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    override fun initListener() {
        binding.btnLogin.setOnClickListener {
            val username = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            viewModel.login(username, password)
        }
    }

    override fun observeLiveData() {
        viewModel.loginResult.observe(this) { resource ->
            handleApiState(resource){sessionId ->
                pref.saveSessionId(sessionId)
                pref.setLoggedIn(true)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}