package io.techmeskills.an02onl_plannerapp.screen.newnote

import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.repositories.NotesRepository
import io.techmeskills.an02onl_plannerapp.repositories.UsersRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class NewFragmentViewModel(private val notesRepository: NotesRepository,
                           private val usersRepository: UsersRepository
                           ) : CoroutineViewModel() {

    fun addNewNote(note: Note) {
        launch {
            notesRepository.newNote(note)
        }
    }
}