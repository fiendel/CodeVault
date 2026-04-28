package com.zeroknowledgeinteractive.codevault.app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// DAO stands for Data Access Object. It defines the database operations Room will implement.
@Dao
interface SnippetDAO {

    // suspend means Room will run this insert from a coroutine-friendly background call.
    @Insert
    suspend fun insert(item: Snippet)

    // Update writes changes to an existing snippet row that already has an id.
    @Update
    suspend fun update(item: Snippet)

    @Delete
    suspend fun delete(item: Snippet)

    // Returning Flow lets the UI react automatically whenever the table changes.
    @Query("SELECT * FROM snippets")
     fun getAll(): Flow<List<Snippet>>

    // This version returns one snippet directly for a matching id.
    @Query("SELECT * FROM snippets WHERE id = :id")
     fun getById(id: Int): Snippet?
}
