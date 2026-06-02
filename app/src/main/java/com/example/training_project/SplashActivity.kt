package com.example.training_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.training_project.ui.auth.LoginActivity
import com.example.training_project.ui.auth.PreferenceManager
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {
    private val pref: PreferenceManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val targetActivity = if (pref.isLoggedIn()) {
            MainActivity::class.java
        } else {
            LoginActivity::class.java
        }

        startActivity(Intent(this, targetActivity))

        finish()
    }
}