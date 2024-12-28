package com.example.grouptasker.data.roomDb.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tokens")
data class TokenEntity(
    @PrimaryKey val id: Int = 0, // Одна запись (можно использовать фиксированный ID)
    val accessToken: String,
    val refreshToken: String,
    val userId: Long,
    val email: String
)