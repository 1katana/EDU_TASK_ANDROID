package com.example.grouptasker.Repositories

import com.example.grouptasker.data.models.Group
import com.example.grouptasker.data.remoteAPI.GroupApi
import com.example.grouptasker.data.remoteAPI.UserApi
import com.example.grouptasker.data.roomDb.Dao.GroupDao
import com.example.grouptasker.data.roomDb.Dao.UserDao
import com.example.grouptasker.data.roomDb.Entity.toTaskStatus
import com.example.grouptasker.viewModels.AuthStateManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GroupRepository @Inject constructor(
    val groupDao: GroupDao,
    val groupApi: GroupApi,
    val userApi: UserApi,
    val userDao: UserDao,
    val authStateManager: AuthStateManager
) {


    fun getUserGroups(id: Long, forceRefresh: Boolean = false): Flow<List<Group>> = flow {
        if (!forceRefresh) {
            // Fetch from local DB
            val localGroups = groupDao.getUserGroups(id).firstOrNull()
            if (!localGroups.isNullOrEmpty()) {
                emit(localGroups)
            }
        }

        handleApiRequest(
            apiCall = { userApi.getUserGroups(id).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess { remoteGroups ->
                groupDao.insertGroups(remoteGroups.map { it.toEntity() })

//                remoteGroups.forEach {
//                    val d=it.toEntity()
//                    groupDao.insertGroup(d)
//                }


                emit(remoteGroups)

            }.onFailure { error ->
                throw error
            }
        }
    }



    fun createGroup(group: Group) = flow {
        groupDao.insertGroups(listOf(group.toEntity()))
        emit(group)

        fetchCreateGroup(group)
    }

    fun fetchCreateGroup(group: Group) = flow {
        handleApiRequest(
            apiCall = { groupApi.createGroup(group).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess { remoteGroups ->
                groupDao.insertGroups(listOf(remoteGroups.toEntity()))
                emit(remoteGroups)

            }.onFailure { error ->
                throw error
            }
        }
    }

    fun updateGroup(group: Group) = flow {
        groupDao.updateGroup(group.toEntity())
        emit(group)

        fetchUpdateGroup(group)
    }


    fun fetchUpdateGroup(group: Group) = flow {
        handleApiRequest(
            apiCall = { groupApi.updateGroup(group).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess { remoteGroups ->
                groupDao.insertGroups(listOf(remoteGroups.toEntity()))
                emit(remoteGroups)

            }.onFailure { error ->
                throw error
            }
        }
    }

    fun getUsersInGroup(groupId: Long) = flow {
        val localUsers = groupDao.getGroupUsers(groupId).firstOrNull()

        if (!localUsers.isNullOrEmpty()) {
            emit(localUsers)
        }

        handleApiRequest(
            apiCall = { groupApi.getUsersInGroup(groupId).execute() },
            authStateManager = authStateManager
        ).collect { result ->
            result.onSuccess { remoteUsers ->

                val addUsers = remoteUsers.mapNotNull { user ->
                    if (user.id == authStateManager.token.value?.userId) {
                        null
                    } else {
                        user.toEntity()
                    }
                }

                userDao.insertUsers(addUsers)

            }.onFailure { error ->
                throw error
            }
        }


    }


//    suspend fun getGroupByUserIdAndGroupId(userId: Long, groupId: Long, forceRefresh: Boolean = false): Flow<Group> = flow {
//        if (!forceRefresh) {
//            // Fetch from local DB
//            val localGroup = userDao.getGroupByUserIdAndGroupId(userId, groupId).firstOrNull()
//            if (localGroup != null) {
//                emit(localGroup)
//            }
//        }
//
//        // Fetch from API
//        val response = userApi.getGroupByUserIdAndGroupId(userId, groupId).execute()
//        if (response.isSuccessful) {
//            val remoteGroup = response.body()!!
//            groupDao.insertGroups(listOf(remoteGroup))
//            emit(remoteGroup)
//        }
//    }


}