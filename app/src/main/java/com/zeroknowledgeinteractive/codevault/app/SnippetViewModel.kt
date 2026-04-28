package com.zeroknowledgeinteractive.codevault.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel survives configuration changes and keeps UI data out of composable functions.
class SnippetViewModel(private val repository: SnippetRepository) : ViewModel() {

    // stateIn converts Flow into a StateFlow so Compose can read the latest list as screen state.
    val snippets = repository.allSnippets.stateIn(
        // viewModelScope is a coroutine scope tied to this ViewModel's lifecycle.
        scope = viewModelScope,
        // WhileSubscribed keeps the upstream Flow active while the UI is observing it.
        started = SharingStarted.WhileSubscribed(5000),
        // Initial value is shown before Room emits the real database content.
        initialValue = emptyList()
    )

    fun insert(snippet: Snippet) {
        // launch starts a coroutine so the suspend repository call can run safely.
        viewModelScope.launch {
            repository.insert(snippet)
        }
    }

    fun update(snippet: Snippet) {
        viewModelScope.launch {
            repository.update(snippet)
        }
    }

    fun delete(snippet: Snippet) {
        viewModelScope.launch {
            repository.delete(snippet)
        }
    }
}

// Compose's default viewModel() needs a factory when the ViewModel constructor has parameters.
class SnippetViewModelFactory(
    private val repository: SnippetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SnippetViewModel::class.java)) {
            // Kotlin generics are erased at runtime, so this cast is needed after the type check above.
            @Suppress("UNCHECKED_CAST")
            return SnippetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
