package com.zeroknowledgeinteractive.codevault.app
import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room

// @Database tells Room to generate the SQLite database code from these entities and DAOs.
@Database(
    entities = [Snippet::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    // Room generates the implementation for this DAO getter at build time.
    abstract fun snippetDao(): SnippetDAO

    companion object {

        // @Volatile makes sure every thread sees the latest INSTANCE value.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // A companion object is Kotlin's way to hold "static-like" members on a class.
        fun getDatabase(context: Context): AppDatabase {
            // Elvis operator (?:) returns INSTANCE if it exists, otherwise runs the synchronized block.
            return INSTANCE ?: synchronized(this) {
                // synchronized prevents two threads from creating the database at the same time.
                val instance = Room.databaseBuilder(
                    // applicationContext avoids leaking an Activity context.
                    context.applicationContext,
                    AppDatabase::class.java,
                    "snippet_database"
                )
                    .build()

                // Save the created database so future calls reuse the same object.
                INSTANCE = instance
                instance
            }
        }
    }
}

