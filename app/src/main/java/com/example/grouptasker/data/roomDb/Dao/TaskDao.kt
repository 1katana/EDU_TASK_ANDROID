package com.example.grouptasker.data.roomDb.Dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.grouptasker.data.models.TaskStatus
import com.example.grouptasker.data.roomDb.Entity.TaskEntity
import com.example.grouptasker.data.roomDb.Entity.TaskStatusEntity
import com.example.grouptasker.data.roomDb.Entity.TaskStatusWithTask
import com.example.grouptasker.data.roomDb.Entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTasksWithStatuses(
        tasks: List<TaskEntity>,
        taskStatusEntities: List<TaskStatusEntity>
    ) {
        insertTasks(tasks)
        insertTaskStatuses(taskStatusEntities)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTaskStatuses(taskStatusEntities: List<TaskStatusEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Update
    suspend fun updateTaskStatuses(taskStatusEntity: TaskStatusEntity)

    @Transaction
    @Query("SELECT * FROM taskStatuses WHERE userId = :id")
    fun getUserTasks(id: Long): Flow<List<TaskStatusWithTask>>

    @Query("SELECT * FROM taskStatuses WHERE taskId = :taskId")
    fun getTask(taskId: Long): TaskStatusWithTask


    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Delete
    suspend fun deleteTaskStatus(taskStatusEntity: TaskStatusEntity)

    @Transaction
    suspend fun deleteTaskWithStatus(taskStatusWithTask: TaskStatusWithTask) {


        deleteTask(taskStatusWithTask.task)

        // Удаляем TaskStatusEntity
        deleteTaskStatus(taskStatusWithTask.taskStatus)
    }

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskEntityById(taskId: Long): TaskEntity?
}