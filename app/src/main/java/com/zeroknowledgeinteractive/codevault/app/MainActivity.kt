package com.zeroknowledgeinteractive.codevault.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zeroknowledgeinteractive.codevault.ui.theme.CodeVaultTheme
import androidx.compose.foundation.isSystemInDarkTheme

// MainActivity is the Android entry point. Android creates this class first when the app opens.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Draw app content behind the system bars so Compose can manage the full screen.
        enableEdgeToEdge()

        // setContent starts the Jetpack Compose UI tree instead of using XML layouts.
        setContent {
            val themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModel.Factory)
            val themePreference by themeViewModel.isDarkMode.collectAsStateWithLifecycle(initialValue = null)
            
            val useDarkTheme = themePreference ?: isSystemInDarkTheme()

            CodeVaultTheme(darkTheme = useDarkTheme) {
                // Scaffold is a Material container that helps place screen content and handles insets.
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // Pass the Scaffold padding down so navigation screens do not draw under system UI.
                    AppNav(innerPadding = innerPadding, themeViewModel = themeViewModel)
                }
            }
        }
    }
}
