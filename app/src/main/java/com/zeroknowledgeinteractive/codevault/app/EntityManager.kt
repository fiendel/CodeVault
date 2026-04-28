package com.zeroknowledgeinteractive.codevault.app

import androidx.room.Entity
import androidx.room.PrimaryKey

// This data class represents one row in the "snippets" Room table.
@Entity(tableName = "snippets")
data class Snippet(
    // autoGenerate lets SQLite create the id value for each new saved snippet.
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val code: String,
    val description: String,
    val language: String
)
