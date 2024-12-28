//package com.example.grouptasker.Repositories
//
//import com.example.grouptasker.data.models.User
//import com.example.grouptasker.data.remoteAPI.UserApi
//import com.example.grouptasker.data.roomDb.Dao.UserDao
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//
//
//class UserRepository(
//    private val userApi: UserApi,
//    private val userDao: UserDao
//){
//    fun getById(userId: Long): Flow<User?> = flow {
//        val response = userApi.getById(userId).execute()
//        if (response.isSuccessful) {
//            userDao.insertOrUpdate(user.toEntity())
//            emit(response.body())
//        } else {
//            emit(null)
//        }
//    }
//
//
//}