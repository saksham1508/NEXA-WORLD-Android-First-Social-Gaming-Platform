package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_maps")
data class CustomMapEntity(
    @PrimaryKey val id: String,
    val title: String,
    val creatorName: String,
    val likesCount: Int = 12,
    val playsCount: Int = 89,
    val obstacleData: String, // Comma separated grid coordinates "x:y,x2:y2"
    val energySpawnerData: String,
    val createdAt: Long = System.currentTimeMillis()
)
