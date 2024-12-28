package com.example.grouptasker.data.roomDb.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grouptasker.data.roomDb.Entity.TaskStatusEntity

@Dao
interface TaskStatusDao {
    @Query("SELECT * FROM task_statuses WHERE userId = :userId")
    fun getStatusesByUser(userId: Long): LiveData<List<TaskStatusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatuses(statuses: List<TaskStatusEntity>)
}