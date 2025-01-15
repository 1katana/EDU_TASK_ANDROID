package com.example.grouptasker.data.roomDb.Dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.grouptasker.data.models.User
import com.example.grouptasker.data.roomDb.Entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Long): Flow<User>

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String): Flow<User>

    @Query("SELECT * FROM users WHERE email LIKE '%' || :email || '%'")
    fun findUsersByEmail(email: String): Flow<List<User>>

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: Long)


    @Update
    suspend fun update(user: UserEntity)



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}
