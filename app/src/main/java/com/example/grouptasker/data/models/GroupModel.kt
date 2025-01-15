package com.example.grouptasker.data.models

import com.example.grouptasker.data.roomDb.Entity.GroupEntity
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Group(
    val id: Long? = null,
    val name: String,
    val creatorId: Long,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val createdAt: LocalDateTime? = null
) {
    fun isValidForCreate(): Boolean {
        return name.isNotBlank() && name.length <= 255 &&
                creatorId > 0
    }

    fun isValidForUpdate(): Boolean {
        return id != null &&
                name.isNotBlank() && name.length <= 255 &&
                creatorId > 0
    }



    fun toEntity(): GroupEntity {
        return GroupEntity(id = this.id!!, name = this.name, creatorId = this.creatorId, createdAt = this.createdAt!!)
    }


}