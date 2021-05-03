package io.techmeskills.an02onl_plannerapp

import android.app.Application
import io.techmeskills.an02onl_plannerapp.cloud.IApi
import io.techmeskills.an02onl_plannerapp.database.DatabaseConstructor
import io.techmeskills.an02onl_plannerapp.database.NotesDatabase
import io.techmeskills.an02onl_plannerapp.datastore.AppSettings
import io.techmeskills.an02onl_plannerapp.repositories.CloudRepository
import io.techmeskills.an02onl_plannerapp.repositories.NotesRepository
import io.techmeskills.an02onl_plannerapp.repositories.UsersRepository
import io.techmeskills.an02onl_plannerapp.screen.loginscreen.LoginViewModel
import io.techmeskills.an02onl_plannerapp.screen.main.MainViewModel
import io.techmeskills.an02onl_plannerapp.screen.newscreen.EditFragmentViewModel
import io.techmeskills.an02onl_plannerapp.screen.newscreen.NewFragmentViewModel
import io.techmeskills.an02onl_plannerapp.screen.usersettings.UserSettingsFragment
import io.techmeskills.an02onl_plannerapp.screen.usersettings.UserSettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PlannerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlannerApp)
            modules(listOf(viewModels, storageModule, repositoryModule, cloudModule))
        }
    }

    private val viewModels = module {
        viewModel { MainViewModel(get(), get()) }
        viewModel { NewFragmentViewModel(get()) }
        viewModel { EditFragmentViewModel(get()) }
        viewModel { LoginViewModel(get())}
        viewModel { UserSettingsViewModel(get(), get()) }
        }

    private val storageModule = module {
        single { DatabaseConstructor.create(get()) }
        factory { get<NotesDatabase>().notesDao() }
        factory { get<NotesDatabase>().usersDao() }
        single { AppSettings(get()) }
    }

    private val repositoryModule = module {
        factory { UsersRepository(get(), get(), get()) }
        factory { NotesRepository(get(), get(), get()) }
        factory { CloudRepository(get(), get(), get()) }
    }

    private val cloudModule = module {
        factory { IApi.get() }
    }
}