package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "battle_pass_tiers")
data class BattlePassTierEntity(
    @PrimaryKey val tier: Int,
    val requiredStars: Int,
    val freeRewardTitle: String,
    val freeRewardType: String, // "COINS", "GEMS", "ITEM"
    val freeRewardAmount: Int,
    val isFreeClaimed: Boolean = false,
    val premiumRewardTitle: String,
    val premiumRewardType: String,
    val premiumRewardAmount: Int,
    val isPremiumClaimed: Boolean = false
)
