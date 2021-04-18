package io.techmeskills.an02onl_plannerapp.screen.newscreen

import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class EditFragmentViewModel(private val notesDao: NotesDao) : CoroutineViewModel()  {

    fun updateNote(note: Note) {
        launch {
            notesDao.updateNote(note)
        }
    }
}