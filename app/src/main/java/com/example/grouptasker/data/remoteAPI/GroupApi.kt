package com.example.grouptasker.data.remoteAPI

import com.example.grouptasker.data.models.Group
import com.example.grouptasker.data.models.ResponseEntity
import com.example.grouptasker.data.models.TaskStatus
import com.example.grouptasker.data.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroupApi {
    @POST("group")
    fun createGroup(@Body groupDto: Group): Call<Group>

    @PUT("group")
    fun updateGroup(@Body groupDto: Group): Call<Group>

    @GET("group/{groupId}/user/{userId}")
    fun getTasksByGroupId(@Path("groupId") groupId: Long, @Path("userId") userId: Long): Call<List<TaskStatus>>

    @GET("group/{id}/users")
    fun getUsersInGroup(@Path("id") groupId: Long): Call<List<User>>

    @POST("group/{groupId}/users/{userId}")
    fun addUserToGroup(@Path("groupId") groupId: Long, @Path("userId") userId: Long): Call<ResponseEntity<String>>
}