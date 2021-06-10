package io.techmeskills.an02onl_plannerapp.repositories

import io.techmeskills.an02onl_plannerapp.cloud.CloudNote
import io.techmeskills.an02onl_plannerapp.cloud.CloudUser
import io.techmeskills.an02onl_plannerapp.cloud.ExportNotesRequestBody
import io.techmeskills.an02onl_plannerapp.cloud.IApi
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.flow.first
import java.util.*

class CloudRepository(
    private val apiInterface: IApi,
    private val usersRepository: UsersRepository,
    private val notesRepository: NotesRepository
) {

    private val calendar = Calendar.getInstance()

    suspend fun exportNotes(): Boolean {
        val user = usersRepository.getCurrentUserFlow().first()
        val notes = notesRepository.getCurrentUserNotes()
        val cloudUser = CloudUser(userName = user.name)
        val cloudNotes = notes.map {
            CloudNote(
            title = it.title,
            date = it.date,
            alarmEnabled = it.notificationOn,
            noteColor = it.backgroundColor,
            notePinned = it.notePinned
            )
        }
        val exportNotesRequestBody = ExportNotesRequestBody(cloudUser, usersRepository.phoneId, cloudNotes)
        val exportResult = apiInterface.exportNotes(exportNotesRequestBody).isSuccessful
        if(exportResult) {
            notesRepository.setNotesSyncWithCloud()
        }
        return exportResult
    }

    suspend fun importNotes(): Boolean {
        val user = usersRepository.getCurrentUserFlow().first()
        val response = apiInterface.importNotes(user.name, usersRepository.phoneId)
        val cloudNote= response.body() ?: emptyList()
        val importedNotes = cloudNote.map { cloudNote ->
            Note(
                title = cloudNote.title,
                date = cloudNote.date,
                userName = user.name,
                fromCloud = true,
                notificationOn = cloudNote.alarmEnabled,
                dateOfBirth = calendar.timeInMillis,
                backgroundColor = cloudNote.noteColor,
                notePinned = cloudNote.notePinned
            )
        }
        val currentNotes = notesRepository.getCurrentUserNotes()
        val resultList = (importedNotes + currentNotes).distinctBy { it.title + it.date }
        notesRepository.newNotes(resultList)
        return response.isSuccessful

    }
}