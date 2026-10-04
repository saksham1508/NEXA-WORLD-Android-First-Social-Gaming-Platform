package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey val id: String,
    val username: String,
    val tag: String,
    val level: Int,
    val rankTier: String,
    val status: String, // "ONLINE_HUB", "IN_MATCH", "OFFLINE"
    val avatarHair: String,
    val avatarColor: String,
    val isInSquad: Boolean = false,
    val isBlocked: Boolean = false
)
