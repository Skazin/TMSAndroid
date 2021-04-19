package io.techmeskills.an02onl_plannerapp.database

import android.content.Context
import androidx.room.*
import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.models.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)

abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}

object DatabaseConstructor {
    fun create(context: Context): NotesDatabase =
        Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "io.techmeskills.an02onl_plannerapp.db"
        ).build()
}