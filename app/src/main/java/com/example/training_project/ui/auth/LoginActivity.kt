package com.example.training_project.ui.auth

import android.content.Intent
import com.example.training_project.MainActivity
import com.example.training_project.databinding.ActivityLoginBinding
import com.example.training_project.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: PreferenceManager
    override val viewModel: LoginViewModel by viewModel()

    override fun initView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = PreferenceManager.getInstance(this)
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