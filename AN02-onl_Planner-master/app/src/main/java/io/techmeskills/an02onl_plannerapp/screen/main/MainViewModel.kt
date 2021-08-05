package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.repositories.CloudRepository
import io.techmeskills.an02onl_plannerapp.repositories.NotesRepository
import io.techmeskills.an02onl_plannerapp.repositories.Result
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class MainViewModel(private val notesRepository: NotesRepository,
                    private val cloudRepository: CloudRepository
                    ) : CoroutineViewModel() {

    private var filterText = ""
    private var filterType = FilterType.ADD19
    var listLiveData: LiveData<List<Note>> = MutableLiveData()

    init {
        launch {
            notesRepository.currentNotesFlow.collectLatest {
                bigFilter(it)
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun bigFilter(list: List<Note>) {
        when(filterType) {
            FilterType.AZ -> filterByAlphabetAZ(list.filter {it.title.contains(filterText)})
            FilterType.ZA -> filterByAlphabetZA(list.filter {it.title.contains(filterText)})
            FilterType.DATE19 -> filterByDate19(list.filter {it.title.contains(filterText)})
            FilterType.DATE91 -> filterByDate91(list.filter {it.title.contains(filterText)})
            FilterType.ADD19 -> filterByAdding19(list.filter {it.title.contains(filterText)})
            FilterType.ADD91 -> filterByDate91(list.filter {it.title.contains(filterText)})
            FilterType.PIN -> sortByPin(list)
        }
    }

    fun deleteNote(note: Note) {
        launch {
            notesRepository.deleteNote(note)
        }
    }

    fun pinNote(note: Note) {
        launch {
            notesRepository.pinNote(note)
            sortByPin()
        }
    }

    private fun sortByPin(notes: List<Note>? = null) {
        filterType = FilterType.PIN
        launch {
            val currentNotes = notes ?: notesRepository.getCurrentUserNotes()
            val sortedByPin = currentNotes.sortedByDescending { it.notePinned }
            (listLiveData as MutableLiveData).postValue(sortedByPin)
        }
    }

    fun exportNotes(callback : (Result) -> Unit) = launch {
        val result = cloudRepository.exportNotes()
        withContext(Dispatchers.Main) {
            callback(result)
        }
    }

    fun importNotes(callback : (Result) -> Unit) = launch {
        val result = cloudRepository.importNotes()
        withContext(Dispatchers.Main) {
            callback(result)
        }
    }

    fun filterNotes(text: String?) {
        filterText = text ?: ""
        launch {
            val list = notesRepository.getCurrentUserNotes()
            bigFilter(list)
        }
    }

    @ExperimentalCoroutinesApi
    fun filterByAlphabetAZ(notes: List<Note>? = null) {
        filterType = FilterType.AZ
        launch {
            val currentNotes = notes ?: notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedBy { it.title }.filter {it.title.contains(filterText)}
            (listLiveData as MutableLiveData).postValue(filteredNotes)
        }
    }

    @ExperimentalCoroutinesApi
    fun filterByAlphabetZA(notes: List<Note>? = null) {
        filterType = FilterType.ZA
        launch {
            val currentNotes = notes ?: notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedByDescending { it.title }.filter {it.title.contains(filterText)}
            (listLiveData as MutableLiveData).postValue(filteredNotes)
        }
    }

    @ExperimentalCoroutinesApi
    fun filterByDate19(notes: List<Note>? = null) {
        filterType = FilterType.DATE19
        launch {
            val currentNotes = notes ?: notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedBy { it.date }.filter {it.title.contains(filterText)}
            (listLiveData as MutableLiveData).postValue(filteredNotes)
        }
    }

    @ExperimentalCoroutinesApi
    fun filterByDate91(notes: List<Note>? = null) {
        filterType = FilterType.DATE91
        launch {
            val currentNotes = notes ?: notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedByDescending { it.date }.filter {it.title.contains(filterText)}
            (listLiveData as MutableLiveData).postValue(filteredNotes)
        }
    }

    @ExperimentalCoroutinesApi
    fun filterByAdding19(notes: List<Note>? = null) {
        filterType = FilterType.ADD19
        launch {
            val currentNotes = notes ?: notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedByDescending { it.dateOfBirth }.filter {it.title.contains(filterText)}
            (listLiveData as MutableLiveData).postValue(filteredNotes)
        }
    }
    @ExperimentalCoroutinesApi
    fun filterByAdding91(notes: List<Note>? = null) {
        filterType = FilterType.ADD91
        launch {
            val currentNotes = notes ?: notesRepository.getCurrentUserNotes()
            val filteredNotes = currentNotes.sortedBy { it.dateOfBirth }.filter {it.title.contains(filterText)}
            (listLiveData as MutableLiveData).postValue(filteredNotes)
        }
    }
}

enum class FilterType {
    AZ,
    ZA,
    DATE19,
    DATE91,
    ADD19,
    ADD91,
    PIN
}