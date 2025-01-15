package com.example.grouptasker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouptasker.Repositories.TaskRepository
import com.example.grouptasker.Repositories.UserRepository
import com.example.grouptasker.data.models.Task
import com.example.grouptasker.data.models.TaskStatus
import com.example.grouptasker.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val authStateManager: AuthStateManager
) : ViewModel() {

    val uiState = MutableStateFlow<State>(State.Idle)

    private val _taskState = MutableStateFlow<MutableList<TaskStatus>>(mutableListOf())
    val taskState: StateFlow<MutableList<TaskStatus>> = _taskState


//    init {
//
//        if (!authStateManager.isAuthorized.value) {
//            _taskState.value = State.NotAuthenticated
//        }
//    }

    fun createTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = State.Loading

            try {
                taskRepository.createTask(task).collect { taskStatus ->
                    uiState.value = State.Success(message = "Succes")

                    _taskState.value.add(taskStatus)

                }
            } catch (e: Exception) {
                uiState.value = State.Error(message = e.localizedMessage)
            }
        }
    }

    fun getUserTasks(forceRefresh: Boolean = false) {

        viewModelScope.launch(Dispatchers.IO) {

            uiState.value = State.Loading
            try {
                authStateManager.token.value?.let {
                    taskRepository.getUserTasks(6, forceRefresh).collect { tasks ->
                        uiState.value = State.Success(message = "Succes")

                        _taskState.value = tasks.toMutableList()


                    }
                }
            } catch (e: Exception) {
                uiState.value = State.Error(message = e.localizedMessage)
            }
        }

    }


}