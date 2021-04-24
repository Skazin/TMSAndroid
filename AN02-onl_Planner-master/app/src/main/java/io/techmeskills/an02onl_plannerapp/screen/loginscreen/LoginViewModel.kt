package io.techmeskills.an02onl_plannerapp.screen.loginscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.repositories.UsersRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(private val usersRepository: UsersRepository) : CoroutineViewModel() {

    val autoCompleteUsersLiveData = usersRepository.userNames.asLiveData()

    val logged: LiveData<Boolean> = usersRepository.checkUserLoggedIn().asLiveData()

    val errorLiveData = MutableLiveData<String>()

    fun login(user: String){
        launch {
            try {
                if (user.isNotBlank()) {
                    usersRepository.login(user)
                } else {
                    errorLiveData.postValue("Please, enter your name")
                }
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }
}