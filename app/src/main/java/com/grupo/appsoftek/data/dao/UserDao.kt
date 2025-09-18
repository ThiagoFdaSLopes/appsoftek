package com.grupo.appsoftek.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.grupo.appsoftek.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM users WHERE password = :password LIMIT 1")
    suspend fun getUserByPassword(password: String): User?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    @Query("SELECT * FROM users ORDER BY createdAt ASC LIMIT 1")
    suspend fun getFirstUser(): User?

    @Query("SELECT * FROM users ORDER BY lastLoginAt DESC LIMIT 1")
    suspend fun getLastLoggedUser(): User?

    @Query("UPDATE users SET lastLoginAt = :timestamp WHERE id = :userId")
    suspend fun updateLastLogin(userId: String, timestamp: Long)

    @Query("SELECT COUNT(*) > 0 FROM users")
    fun hasUsers(): Flow<Boolean>
}

