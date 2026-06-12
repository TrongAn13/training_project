package com.example.uicompose.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uicompose.theme.AppTheme

abstract class BaseComposeFragment<VM : BaseComposeViewModel> : Fragment() {
    protected abstract val viewModel: VM
    @Composable
    protected abstract fun ScreenContent()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
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
}