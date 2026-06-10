package com.example.training_project.ui.auth

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.lifecycleScope
import com.example.training_project.MainActivity
import com.example.ui.R
import com.example.training_project.databinding.ActivityLoginBinding
import com.example.ui.base.BaseActivity
import com.example.uicompose.component.AppButton
import com.example.uicompose.component.AppTextField
import com.example.uicompose.theme.AppTheme
import com.example.uicompose.theme.button_login_text
import com.example.uicompose.theme.primary_blue
import com.example.uicompose.theme.white
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val pref: PreferenceManager by inject()

    override val viewModel: LoginViewModel by viewModel()

    private var email = mutableStateOf("")
    private var password = mutableStateOf("")

    override fun initView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPasswordField()
        setupEmailField()
        setupLoginButton()
        setupGmailButton()
        setupFacebookButton()
    }
    override fun initListener() {}
    private fun setupLoginButton(){
        binding.btnLogin.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    AppButton(
                        text = getString(R.string.login_title),
                        onClick = {
                            viewModel.login(email.value, password.value)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = white,
                            contentColor = button_login_text
                        )
                    )
                }
            }
        }
    }
    private fun setupGmailButton(){
        binding.btngm.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    AppButton(
                        text = getString(R.string.gmail_login),
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primary_blue,
                            contentColor = white
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_gg),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    )
                }
            }
        }
    }
    private fun setupFacebookButton(){
        binding.btnfb.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    AppButton(
                        text = getString(R.string.facebook_login),
                        onClick = {},
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primary_blue,
                            contentColor = white
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_fb),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    )
                }
            }
        }
    }
    private fun setupEmailField() {
        binding.edtEmail.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                AppTheme {
                    AppTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        hint = getString(R.string.hint_email),
                        modifier = Modifier.fillMaxSize(),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mail),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    )
                }
            }
        }
    }
    private fun setupPasswordField() {
        binding.edtPassword.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                AppTheme {
                    AppTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        hint = getString(R.string.hint_password),
                        modifier = Modifier.fillMaxSize(),
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lock),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    )
                }
            }
        }
    }

    override fun observeLiveData() {
        viewModel.loginResult.observe(this) { resource ->
            handleApiState(resource){sessionId ->
                lifecycleScope.launch {
                    pref.saveSessionId(sessionId)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}