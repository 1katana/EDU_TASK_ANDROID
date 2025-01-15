package com.example.grouptasker.data.roomDb.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime



@Entity(
    tableName = "groups",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["creatorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["creatorId"])]
)
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val creatorId: Long,
    val createdAt: LocalDateTime
){

}