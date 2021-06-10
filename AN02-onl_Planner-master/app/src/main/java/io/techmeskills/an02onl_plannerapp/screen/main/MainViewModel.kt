package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.repositories.CloudRepository
import io.techmeskills.an02onl_plannerapp.repositories.NotesRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val notesRepository: NotesRepository,
                    private val cloudRepository: CloudRepository
                    ) : CoroutineViewModel() {

    val progressLiveData = MutableLiveData<Boolean>()
    @ExperimentalCoroutinesApi
    var listLiveData = notesRepository.currentNotesFlow.flowOn(Dispatchers.IO).map { it }.asLiveData()
    val filterLiveData = MutableLiveData<List<Note>>()

    fun deleteNote(note: Note) {
        launch {
            notesRepository.deleteNote(note)
        }
    }

    fun exportNotes() = launch {
        val result = cloudRepository.exportNotes()
        progressLiveData.postValue(result)
    }

    fun importNotes() = launch {
        val result = cloudRepository.importNotes()
        progressLiveData.postValue(result)
    }

    fun filterNotes(text: String?) {
        launch {
            val currentNotes = notesRepository.getCurrentUserNotes()
            val filteredNotes = mutableListOf<Note>()

            for (item in currentNotes) {
                if (item.title.toLowerCase(Locale.ROOT)
                        .contains(text!!.toLowerCase(Locale.ROOT))) {
                    filteredNotes.add(item)
                }
                filterLiveData.postValue(filteredNotes)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun filterByAlphabetAZ() {
        launch {
            val currentNotes = notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedBy { it.title }
            filterLiveData.postValue(filteredNotes)
        }
    }

    @ExperimentalCoroutinesApi
    fun filterByAlphabetZA() {
        launch {
            val currentNotes = notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedBy { it.title }.reversed()
            filterLiveData.postValue(filteredNotes)
        }
    }

    @ExperimentalCoroutinesApi
    fun filterByDate19() {
        launch {
            val currentNotes = notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedBy { it.date }
            filterLiveData.postValue(filteredNotes)
        }
    }

    @ExperimentalCoroutinesApi
    fun filterByDate91() {
        launch {
            val currentNotes = notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedBy { it.date }.reversed()
            filterLiveData.postValue(filteredNotes)
        }
    }

    @ExperimentalCoroutinesApi
    fun filterByAdding19() {
        launch {
            val currentNotes = notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedBy { it.dateOfBirth }
            filterLiveData.postValue(filteredNotes)
        }
    }
    @ExperimentalCoroutinesApi
    fun filterByAdding91() {
        launch {
            val currentNotes = notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedBy { it.dateOfBirth }.reversed()
            filterLiveData.postValue(filteredNotes)
        }
    }
}