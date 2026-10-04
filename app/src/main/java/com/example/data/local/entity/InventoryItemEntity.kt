package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory")
data class InventoryItemEntity(
    @PrimaryKey val id: String,
    val category: String, // "HAIR", "OUTFIT", "VISOR", "WINGS", "EMOTE", "TITLE"
    val name: String,
    val rarity: String, // "COMMON", "RARE", "EPIC", "LEGENDARY"
    val priceCoins: Long = 0,
    val priceGems: Int = 0,
    val isEquipped: Boolean = false,
    val isUnlocked: Boolean = true,
    val valueParam: String // style name or color code
)
