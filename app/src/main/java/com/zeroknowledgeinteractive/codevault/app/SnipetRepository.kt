package com.zeroknowledgeinteractive.codevault.app
import kotlinx.coroutines.flow.Flow

// The repository is a small middle layer between the ViewModel and the DAO.
class SnippetRepository(private val dao: SnippetDAO) {

    // Flow is Kotlin's async stream type. Compose can observe it and update when DB data changes.
    val allSnippets: Flow<List<Snippet>> = dao.getAll()

    // suspend marks a function that must run from a coroutine instead of the main thread.
    suspend fun insert(snippet: Snippet) {
        dao.insert(snippet)
    }

    suspend fun update(snippet: Snippet) {
        dao.update(snippet)
    }

    suspend fun delete(snippet: Snippet) {
        dao.delete(snippet)
    }
}
