package com.example.grouptasker.data.roomDb.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String?,
    val deadline: String, // ISO-8601 timestamp
    val groupId: Long,
    val creatorId: Long
)