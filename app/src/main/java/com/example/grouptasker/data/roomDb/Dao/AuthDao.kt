package com.example.grouptasker.data.roomDb.Dao

import android.media.session.MediaSession
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grouptasker.data.models.Response
import com.example.grouptasker.data.roomDb.Entity.TokenEntity

@Dao
interface AuthDao {
    @Query("SELECT accessToken FROM tokens LIMIT 1")
    suspend fun getAccessToken(): String?

    @Query("SELECT refreshToken FROM tokens LIMIT 1")
    suspend fun getRefreshToken(): String?

    @Query("SELECT userId,email,accessToken,refreshToken FROM tokens LIMIT 1")
    suspend fun getToken(): Response?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTokens(tokens: TokenEntity)

    @Query("DELETE FROM tokens")
    suspend fun clearTokens()
}