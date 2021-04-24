package io.techmeskills.an02onl_plannerapp.repositories

import io.techmeskills.an02onl_plannerapp.cloud.CloudNote
import io.techmeskills.an02onl_plannerapp.cloud.CloudUser
import io.techmeskills.an02onl_plannerapp.cloud.ExportNotesRequestBody
import io.techmeskills.an02onl_plannerapp.cloud.IApi
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.flow.first

class CloudRepository(
    private val apiInterface: IApi,
    private val usersRepository: UsersRepository,
    private val notesRepository: NotesRepository
) {

    suspend fun exportNotes(): Boolean {
        val user = usersRepository.getCurrentUserFlow().first()
        val notes = notesRepository.getCurrentUserNotes()
        val cloudUser = CloudUser(userId = user.id, userName = user.name)
        val cloudNotes = notes.map { CloudNote(id = it.id, title = it.title, date = it.date) }
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
        val notes = cloudNote.map { cloudNote ->
            Note(
                title = cloudNote.title,
                date = cloudNote.date,
                userId = user.id,
                fromCloud = true
            )
        }
        notesRepository.newNotes(notes)
        return response.isSuccessful
    }
}