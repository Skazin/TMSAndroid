package io.techmeskills.an02onl_plannerapp.database

import android.content.Context
import androidx.room.*
import io.techmeskills.an02onl_plannerapp.BuildConfig
import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.database.dao.UsersDao
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.models.User

@Database(
    entities = [Note::class,
               User::class],
    version = 1,
    exportSchema = false
)

abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun usersDao(): UsersDao
}

object DatabaseConstructor {
    fun create(context: Context): NotesDatabase =
        Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
                "${BuildConfig.APPLICATION_ID}.db"
        ).build()
}