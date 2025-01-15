package com.example.grouptasker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouptasker.Repositories.GroupRepository
import com.example.grouptasker.data.models.Group
import com.example.grouptasker.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val authStateManager: AuthStateManager
) : ViewModel() {

    val uiState = MutableStateFlow<State>(State.Idle)

    private val _groupState = MutableStateFlow<MutableList<Group>>(mutableListOf())
    val groupState: StateFlow<MutableList<Group>> = _groupState

    private val _groupUserState = MutableStateFlow<MutableList<User>>(mutableListOf())
    val groupUserState: StateFlow<MutableList<User>> = _groupUserState


    fun getUserGroups(forceRefresh: Boolean = false) {

        uiState.value = State.Loading

        viewModelScope.launch(Dispatchers.IO) {

            try {
                authStateManager.token.value?.let {
                    groupRepository.getUserGroups(6, forceRefresh).collect { groups ->
                        _groupState.value = groups.toMutableList()
                        uiState.value = State.Success(message = "Success loading groups")

                    }


                }
            } catch (e: Exception) {
                uiState.value = State.Error(message = e.localizedMessage)
            }

        }
    }

}