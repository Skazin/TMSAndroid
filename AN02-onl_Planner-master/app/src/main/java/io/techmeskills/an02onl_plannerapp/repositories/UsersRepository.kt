package io.techmeskills.an02onl_plannerapp.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import io.techmeskills.an02onl_plannerapp.database.dao.UsersDao
import io.techmeskills.an02onl_plannerapp.datastore.AppSettings
import io.techmeskills.an02onl_plannerapp.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UsersRepository(
    context: Context,
    private val usersDao: UsersDao,
    private val appSettings: AppSettings,
    private val broadcastRepository: BroadcastRepository
    ) {

    val userNames = usersDao.getAllUserNames()

    suspend fun login(userName: String) {
        withContext(Dispatchers.IO) {
            if(checkUserExists(userName).not()){
                usersDao.newUser(User(name = userName))
                appSettings.setUserName(userName)
            } else {
                appSettings.setUserName(userName)
            }
        }
        broadcastRepository.broadcastNotesUpdate()
    }

    suspend fun userName(): String {
        return withContext(Dispatchers.IO) {
            appSettings.userName()
        }
    }

    suspend fun updateUser(oldName: String, newName: String) {
        withContext(Dispatchers.IO) {
            appSettings.setUserName(newName)
            usersDao.updateUserName(oldName, newName)
        }
    }

    private suspend fun checkUserExists(userName: String): Boolean {
        return withContext(Dispatchers.IO) {
            usersDao.getUsersCount(userName) > 0
        }
    }

    fun getCurrentUserFlow(): Flow<User> = appSettings.userNameFlow().map {
        User(it)
    }

    @ExperimentalCoroutinesApi
    fun getCurrentUserNameFlow(): Flow<String> = appSettings.userNameFlow().flatMapLatest {
        appSettings.userNameFlow()
    }

    fun checkUserLoggedIn(): Flow<Boolean> =
            appSettings.userNameFlow().map { it.isNotEmpty() }.flowOn(Dispatchers.IO)

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            appSettings.setUserName("")
        }
        broadcastRepository.broadcastNotesUpdate()
    }

    suspend fun deleteCurrent() {
        withContext(Dispatchers.IO) {
            usersDao.deleteUser(User(appSettings.userName()))
            logout()
        }
    }

    @SuppressLint("HardwareIds")
    val phoneId: String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}