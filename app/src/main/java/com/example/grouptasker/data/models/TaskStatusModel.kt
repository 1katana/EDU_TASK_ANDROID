package com.example.grouptasker.data.models

import com.example.grouptasker.data.roomDb.Entity.TaskStatusEntity
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class TaskStatus(
    val id: Long? = null,
    val task: Task,
    val userId: Long,
    val status: Status = Status.NEW,

    val updatedAt: LocalDateTime? = null
) {
    fun isValidForCreate(): Boolean {
        return task.isValidForCreate() &&
                userId > 0
    }

    fun isValidForUpdate(): Boolean {
        return id != null &&
                task.isValidForUpdate() &&
                userId > 0
    }

    fun toEntity(): TaskStatusEntity {
        return TaskStatusEntity(
            id = this.id!!,
            taskId = this.task.id!!,
            userId = this.userId,
            status = this.status.name,
            updatedAt = this.updatedAt!!
        )
    }
}