package com.example.grouptasker.data.roomDb.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grouptasker.data.roomDb.Entity.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE groupId = :groupId")
    fun getTasksByGroup(groupId: Long): LiveData<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)
}