package com.example.grouptasker.data.models

data class Response(
    val userId: Long,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)
