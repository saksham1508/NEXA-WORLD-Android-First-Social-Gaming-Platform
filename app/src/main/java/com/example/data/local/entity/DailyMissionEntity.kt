package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_missions")
data class DailyMissionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val currentProgress: Int,
    val targetGoal: Int,
    val rewardCoins: Long,
    val rewardXp: Int,
    val isClaimed: Boolean = false,
    val categoryIcon: String = "BATTLE" // "BATTLE", "ENERGY", "SOCIAL", "VICTORY"
)
