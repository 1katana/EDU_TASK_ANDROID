package com.example.grouptasker.Repositories

import com.example.grouptasker.viewModels.AuthStateManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

fun <T> handleApiRequest(
    apiCall: suspend () -> Response<T>,
    handleUnauthorized: (suspend () -> Unit)={},
    handleForbidden: suspend () -> Unit = {},
    authStateManager: AuthStateManager

): Flow<Result<T>> = flow {
    try {
        val response = apiCall()
        when {
            response.isSuccessful -> {
                response.body()?.let {
                    emit(Result.success(it))
                } ?: emit(Result.failure(Exception("Empty response body")))
            }
            response.code() == 401 -> {
                handleUnauthorized()

                authStateManager.setNotAuthorized()
                emit(Result.failure(Exception("Unauthorized")))
            }
            response.code() == 403 -> {
                handleForbidden()
                emit(Result.failure(Exception("Forbidden")))
            }
            else -> {
                emit(Result.failure(Exception("HTTP Error: ${response.code()} ${response.message()}")))
            }
        }
    } catch (e: Exception) {
        emit(Result.failure(e))
    }
}


