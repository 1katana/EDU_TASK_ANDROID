package com.example.grouptasker.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grouptasker.Repositories.AuthRepository
import com.example.grouptasker.data.models.Request
import com.example.grouptasker.data.models.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val response: Response) : AuthState()
    data class Error(val message: String) : AuthState()
    object LoggedOut : AuthState()
}


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authStateManager: AuthStateManager
) : ViewModel() {


    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {

        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            val token = authRepository.getToken()
            if (token != null) {
                authStateManager.updateAccessToken(token)
                _authState.value=AuthState.Success(token)
            }
            else{
                _authState.value = AuthState.Idle
            }
        }
    }




    // Выполняем вход в систему
    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            try {
                val loginRequest = Request(email = email, password = password)
                authRepository.login(loginRequest).collect { response ->

                    authStateManager.updateAccessToken(response)
                    _authState.value = AuthState.Success(response)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    // Обновляем токены
    fun refreshTokens() {
        viewModelScope.launch(Dispatchers.IO) {
//            _authState.value = AuthState.Loading
            try {
                val refreshToken = authRepository.getRefreshToken()
                    ?: throw Exception("No refresh token available")
                authRepository.refreshTokens(refreshToken).collect { response ->
                    authStateManager.updateAccessToken(response)
                    _authState.value = AuthState.Success(response)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Token refresh failed")
            }
        }
    }

    // Очистка токенов
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.clearTokens()
            authStateManager.clearAuthState()
            _authState.value = AuthState.LoggedOut
        }
    }
}
