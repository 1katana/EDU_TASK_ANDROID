package com.example.grouptasker.data.roomDb.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grouptasker.data.roomDb.Entity.GroupEntity

@Dao
interface GroupDao {
    @Query("SELECT * FROM groups")
    fun getAllGroups(): LiveData<List<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<GroupEntity>)
}