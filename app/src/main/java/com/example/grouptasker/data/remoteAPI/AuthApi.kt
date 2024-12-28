package com.example.grouptasker.data.remoteAPI

import com.example.grouptasker.data.models.Request
import com.example.grouptasker.data.models.Response
import com.example.grouptasker.data.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST



interface AuthApi {
    @POST("auth/login")
    fun login(@Body loginRequest: Request): Call<Response>

    @POST("auth/register")
    fun register(@Body userDto: User): Call<User>

    @POST("auth/refresh")
    fun refresh(@Body refreshToken: String): Call<Response>
}