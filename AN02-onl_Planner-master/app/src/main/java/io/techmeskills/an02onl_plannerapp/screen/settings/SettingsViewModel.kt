package io.techmeskills.an02onl_plannerapp.screen.settings

import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.repositories.UsersRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class SettingsViewModel(private val usersRepository: UsersRepository) : CoroutineViewModel() {

    val currentUserNameLiveData = usersRepository.getCurrentUserNameFlow().asLiveData()

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