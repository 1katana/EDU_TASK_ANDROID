package com.example.grouptasker.viewModels

import com.example.grouptasker.data.models.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton


sealed class State {
    object Idle : State()
    object Loading : State()
    data class Success(val message: String): State()
    data class Error(val message: String): State()
    object NotAuthenticated : State()
}


@Singleton
class AuthStateManager @Inject constructor() {

    private val _token = MutableStateFlow<Response?>(null)
    val token: StateFlow<Response?> = _token

    private val _isAuthorized = MutableStateFlow<Boolean>(false)
    val isAuthorized:StateFlow<Boolean> = _isAuthorized


    fun setNotAuthorized(){

        _isAuthorized.value=false
    }

    fun updateAccessToken(accessToken: Response){
        _token.value=accessToken
        _isAuthorized.value=true
    }

    fun clearAuthState(){
        _token.value=null
        _isAuthorized.value=false
    }

}