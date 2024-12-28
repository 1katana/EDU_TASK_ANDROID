package com.example.grouptasker.data.remoteAPI

import com.example.grouptasker.data.models.Task
import com.example.grouptasker.data.models.TaskStatus
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {
    @POST("task")
    fun createTask(@Body taskDto: Task): Call<TaskStatus>

    @PUT("task")
    fun updateTask(@Body taskDto: Task): Call<Void>

    @PUT("task/status")
    fun updateTaskStatus(@Body taskStatusDto: TaskStatus): Call<TaskStatus>

    @DELETE("task/{id}")
    fun deleteTask(@Path("id") taskId: Long): Call<Void>

    @GET("task/{taskId}/user/{userId}")
    fun getTaskStatusByTaskAndUser(@Path("taskId") taskId: Long, @Path("userId") userId: Long): Call<TaskStatus>
}