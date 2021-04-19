package io.techmeskills.an02onl_plannerapp

import android.app.Application
import io.techmeskills.an02onl_plannerapp.database.DatabaseConstructor
import io.techmeskills.an02onl_plannerapp.database.NotesDatabase
import io.techmeskills.an02onl_plannerapp.screen.main.MainViewModel
import io.techmeskills.an02onl_plannerapp.screen.newscreen.EditFragmentViewModel
import io.techmeskills.an02onl_plannerapp.screen.newscreen.NewFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PlannerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlannerApp)
            modules(listOf(viewModels, storageModule))
        }
    }

    private val viewModels = module {
        viewModel { MainViewModel(get()) }
        viewModel { NewFragmentViewModel(get()) }
        viewModel { EditFragmentViewModel(get()) }
    }

    private val storageModule = module {
        single { DatabaseConstructor.create(get()) }  //создаем синглтон базы данных
        factory { get<NotesDatabase>().notesDao() } //предоставляем доступ для конкретной Dao (в нашем случае NotesDao)
    }
}