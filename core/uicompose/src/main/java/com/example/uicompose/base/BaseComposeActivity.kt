package com.example.uicompose.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import com.example.uicompose.theme.AppTheme

abstract class BaseComposeActivity<VM : BaseComposeViewModel> : ComponentActivity() {
    protected abstract val viewModel: VM
    @Composable
    protected abstract fun ScreenContent()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val baseUiState by viewModel.baseUiState.collectAsStateWithLifecycle()

                BaseScreen(
                    baseUiState = baseUiState,
                    onClearError = { viewModel.clearError() }
                ) {
                    ScreenContent()
                }
            }
        }
    }
}