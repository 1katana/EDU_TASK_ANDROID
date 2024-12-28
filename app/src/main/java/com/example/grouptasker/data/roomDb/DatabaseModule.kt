package com.example.grouptasker.data.roomDb

import android.content.Context
import androidx.room.Room
import com.example.grouptasker.Repositories.AuthRepository
import com.example.grouptasker.data.remoteAPI.AuthApi
import com.example.grouptasker.data.roomDb.Dao.AuthDao
import com.example.grouptasker.data.roomDb.Dao.GroupDao
import com.example.grouptasker.data.roomDb.Dao.TaskDao
import com.example.grouptasker.data.roomDb.Dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideAuthDao(database: AppDatabase): AuthDao = database.authDao()

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideGroupDao(database: AppDatabase): GroupDao = database.groupDao()

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()

    // Provide repositories
    @Provides
    @Singleton
    fun provideAuthRepository(authDao: AuthDao, authApi: AuthApi): AuthRepository {
        return AuthRepository(authDao, authApi)
    }

//    @Provides
//    @Singleton
//    fun provideUserRepository(userDao: UserDao, userApi: UserApi): UserRepository {
//        return UserRepository(userDao, userApi)
//    }
//
//    @Provides
//    @Singleton
//    fun provideGroupRepository(groupDao: GroupDao, groupApi: GroupApi): GroupRepository {
//        return GroupRepository(groupDao, groupApi)
//    }
//
//    @Provides
//    @Singleton
//    fun provideTaskRepository(taskDao: TaskDao, taskApi: TaskApi): TaskRepository {
//        return TaskRepository(taskDao, taskApi)
//    }
}