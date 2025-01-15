package com.example.grouptasker.Repositories

import com.example.grouptasker.data.models.User
import com.example.grouptasker.data.remoteAPI.UserApi
import com.example.grouptasker.data.roomDb.Dao.UserDao
import com.example.grouptasker.viewModels.AuthStateManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi,
    private val authStateManager: AuthStateManager
) {

    fun getUserById(id: Long, forceRefresh: Boolean = false): Flow<User> = flow {
        if (!forceRefresh) {
            // Try fetching user from local DB
            val localUser = userDao.getUserById(id).firstOrNull()
            if (localUser != null) {
                emit(localUser)
            }
        }

        handleApiRequest(
            apiCall = { userApi.getById(id).execute() },
            authStateManager = authStateManager

        ).collect { result ->
            result.onSuccess { remoteUser ->
                userDao.insertUsers(listOf(remoteUser.toEntity()))
                emit(remoteUser)
            }.onFailure { error ->

                throw error
            }
        }
    }


    fun findUserBySnippetEmail(snippetEmail: String): Flow<List<User>> = flow {

        handleApiRequest(
            apiCall = { userApi.findUserBySnippetEmail(snippetEmail).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess { remoteUser ->

                emit(remoteUser)
            }.onFailure { error ->

                throw error
            }
        }
    }

    fun deleteUserById(id: Long) = flow {


        handleApiRequest(
            apiCall = { userApi.deleteById(id).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess {
                userDao.deleteById(id)
                emit(true)
            }.onFailure { error ->

                throw error
            }
        }
    }

    fun updateUser(user: User) = flow {


        handleApiRequest(
            apiCall = { userApi.update(user).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess { remoteUser ->

                userDao.update(user.toEntity())
                emit(remoteUser)
            }.onFailure { error ->

                throw error
            }
        }

    }
}



