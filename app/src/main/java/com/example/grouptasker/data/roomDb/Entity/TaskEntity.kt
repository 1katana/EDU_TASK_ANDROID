package com.example.grouptasker.data.roomDb.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.grouptasker.data.models.Task
import java.time.LocalDateTime
import java.util.Date

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["creatorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["groupId"]), Index(value = ["creatorId"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val description: String?,
    val deadline: LocalDateTime,
    val groupId: Long,
    val creatorId: Long,
    val createdAt: LocalDateTime
)

fun TaskEntity.toTask(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        deadline = this.deadline,
        groupId = this.groupId,
        creatorId = this.creatorId,
        createdAt = this.createdAt
    )
}