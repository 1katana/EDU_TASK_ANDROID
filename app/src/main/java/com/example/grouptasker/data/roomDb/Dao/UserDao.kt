package com.example.grouptasker.data.roomDb.Dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grouptasker.data.roomDb.Entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUserById(): Flow<UserEntity>

    fun getUserByEmail(): Flow<UserEntity>

    fun FindUsersByEmail(): Flow<List<UserEntity>>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}