package com.example.grouptasker.data.models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class TaskStatus(
    val id: Long? = null,
    val task: Task,
    val userId: Long,
    val status: Status = Status.NEW,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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
}