package com.example.training_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.training_project.ui.auth.PreferenceManager
import com.example.uicompose.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val pref: PreferenceManager by inject()
    private var startRoute by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { startRoute == null }

        lifecycleScope.launch {
            val sessionId = pref.getSessionId()
            startRoute = if (sessionId.isNullOrEmpty()) {
                Screen.Login.route
            } else {
                Screen.Home.route
            }
        }
        setContent {
            AppTheme {
                startRoute?.let { route ->
                    MovieApp(startDestination = route)
                }
            }
        }
    }
}
