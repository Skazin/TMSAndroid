package io.techmeskills.an02onl_plannerapp.repositories

import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.datastore.AppSettings
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext

class NotesRepository(private val notesDao: NotesDao, private val appSettings: AppSettings) {

    val currentNotesFlow: Flow<List<Note>> =
        appSettings.userIdFlow()
            .flatMapLatest { userId ->
                notesDao.getAllNotesFlowByUserId(userId)
            }

    suspend fun getCurrentUserNotes(): List<Note> =
        notesDao.getAllNotesByUserId(appSettings.userId())


    suspend fun setNotesSyncWithCloud() {
        withContext(Dispatchers.IO){
            notesDao.getAllNotesSyncWithCloud()
        }
    }

    suspend fun newNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.insertNote(Note(
                    title = note.title,
                    date = note.date,
                    userId = appSettings.userId()
            ))
        }
    }

    suspend fun newNotes(notes: List<Note>) {
        withContext(Dispatchers.IO) {
            notesDao.insertNotes(notes)
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.updateNote(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.deleteNote(note)
        }
    }
}