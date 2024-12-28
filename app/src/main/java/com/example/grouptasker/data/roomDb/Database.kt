package com.example.grouptasker.data.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.grouptasker.data.roomDb.Dao.AuthDao
import com.example.grouptasker.data.roomDb.Dao.GroupDao
import com.example.grouptasker.data.roomDb.Dao.TaskDao
import com.example.grouptasker.data.roomDb.Dao.TaskStatusDao
import com.example.grouptasker.data.roomDb.Dao.TokenDao
import com.example.grouptasker.data.roomDb.Dao.UserDao
import com.example.grouptasker.data.roomDb.Entity.GroupEntity
import com.example.grouptasker.data.roomDb.Entity.TaskEntity
import com.example.grouptasker.data.roomDb.Entity.TaskStatusEntity
import com.example.grouptasker.data.roomDb.Entity.TokenEntity
import com.example.grouptasker.data.roomDb.Entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        GroupEntity::class,
        TaskEntity::class,
        TaskStatusEntity::class,
        TokenEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun taskDao(): TaskDao
    abstract fun taskStatusDao(): TaskStatusDao
    abstract fun tokenDao(): TokenDao
    abstract fun authDao(): AuthDao
}