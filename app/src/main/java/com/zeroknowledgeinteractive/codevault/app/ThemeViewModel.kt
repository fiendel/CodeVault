package com.zeroknowledgeinteractive.codevault.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val themePreferences = ThemePreferences(application)
    
    val isDarkMode: Flow<Boolean?> = themePreferences.isDarkMode

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            themePreferences.saveDarkMode(isDark)
        }
    }

    fun clearDarkMode() {
        viewModelScope.launch {
            // We can implement clear in ThemePreferences if needed
            // For now, let's just add it there
            themePreferences.clearDarkMode()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return ThemeViewModel(application) as T
            }
        }
    }
}
