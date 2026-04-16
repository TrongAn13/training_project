package com.example.training_project

import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val pref = PreferenceManager(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if(pref.isLoggedIn()){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 2000)
    }
}