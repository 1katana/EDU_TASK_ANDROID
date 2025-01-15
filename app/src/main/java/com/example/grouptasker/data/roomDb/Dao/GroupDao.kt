package com.example.grouptasker.data.roomDb.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.grouptasker.data.models.Group
import com.example.grouptasker.data.models.TaskStatus
import com.example.grouptasker.data.models.User
import com.example.grouptasker.data.roomDb.Entity.GroupEntity
import com.example.grouptasker.data.roomDb.Entity.GroupUsersEntity
import com.example.grouptasker.data.roomDb.Entity.TaskStatusWithTask
import com.example.grouptasker.data.roomDb.Entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("SELECT * FROM groups WHERE id = :id")
    fun getGroupById(id: Long): Flow<Group>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroups(groups: List<GroupEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroup(group: GroupEntity)

    @Update
    suspend fun updateGroup(group: GroupEntity)

    @Query("DELETE FROM groups WHERE id = :id")
    suspend fun deleteGroup(id: Long)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsersGroups(users: List<UserEntity>, groupId: Long) {
        users.forEach { user ->
            insertGroupUser(GroupUsersEntity(groupId = groupId, userId = user.id))
        }
    }

    @Transaction
    @Query("SELECT * FROM groups INNER JOIN groupUsers ON groups.id = groupUsers.groupId WHERE groupUsers.userId = :userId")
    fun getUserGroups(userId: Long): Flow<List<Group>>

    @Transaction
    @Query(
        """
    SELECT users.* 
    FROM users
    INNER JOIN groupUsers ON users.id = groupUsers.userId
    INNER JOIN groups ON groups.id = groupUsers.groupId
    WHERE groups.id = :groupId
"""
    )
    fun getGroupUsers(groupId: Long): Flow<List<User>>

    @Transaction
    @Query("SELECT * FROM groups INNER JOIN groupUsers ON groups.id = groupUsers.groupId WHERE groupUsers.userId = :userId AND groupUsers.groupId = :groupId ")
    fun getGroupByUserIdAndGroupId(userId: Long, groupId: Long): Flow<Group>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroupUser(groupUser: GroupUsersEntity)


    @Query("SELECT ts.* FROM taskStatuses ts INNER JOIN tasks t ON ts.taskId = t.id WHERE t.groupId = :id")
    fun getTasksByGroups(id: Long): Flow<TaskStatusWithTask>
}