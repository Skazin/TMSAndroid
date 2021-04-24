package io.techmeskills.an02onl_plannerapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.techmeskills.an02onl_plannerapp.models.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UsersDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun newUser(user: User): Long

    @Query("SELECT COUNT(*) FROM users WHERE name == :userName")
    abstract fun getUsersCount(userName: String): Int

    @Query("SELECT id FROM users WHERE name == :userName")
    abstract fun getUserId(userName: String): Long

    @Query("SELECT * FROM users WHERE id == :userId")
    abstract fun getById(userId: Long): Flow<User>

    @Query("SELECT COUNT(*) FROM users WHERE name == :userName")
    abstract fun getUsersCountFlow(userName: String): Flow<Int>

    @Query("SELECT name FROM users")
    abstract fun getAllUserNames(): Flow<List<String>>

    @Query("SELECT name FROM users WHERE id == :userId")
    abstract fun getUserNameById(userId: Long): Flow<String>
}