package com.example.grouptasker.data.remoteAPI

import com.example.grouptasker.data.models.Group
import com.example.grouptasker.data.models.TaskStatus
import com.example.grouptasker.data.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @GET("users/{id}")
    fun getById(@Path("id") id: Long): Call<User>

    @DELETE("users/{id}")
    fun deleteById(@Path("id") id: Long): Call<Void>

    @GET("users/{id}/groups")
    fun getUserGroups(@Path("id") id: Long): Call<List<Group>>

    @GET("users/{id}/group/{groupId}")
    fun getGroupByUserIdAndGroupId(@Path("id") id: Long, @Path("groupId") groupId: Long): Call<Group>

    @PUT("users")
    fun update(@Body user: User): Call<User>

    @GET("users/{id}/tasks")
    fun getUserTasks(@Path("id") id: Long): Call<List<TaskStatus>>

    @GET("users/find/{snippetEmail}")
    fun findUserBySnippetEmail(@Path("snippetEmail")  snippetEmail:String): Call<List<User>>
}