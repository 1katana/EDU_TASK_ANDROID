package com.example.grouptasker.data.models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Task(
    val id: Long? = null,
    val title: String,
    val description: String?,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val deadline: LocalDateTime,
    val groupId: Long,
    val creatorId: Long,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val createdAt: LocalDateTime? = null
) {
    fun isValidForCreate(): Boolean {
        return title.isNotBlank() && title.length <= 255 &&
                (description == null || description.length <= 1000) &&
                groupId > 0 &&
                creatorId > 0
    }

    fun isValidForUpdate(): Boolean {
        return id != null &&
                title.isNotBlank() && title.length <= 255 &&
                (description == null || description.length <= 1000) &&
                groupId > 0 &&
                creatorId > 0
    }
}

enum class Status {
    NEW, IN_PROGRESS, COMPLETED
}