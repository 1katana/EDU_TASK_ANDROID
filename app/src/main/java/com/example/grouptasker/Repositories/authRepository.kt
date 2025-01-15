package com.example.grouptasker.Repositories

import android.util.Log
import com.example.grouptasker.data.models.Request
import com.example.grouptasker.data.models.Response
import com.example.grouptasker.data.models.User
import com.example.grouptasker.data.remoteAPI.AuthApi
import com.example.grouptasker.data.roomDb.Dao.AuthDao
import com.example.grouptasker.data.roomDb.Entity.TokenEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authDao: AuthDao,
    private val authApi: AuthApi
) {


    fun register(user: User):Flow<Result<User>> = flow{
        val response = authApi.register(user).execute()
        when {
            response.code() == 401 -> {
                emit(Result.failure(Exception("Such an email exists")))
            }
            response.isSuccessful -> {
                response.body()?.let {

                    login(Request(user.email,user.password!!))

                    emit(Result.success(it))
                } ?: emit(Result.failure(Exception("Empty response body")))
            }
            else ->{
                emit(Result.failure(Exception("HTTP Error: ${response.code()} ${response.message()}")))
            }

        }



    }




    fun login(loginRequest: Request): Flow<Response> = flow {
        val response = authApi.login(loginRequest).execute()
        Log.d("my", response.body().toString())
        if (response.isSuccessful) {
            response.body()?.let {
                authDao.saveTokens(
                    TokenEntity(
                        accessToken = it.accessToken,
                        refreshToken = it.refreshToken,
                        userId = it.userId,
                        email = it.email
                    )
                )
                emit(it)
            }
        } else {
            throw Exception("Login failed")
        }
    }

    fun refreshTokens(refreshToken: String): Flow<Response> = flow {
        val response = authApi.refresh(refreshToken).execute()
        if (response.isSuccessful) {
            response.body()?.let {
                authDao.saveTokens(
                    TokenEntity(
                        accessToken = it.accessToken,
                        refreshToken = it.refreshToken,
                        userId = it.userId,
                        email = it.email
                    )
                )
                emit(it)
            }
        } else {
            throw Exception("Token refresh failed")
        }
    }

    suspend fun getAccessToken(): String? = authDao.getAccessToken()
    suspend fun getRefreshToken(): String? = authDao.getRefreshToken()
    suspend fun getToken(): Response? = authDao.getToken()

    suspend fun clearTokens() {
        authDao.clearTokens()
    }
}
