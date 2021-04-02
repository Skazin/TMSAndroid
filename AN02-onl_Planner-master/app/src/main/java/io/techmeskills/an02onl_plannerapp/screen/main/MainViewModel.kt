package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.MutableLiveData
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class MainViewModel : CoroutineViewModel() {

    val listLiveData = MutableLiveData(
        listOf(
                Note("Амброксол", "Отхаркивающее"),
                Note("Диклофенак", "НПВС"),
                Note("Кетопрофен", "НПВС"),
                Note("Ацетилцистеин", "Отхаркивающее"),
                Note("Фенибут", "Анксиолитик"),
                Note("Валерианы настойка"),
                Note("Вода питьевая"),
                Note("Крем детский"),
        )
    )


    fun adding(text: String) {
        launch {
            val list = listLiveData.value!!.toMutableList()
            list.add(0, Note(text))
            listLiveData.postValue(list)
        }
    }


}

class Note(
    val title: String,
    val group: String? = null
)