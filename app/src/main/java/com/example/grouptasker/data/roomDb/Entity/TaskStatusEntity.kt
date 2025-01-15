package com.example.grouptasker.data.roomDb.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.grouptasker.data.models.Status
import com.example.grouptasker.data.models.TaskStatus
import java.time.LocalDateTime


@Entity(
    tableName = "taskStatuses",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["taskId"]), Index(value = ["userId"])]
)
data class TaskStatusEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val taskId: Long,
    val userId: Long,
    val status: String = "NEW",
    val updatedAt: LocalDateTime
)

