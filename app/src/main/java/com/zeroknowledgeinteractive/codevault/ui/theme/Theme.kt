package com.zeroknowledgeinteractive.codevault.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = VaultPrimaryDark,
    onPrimary = VaultOnPrimaryDark,
    primaryContainer = VaultPrimaryContainerDark,
    onPrimaryContainer = VaultOnPrimaryContainerDark,
    secondary = VaultSecondaryDark,
    onSecondary = VaultOnSecondaryDark,
    background = VaultBackgroundDark,
    onBackground = VaultOnBackgroundDark,
    surface = VaultSurfaceDark,
    onSurface = VaultOnSurfaceDark,
    surfaceVariant = VaultSurfaceVariantDark,
    onSurfaceVariant = VaultOnSurfaceVariantDark,
    outline = VaultOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = VaultPrimaryLight,
    onPrimary = VaultOnPrimaryLight,
    primaryContainer = VaultPrimaryContainerLight,
    onPrimaryContainer = VaultOnPrimaryContainerLight,
    secondary = VaultSecondaryLight,
    onSecondary = VaultOnSecondaryLight,
    background = VaultBackgroundLight,
    onBackground = VaultOnBackgroundLight,
    surface = VaultSurfaceLight,
    onSurface = VaultOnSurfaceLight,
    surfaceVariant = VaultSurfaceVariantLight,
    onSurfaceVariant = VaultOnSurfaceVariantLight,
    outline = VaultOutlineLight
)

@Composable
fun CodeVaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
