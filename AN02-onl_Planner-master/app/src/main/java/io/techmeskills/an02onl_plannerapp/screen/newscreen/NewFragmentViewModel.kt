package io.techmeskills.an02onl_plannerapp.screen.newscreen

import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.repositories.NotesRepository
import io.techmeskills.an02onl_plannerapp.repositories.UsersRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewFragmentViewModel(private val notesRepository: NotesRepository,
                           private val usersRepository: UsersRepository
                           ) : CoroutineViewModel() {

    fun addNewNote(note: Note) {
        launch {
            notesRepository.newNote(note)
        }
    }
}