package com.example.grouptasker.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.grouptasker.Repositories.AuthRepository
import com.example.grouptasker.data.models.Request
import com.example.grouptasker.data.models.Response
import com.example.grouptasker.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject



sealed class AuthState {
    object Loading : AuthState()
    object LoggedIn : AuthState()
    object LoggedOut : AuthState()
    data class Error(val message: String) : AuthState()
}



@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authStateManager: AuthStateManager
) : ViewModel() {


    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState: StateFlow<AuthState> = _authState

    init {
        viewModelScope.launch(Dispatchers.IO)  {
            // Создаем задачу для проверки токена
            val tokenCheckJob = launch{
                _authState.value = AuthState.Loading
                val token = authRepository.getToken()
                if (token != null) {
                    authStateManager.updateAccessToken(token)
                    _authState.value = AuthState.LoggedIn
                } else {
                    _authState.value = AuthState.LoggedOut
                }
            }

            // Ждем завершения проверки токена
            tokenCheckJob.join()


            authStateManager.isAuthorized.collect { isAuthorized ->
                if (!isAuthorized) {
                    handleAuthorizationState()
                }
            }
        }
    }


    private suspend fun handleAuthorizationState() {
        try {
            // Пытаемся обновить токен
            refreshTokens()
        } catch (e: Exception) {
            // Если не удалось обновить токен, переводим в состояние LoggedOut
            _authState.value = AuthState.LoggedOut
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
                    _authState.value = AuthState.LoggedIn
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
                    _authState.value = AuthState.LoggedIn
                }
            } catch (e: Exception) {
                authStateManager.clearAuthState()
                _authState.value = AuthState.Error(e.message ?: "Token refresh failed")
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            try {
                // Выполняем регистрацию
                val result = authRepository.register(user).firstOrNull()

                if (result != null && result.isSuccess) {
                    // Если регистрация успешна, извлекаем пользователя
                    val registeredUser = result.getOrNull()
                    if (registeredUser != null) {
                        // Вызываем login с зарегистрированным пользователем
                        login(registeredUser.email, user.password!!)
                    } else {
                        // Обработка ошибки, если пользователь оказался null
                        _authState.value = AuthState.Error("Failed to retrieve user after registration.")
                    }
                } else {
                    // Обработка ошибки, если результат неуспешен
                    val errorMessage = result?.exceptionOrNull()?.message ?: "Unknown error during registration."
                    _authState.value = AuthState.Error(errorMessage)
                }
            } catch (e: Exception) {
                // Обрабатываем любые исключения
                _authState.value = AuthState.Error(e.message ?: "Register failed.")
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
