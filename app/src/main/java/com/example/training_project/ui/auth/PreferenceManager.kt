package com.example.training_project.ui.auth

import android.content.Context

class PreferenceManager(context: Context) {
    private val sharedPreferences = context.applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: PreferenceManager? = null

        fun getInstance(context: Context): PreferenceManager {
            return instance ?: synchronized(this) {
                instance ?: PreferenceManager(context.applicationContext).also { instance = it }
            }
        }
    }
    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("KEY_IS_LOGGED_IN", isLoggedIn).apply()
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("KEY_IS_LOGGED_IN", false)
    }
}