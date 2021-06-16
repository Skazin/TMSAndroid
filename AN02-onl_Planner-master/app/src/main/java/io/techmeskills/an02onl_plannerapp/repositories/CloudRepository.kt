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

    suspend fun exportNotes(): Result {
        val user = usersRepository.getCurrentUserFlow().first()
        val notes = notesRepository.getCurrentUserNotes()
        if (notes.isEmpty()) return Result.NO_NOTES_EXPORT
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
        return Result.ALL_GOOD_EXPORT
    }

    suspend fun importNotes(): Result {
        val user = usersRepository.getCurrentUserFlow().first()
        val response = apiInterface.importNotes(user.name, usersRepository.phoneId)
        val cloudNote= response.body() ?: emptyList()
        if (cloudNote.isEmpty()) return Result.NO_NOTES_IMPORT
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
        for (itemC in currentNotes) {
            for (itemR in importedNotes) {
                if (itemC.title == itemR.title && itemC.date == itemR.date) return Result.NO_NEW_NOTES_IMPORT
            }
        }
        val resultList = (importedNotes + currentNotes).distinctBy { it.title + it.date }
        notesRepository.newNotes(resultList)
        return Result.ALL_GOOD_IMPORT

    }
}

enum class Result {
    NO_NOTES_IMPORT,
    NO_NOTES_EXPORT,
    NO_NEW_NOTES_IMPORT,
    ALL_GOOD_IMPORT,
    ALL_GOOD_EXPORT
}