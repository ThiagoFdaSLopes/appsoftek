package com.grupo.appsoftek.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val password: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)

