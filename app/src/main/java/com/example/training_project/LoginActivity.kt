package com.example.training_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtEmail = findViewById<android.widget.EditText>(R.id.edtEmail)
        val edtPassword = findViewById<android.widget.EditText>(R.id.edtPassword)
        val btnLogin = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnLogin)

        val adminEmail = "admin"
        val adminPassword = "123"

        btnLogin.setOnClickListener {
            val inputEmail = edtEmail.text.toString().trim()
            val inputPassword = edtPassword.text.toString().trim()

            if (inputEmail == adminEmail && inputPassword == adminPassword) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}