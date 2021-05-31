package io.techmeskills.an02onl_plannerapp.repositories

import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.database.dao.UsersDao
import io.techmeskills.an02onl_plannerapp.datastore.AppSettings
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext

class NotesRepository(
    private val notesDao: NotesDao,
    private val usersDao: UsersDao,
    private val appSettings: AppSettings,
    private val notificationRepository: NotificationRepository
) {

    @ExperimentalCoroutinesApi
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
            val id = notesDao.insertNote(
                note.copy(userName = appSettings.userName())
            )
            if (note.notificationOn) {
                notificationRepository.setNotification(note.copy(id = id, userName = appSettings.userName()))
            }
        }
    }

        suspend fun newNotes(notes: List<Note>) {
            withContext(Dispatchers.IO) {
                notesDao.clearAndInsertNotes(notes)
            }
        }

        suspend fun updateNote(note: Note) {
            withContext(Dispatchers.IO) {
                notesDao.getNoteById(note.id).let { oldNote ->
                    notificationRepository.unsetNotification(oldNote)
                }
                notesDao.updateNote(note)
                if (note.notificationOn) {
                    notificationRepository.setNotification(note)
                }
            }
        }

        suspend fun deleteNote(note: Note) {
            withContext(Dispatchers.IO) {
                notificationRepository.unsetNotification(note)
                notesDao.deleteNote(note)
            }
        }

        suspend fun deleteNoteById(noteId: Long) {
            withContext(Dispatchers.IO) {
                notesDao.getNoteById(noteId).let {
                    notificationRepository.unsetNotification(it)
                    notesDao.deleteNote(it)
                }
            }
        }

        suspend fun postponeNoteById(noteId: Long) {
            withContext(Dispatchers.IO) {
                val currentNote = notesDao.getNoteById(noteId)
                notificationRepository.unsetNotification(currentNote)
                val newNote = notificationRepository.postponeNotification(currentNote)
                notesDao.updateNote(newNote)
                notificationRepository.setNotification(newNote)
            }
        }
}