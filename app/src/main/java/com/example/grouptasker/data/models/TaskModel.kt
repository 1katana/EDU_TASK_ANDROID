package com.example.grouptasker.data.models

import com.example.grouptasker.data.roomDb.Entity.TaskEntity
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.Date

data class Task(
    val id: Long? = null,
    val title: String,
    val description: String?,


    val deadline: LocalDateTime,
    val groupId: Long,
    val creatorId: Long,


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


    fun toEntity(): TaskEntity {
        return TaskEntity(
            id = this.id!!,
            title = this.title,
            description = this.description,
            deadline = this.deadline,
            groupId = this.groupId,
            creatorId = this.creatorId,
            createdAt = this.createdAt!!
        )
    }
}

enum class Status {
    @SerializedName("NEW")
    NEW,
    @SerializedName("IN_PROGRESS")
    IN_PROGRESS,
    @SerializedName("DONE")
    DONE
}