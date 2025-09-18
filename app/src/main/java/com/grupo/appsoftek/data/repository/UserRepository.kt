package com.grupo.appsoftek.data.repository

import com.grupo.appsoftek.data.dao.UserDao
import com.grupo.appsoftek.data.model.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class UserRepository(private val userDao: UserDao) {

    suspend fun createUser(password: String): User {
        val user = User(
            id = UUID.randomUUID().toString(),
            password = password,
            createdAt = System.currentTimeMillis(),
            lastLoginAt = System.currentTimeMillis()
        )
        userDao.insertUser(user)
        return user
    }

    suspend fun authenticateUser(password: String): User? {
        val user = userDao.getUserByPassword(password)
        if (user != null) {
            userDao.updateLastLogin(user.id, System.currentTimeMillis())
        }
        return user
    }

    suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)
    }

    suspend fun hasUsers(): Boolean {
        return userDao.getUserCount() > 0
    }

    suspend fun getFirstUser(): User? {
        return userDao.getFirstUser()
    }

    suspend fun getLastLoggedUser(): User? {
        return userDao.getLastLoggedUser()
    }

    fun hasUsersFlow(): Flow<Boolean> {
        return userDao.hasUsers()
    }

    suspend fun updateLastLogin(userId: String) {
        userDao.updateLastLogin(userId, System.currentTimeMillis())
    }
}

