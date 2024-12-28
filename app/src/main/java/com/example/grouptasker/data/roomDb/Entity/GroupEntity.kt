package com.example.grouptasker.data.roomDb.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val creatorId: Long,
    val createdAt: String // ISO-8601 timestamp
)