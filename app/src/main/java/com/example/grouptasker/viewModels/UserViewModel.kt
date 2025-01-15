package com.example.grouptasker.viewModels


import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.SearchSnippets
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouptasker.Repositories.UserRepository
import com.example.grouptasker.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    authStateManager: AuthStateManager
) : ViewModel() {

    val uiState = MutableStateFlow<State>(State.Idle)

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState

    private val _findState = MutableStateFlow<List<User>>(listOf())
    val findState: StateFlow<List<User>> = _findState



    init {

        viewModelScope.launch(Dispatchers.IO){
            // Подписываемся на изменения токена
            authStateManager.token.collect { token ->
                if (token != null) {
                    fetchUserById(token.userId, true)
                } else {
                    _userState.value = null
                }
            }
        }

    }



    fun fetchUserById(id: Long, forceRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = State.Loading
            try {
                userRepository.getUserById(id, forceRefresh).collect { fetchedUser ->
                    uiState.value =
                        State.Success(message = "User fetched successfully")
                    _userState.value=fetchedUser
                }
            } catch (e: Exception) {
                uiState.value = State.Error(message = e.localizedMessage)
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = State.Loading
            try {
                userRepository.updateUser(user)
                // After successful update, fetch updated user data
                fetchUserById(user.id!!, forceRefresh = true)
            } catch (e: Exception) {
                uiState.value = State.Error(message = e.localizedMessage)
            }
        }
    }

    fun deleteUserById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = State.Loading
            try {
                userRepository.deleteUserById(id)
                uiState.value =
                    State.Success(message = "User deleted successfully")
                _userState.value=null
            } catch (e: Exception) {
                uiState.value = State.Error(message = e.localizedMessage)
            }
        }
    }

    fun findUserBySnippetEmail(snippetEmail: String) {


        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = State.Loading

            try {
                userRepository.findUserBySnippetEmail(snippetEmail).collect{users->
                    uiState.value = State.Success(message = "Succes")
                    _findState.value=users
                }

            }
            catch (e:Exception){
                uiState.value = State.Error(message = e.localizedMessage)
            }
        }
    }


}


//    fun fetchUserGroups(id: Long, forceRefresh: Boolean = false) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _loading.value = true
//                userRepository.getUserGroups(id, forceRefresh).collect { fetchedGroups ->
//                    _groups.value = fetchedGroups
//                }
//            } catch (e: Exception) {
//                _error.value = e.localizedMessage
//            } finally {
//                _loading.value = false
//            }
//        }
//    }




