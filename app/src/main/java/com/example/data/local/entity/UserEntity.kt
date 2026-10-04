package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "user_player_01",
    val username: String = "NovaStriker",
    val tag: String = "#7789",
    val level: Int = 12,
    val currentXp: Int = 3450,
    val requiredXp: Int = 5000,
    val rankTier: String = "Silver II",
    val rankPoints: Int = 1240,
    val nexCoins: Long = 4250,
    val nexGems: Int = 380,
    val selectedTitle: String = "Cyber Vanguard",
    val matchesPlayed: Int = 48,
    val matchesWon: Int = 31,
    val totalKills: Int = 184,
    val energyBanked: Int = 620,
    // JSON-like or serialized avatar configuration strings
    val skinTone: String = "#F5D0A9",
    val hairStyle: String = "Cyber Spikes",
    val hairColor: String = "#00E5FF",
    val eyeColor: String = "#FF2A85",
    val outfitStyle: String = "Nexus Scout",
    val outfitColor: String = "#1E293B",
    val accentColor: String = "#00E5FF",
    val visorStyle: String = "Holo Visor",
    val wingsStyle: String = "Quantum Jets",
    val emote: String = "Ready"
)
