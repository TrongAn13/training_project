package com.example.training_project.ui.auth

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


private val Context.dataStore by preferencesDataStore(name = "secure_preferences")
class PreferenceManager(private val context: Context, private val cryptoManager: CryptoManager = CryptoManager()) {
    companion object {
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("KEY_IS_LOGGED_IN")
        private val KEY_SESSION_ID = stringPreferencesKey("KEY_SESSION_ID")
    }
    suspend fun saveSessionId(sessionId: String) {
        val encryptedSessionId = cryptoManager.encrypt(sessionId)

        context.dataStore.edit { prefs ->
            prefs[KEY_SESSION_ID] = encryptedSessionId
            prefs[KEY_IS_LOGGED_IN] = true
        }
    }
    suspend fun isLoggedIn(): Boolean {
        return context.dataStore.data.first()[KEY_IS_LOGGED_IN] ?: false
    }
}