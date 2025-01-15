package com.example.grouptasker.data.models
import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("id") val userId: Long,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)
