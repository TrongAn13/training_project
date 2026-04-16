package com.example.training_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.training_project.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adminEmail = "admin"
        val adminPassword = "123"

        binding.btnLogin.setOnClickListener {
            val pref = PreferenceManager(this)
            val inputEmail = binding.edtEmail.text.toString().trim()
            val inputPassword = binding.edtPassword.text.toString().trim()

            if (inputEmail == adminEmail && inputPassword == adminPassword) {
                pref.setLoggedIn(true)
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, R.string.validate, Toast.LENGTH_SHORT).show()
            }
        }
    }
}