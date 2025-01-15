package com.example.grouptasker.data.roomDb.Entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.grouptasker.data.models.Status
import com.example.grouptasker.data.models.TaskStatus

data class TaskStatusWithTask(
    @Embedded val taskStatus: TaskStatusEntity, // Данные из таблицы taskStatuses
    @Relation(
        parentColumn = "taskId", // Колонка в TaskStatusEntity
        entityColumn = "id",    // Колонка в TaskEntity
        entity = TaskEntity::class
    )
    val task: TaskEntity // Данные из таблицы tasks
)

fun TaskStatusWithTask.toTaskStatus(): TaskStatus {
    return TaskStatus(
        id = this.taskStatus.id,
        task = this.task.toTask(), // Преобразование TaskEntity в Task (требуется метод toTask)
        userId = this.taskStatus.userId,
        status = Status.valueOf(this.taskStatus.status),
        updatedAt = this.taskStatus.updatedAt
    )


}
