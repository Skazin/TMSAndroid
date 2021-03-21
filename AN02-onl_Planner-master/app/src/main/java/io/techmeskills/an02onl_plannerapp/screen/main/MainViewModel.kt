package io.techmeskills.an02onl_plannerapp.screen.main

import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel

class MainViewModel : CoroutineViewModel() {

    val notes = listOf(
        Note("Амброксол", "Отхаркивающее"),
        Note("Диклофенак", "НПВС"),
        Note("Кетопрофен", "НПВС"),
        Note("Ацетилцистеин", "Отхаркивающее"),
        Note("Фенибут", "Анксиолитик"),
        Note("Валерианы настойка"),
        Note("Вода питьевая"),
        Note("Крем детский"),
    )

}

class Note(
    val title: String,
    val group: String? = null
)