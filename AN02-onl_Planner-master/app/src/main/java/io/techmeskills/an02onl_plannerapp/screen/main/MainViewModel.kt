package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val notesDao: NotesDao) : CoroutineViewModel() {

    val listLiveData = notesDao.getAllNotesFlow().map {
        listOf(AddNewNote) + it }.flowOn(Dispatchers.IO).asLiveData()

    fun deleteNote(note: Note) {
        launch {
            notesDao.deleteNote(note)
        }
    }

}

object AddNewNote : Note(-1, "", "")