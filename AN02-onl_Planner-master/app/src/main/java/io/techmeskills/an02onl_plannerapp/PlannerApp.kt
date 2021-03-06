package io.techmeskills.an02onl_plannerapp

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import io.techmeskills.an02onl_plannerapp.cloud.IApi
import io.techmeskills.an02onl_plannerapp.database.DatabaseConstructor
import io.techmeskills.an02onl_plannerapp.database.NotesDatabase
import io.techmeskills.an02onl_plannerapp.datastore.AppSettings
import io.techmeskills.an02onl_plannerapp.repositories.*
import io.techmeskills.an02onl_plannerapp.screen.login.LoginViewModel
import io.techmeskills.an02onl_plannerapp.screen.main.MainViewModel
import io.techmeskills.an02onl_plannerapp.screen.edit.EditFragmentViewModel
import io.techmeskills.an02onl_plannerapp.screen.newnote.NewFragmentViewModel
import io.techmeskills.an02onl_plannerapp.screen.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PlannerApp : Application() {

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlannerApp)
            modules(listOf(viewModels, storageModule, repositoryModule, cloudModule, systemModule))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
    }

    @ExperimentalCoroutinesApi
    private val viewModels = module {
        viewModel { MainViewModel(get(), get()) }
        viewModel { NewFragmentViewModel(get(), get()) }
        viewModel { EditFragmentViewModel(get()) }
        viewModel { LoginViewModel(get())}
        viewModel { SettingsViewModel(get()) }
        }

    private val storageModule = module {
        single { DatabaseConstructor.create(get()) }
        factory { get<NotesDatabase>().notesDao() }
        factory { get<NotesDatabase>().usersDao() }
        single { AppSettings(get()) }
    }

    private val repositoryModule = module {
        factory { UsersRepository(get(), get(), get(), get()) }
        factory { NotesRepository(get(), get(), get(), get(), get()) }
        factory { CloudRepository(get(), get(), get()) }
        factory { NotificationRepository(get(), get()) }
        factory { BroadcastRepository(get()) }
    }

    private val cloudModule = module {
        factory { IApi.get() }
    }

    private val systemModule = module {
        factory { get<Context>().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    }
}