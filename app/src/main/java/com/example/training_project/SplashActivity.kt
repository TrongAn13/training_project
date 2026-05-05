package com.example.training_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.training_project.ui.auth.LoginActivity
import com.example.training_project.ui.auth.PreferenceManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val pref = PreferenceManager.getInstance(this)

        val targetActivity = if (pref.isLoggedIn()) {
            MainActivity::class.java
        } else {
            LoginActivity::class.java
        }

        startActivity(Intent(this, targetActivity))

        finish()
    }
}