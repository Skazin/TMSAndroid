package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.repositories.NotesRepository
import io.techmeskills.an02onl_plannerapp.repositories.UsersRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val notesRepository: NotesRepository,
                    private val usersRepository: UsersRepository) : CoroutineViewModel() {

    val listLiveData = notesRepository.currentNotesFlow.flowOn(Dispatchers.IO).map {
        listOf(AddNewNote) + it }.asLiveData()

    fun deleteNote(note: Note) {
        launch {
            notesRepository.deleteNote(note)
        }
    }

    fun logout() {
        launch {
            usersRepository.logout()
        }
    }
}

object AddNewNote : Note(-1, "", "")