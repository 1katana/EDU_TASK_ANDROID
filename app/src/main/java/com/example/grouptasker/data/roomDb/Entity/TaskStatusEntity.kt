package com.example.grouptasker.data.roomDb.Entity

import androidx.room.Entity

@Entity(
    tableName = "task_statuses",
    primaryKeys = ["taskId", "userId"]
)
data class TaskStatusEntity(
    val taskId: Long,
    val userId: Long,
    val status: String, // 'NEW', 'IN_PROGRESS', 'DONE'
    val updatedAt: String // ISO-8601 timestamp
)