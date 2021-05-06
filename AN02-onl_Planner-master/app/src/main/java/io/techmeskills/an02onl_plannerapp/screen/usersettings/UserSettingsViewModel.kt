package io.techmeskills.an02onl_plannerapp.screen.usersettings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.models.User
import io.techmeskills.an02onl_plannerapp.repositories.CloudRepository
import io.techmeskills.an02onl_plannerapp.repositories.UsersRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import io.techmeskills.an02onl_plannerapp.support.Result
import kotlinx.coroutines.launch

class UserSettingsViewModel(private val usersRepository: UsersRepository,
                            private val cloudRepository: CloudRepository) : CoroutineViewModel() {

    private val importProgressLiveData = MutableLiveData<Result>()
    private val progressLiveData = MutableLiveData<Boolean>()
    val currentUserNameLiveData = usersRepository.getCurrentUserNameFlow().asLiveData()

    fun exportNotes() = launch {
        val result = cloudRepository.exportNotes()
        progressLiveData.postValue(result)
    }

    fun importNotes() = launch {
        val result = cloudRepository.importNotes()
        importProgressLiveData.postValue(result)
    }

    fun updateUser(newName: String){
        launch {
            usersRepository.updateUser(usersRepository.userName(), newName)
        }
    }

    fun logout() {
        launch {
            usersRepository.logout()
        }
    }

    fun deleteUser() {
        launch {
            usersRepository.deleteCurrent()
        }
    }
}