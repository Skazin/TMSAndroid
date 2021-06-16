package io.techmeskills.an02onl_plannerapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNote(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNotes(notes: List<Note>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateNotes(notes: List<Note>)

    @Delete
    abstract fun deleteNote(note: Note)

    @Query("DELETE FROM notes")
    abstract fun clearTable()

    @Query("SELECT * FROM notes WHERE id == :noteId LIMIT 1")
    abstract fun getNoteById(noteId: Long): Note

    @Query("SELECT * FROM notes WHERE userName == :userName ORDER BY id DESC")
    abstract fun getAllNotesFlowByUserName(userName: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE userName == :userName ORDER BY id DESC")
    abstract fun getAllNotesByUserName(userName: String): List<Note>

    @Query("SELECT * FROM notes WHERE userName == :userName ORDER BY id DESC")
    abstract fun getAllNotesLiveDataByUserName(userName: String): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE :owner == userName ORDER BY ABS(:currTime-date) LIMIT 1")
    abstract fun getClosestNote(owner: String, currTime: Long): Note?

    @Query("UPDATE notes SET notePinned = :notePinned WHERE id = :noteId")
    abstract fun pinNote(noteId: Long, notePinned: Boolean)

    @Query("UPDATE notes SET fromCloud = 1")
    abstract fun getAllNotesSyncWithCloud()

    @Transaction
    open fun clearAndInsertNotes(notes: List<Note>) {
        clearTable()
        insertNotes(notes)
    }
}