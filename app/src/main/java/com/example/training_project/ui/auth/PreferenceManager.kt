package com.example.training_project.ui.auth

import android.content.Context

class PreferenceManager(context: Context) {
    private val sharedPreferences = context.applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN"
        private const val KEY_SESSION_ID = "KEY_SESSION_ID"
        @Volatile
        private var instance: PreferenceManager? = null

        fun getInstance(context: Context): PreferenceManager {
            return instance ?: synchronized(this) {
                instance ?: PreferenceManager(context.applicationContext).also { instance = it }
            }
        }
    }
    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    fun saveSessionId(sessionId: String) {
        sharedPreferences.edit()
            .putString(KEY_SESSION_ID, sessionId)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }
}