package com.example.grouptasker.Repositories

import com.example.grouptasker.data.models.Group
import com.example.grouptasker.data.models.Task
import com.example.grouptasker.data.models.TaskStatus
import com.example.grouptasker.data.remoteAPI.AuthApi
import com.example.grouptasker.data.remoteAPI.GroupApi
import com.example.grouptasker.data.remoteAPI.TaskApi
import com.example.grouptasker.data.remoteAPI.UserApi
import com.example.grouptasker.data.roomDb.Dao.AuthDao
import com.example.grouptasker.data.roomDb.Dao.TaskDao
import com.example.grouptasker.data.roomDb.Entity.TaskStatusEntity
import com.example.grouptasker.data.roomDb.Entity.toTaskStatus
import com.example.grouptasker.viewModels.AuthStateManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApi: TaskApi,
    private val userApi: UserApi,
    private val groupApi: GroupApi,
    private val authStateManager: AuthStateManager
) {


    private suspend fun fetchCreateTask(task: Task) {
        // Синхронизация с сервером
        handleApiRequest(
            apiCall = { taskApi.createTask(task).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess { remoteTaskStatus ->
                taskDao.updateTaskStatuses(remoteTaskStatus.toEntity())
            }.onFailure { error ->
                throw error
            }
        }
    }

    fun getTasksByGroupId(groupId: Long, userId: Long) = flow {

        handleApiRequest(
            apiCall = { groupApi.getTasksByGroupId(groupId, userId).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess { remoteTasks ->
                taskDao.insertTasksWithStatuses(
                    remoteTasks.map { it.task.toEntity() },
                    remoteTasks.map { it.toEntity() }
                )
                emit(remoteTasks)
            }.onFailure { error ->
                throw error
            }
        }
    }

    // Создание задачи
    fun createTask(task: Task): Flow<TaskStatus> = flow {
        val taskStatus = TaskStatus(
            task = task,
            userId = task.creatorId,
            updatedAt = LocalDateTime.now()
        )

        // Локальное сохранение
        taskDao.insertTasksWithStatuses(
            listOf(task.toEntity()),
            listOf(taskStatus.toEntity())
        )
        emit(taskStatus)

        fetchCreateTask(task)

    }

    // Получение задач пользователя
    fun getUserTasks(userId: Long, forceRefresh: Boolean = false): Flow<List<TaskStatus>> = flow {
        if (!forceRefresh) {
            val localTasks = taskDao.getUserTasks(userId).firstOrNull()
            if (!localTasks.isNullOrEmpty()) {
                emit(localTasks.map { it.toTaskStatus() })
            }
        }

        // Загрузка с сервера
        handleApiRequest(
            apiCall = { userApi.getUserTasks(userId).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess { remoteTasks ->
                taskDao.insertTasksWithStatuses(
                    remoteTasks.map { it.task.toEntity() },
                    remoteTasks.map { it.toEntity() }
                )
                emit(remoteTasks)
            }.onFailure { error ->
                throw error
            }
        }
    }

    // Обновление задачи
    fun updateTask(task: Task): Flow<Unit> = flow {
        // Локальное обновление
        taskDao.updateTask(task.toEntity())
        emit(Unit)

        // Синхронизация с сервером
        handleApiRequest(
            apiCall = { taskApi.updateTask(task).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onFailure { error ->
                throw error
            }
        }
    }

    // Удаление задачи
    fun deleteTask(taskId: Long): Flow<Unit> = flow {

        val taskStatusEntity = taskDao.getTask(taskId)

        taskDao.deleteTaskWithStatus(taskStatusEntity)


        fetchDeleteTask(taskId)
    }


    suspend fun fetchDeleteTask(taskId: Long) {
        handleApiRequest(
            apiCall = { taskApi.deleteTask(taskId).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onFailure { error ->
                throw error
            }
        }
    }


    // Обновление статуса задачи
    fun updateTaskStatus(taskStatus: TaskStatus): Flow<TaskStatus> = flow {
        taskDao.updateTaskStatuses(taskStatus.toEntity())
        emit(taskStatus)

        fetchUpdateTaskStatus(taskStatus)
    }


    fun fetchUpdateTaskStatus(taskStatus: TaskStatus): Flow<TaskStatus> = flow {

        handleApiRequest(
            apiCall = { taskApi.updateTaskStatus(taskStatus).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess { updatedTaskStatus ->
                taskDao.updateTaskStatuses(updatedTaskStatus.toEntity())
                emit(updatedTaskStatus)
            }.onFailure { error ->
                throw error
            }
        }
    }


}