package com.example.grouptasker.data.roomDb.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val lastName: String?,
    val email: String
)