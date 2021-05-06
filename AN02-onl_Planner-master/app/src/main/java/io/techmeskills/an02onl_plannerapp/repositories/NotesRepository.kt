package io.techmeskills.an02onl_plannerapp.repositories

import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.database.dao.UsersDao
import io.techmeskills.an02onl_plannerapp.datastore.AppSettings
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext

class NotesRepository(
    private val notesDao: NotesDao,
    private val usersDao: UsersDao,
    private val appSettings: AppSettings,
    private val notificationRepository: NotificationRepository
) {

    val currentNotesFlow: Flow<List<Note>> =
        appSettings.userNameFlow()
            .flatMapLatest { userName ->
                notesDao.getAllNotesFlowByUserName(userName)
            }

    suspend fun getCurrentUserNotes(): List<Note> =
        usersDao.getUserContent(appSettings.userName())?.notes ?: emptyList()


    suspend fun setNotesSyncWithCloud() {
        withContext(Dispatchers.IO){
            notesDao.getAllNotesSyncWithCloud()
        }
    }

    suspend fun newNote(note: Note) {
        withContext(Dispatchers.IO) {
            notificationRepository.setNotification(note)
            notesDao.insertNote(
                Note(
                    title = note.title,
                    date = note.date,
                    userName = appSettings.userName()
                )
            )
        }
    }

    suspend fun newNotes(notes: List<Note>) {
        withContext(Dispatchers.IO) {
            notesDao.clearAndInsertNotes(notes)
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            val oldNote = notesDao.getNoteById(note.id)
            notificationRepository.unsetNotification(oldNote)
            notesDao.updateNote(note)
            notificationRepository.setNotification(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            notificationRepository.unsetNotification(note)
            notesDao.deleteNote(note)
        }
    }
}