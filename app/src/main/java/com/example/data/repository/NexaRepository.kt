package com.example.data.repository

import com.example.data.local.NexaDatabase
import com.example.data.local.entity.BattlePassTierEntity
import com.example.data.local.entity.CustomMapEntity
import com.example.data.local.entity.DailyMissionEntity
import com.example.data.local.entity.FriendEntity
import com.example.data.local.entity.InventoryItemEntity
import com.example.data.local.entity.UserEntity
import com.example.model.AvatarConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class NexaRepository(private val database: NexaDatabase) {
    val userFlow: Flow<UserEntity?> = database.userDao().getUser()
    val inventoryFlow: Flow<List<InventoryItemEntity>> = database.inventoryDao().getAllInventory()
    val missionsFlow: Flow<List<DailyMissionEntity>> = database.missionDao().getAllMissions()
    val friendsFlow: Flow<List<FriendEntity>> = database.friendDao().getActiveFriends()
    val battlePassFlow: Flow<List<BattlePassTierEntity>> = database.battlePassDao().getAllTiers()
    val customMapsFlow: Flow<List<CustomMapEntity>> = database.customMapDao().getAllCustomMaps()

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val currentUser = database.userDao().getUser().firstOrNull()
        if (currentUser == null) {
            // Seed initial User
            database.userDao().insertUser(
                UserEntity(
                    id = "user_player_01",
                    username = "ApexCipher",
                    tag = "#4092",
                    level = 5,
                    currentXp = 1850,
                    requiredXp = 3000,
                    rankTier = "Silver III",
                    rankPoints = 1450,
                    nexCoins = 1800,
                    nexGems = 120,
                    selectedTitle = "Cyber Pioneer",
                    matchesPlayed = 18,
                    matchesWon = 11,
                    totalKills = 64,
                    energyBanked = 210,
                    skinTone = "#F5D0A9",
                    hairStyle = "Cyber Spikes",
                    hairColor = "#00E5FF",
                    eyeColor = "#FF2A85",
                    outfitStyle = "Nexus Scout",
                    outfitColor = "#1E293B",
                    accentColor = "#00E5FF",
                    visorStyle = "Holo Visor",
                    wingsStyle = "Quantum Jets",
                    emote = "Ready"
                )
            )

            // Seed initial inventory items
            val defaultItems = listOf(
                InventoryItemEntity("hair_01", "HAIR", "Cyber Spikes", "COMMON", 0, 0, true, true, "Cyber Spikes"),
                InventoryItemEntity("hair_02", "HAIR", "Anime Fringe", "RARE", 800, 0, false, true, "Anime Fringe"),
                InventoryItemEntity("hair_03", "HAIR", "Neon Dreads", "EPIC", 1500, 50, false, true, "Neon Dreads"),
                InventoryItemEntity("hair_04", "HAIR", "Sleek Bob", "COMMON", 400, 0, false, true, "Sleek Bob"),

                InventoryItemEntity("outfit_01", "OUTFIT", "Nexus Scout", "COMMON", 0, 0, true, true, "Nexus Scout"),
                InventoryItemEntity("outfit_02", "OUTFIT", "Cyber Ninja", "RARE", 1200, 0, false, true, "Cyber Ninja"),
                InventoryItemEntity("outfit_03", "OUTFIT", "Solar Knight", "EPIC", 2500, 100, false, true, "Solar Knight"),
                InventoryItemEntity("outfit_04", "OUTFIT", "Void Runner", "LEGENDARY", 4000, 200, false, false, "Void Runner"),

                InventoryItemEntity("visor_01", "VISOR", "None", "COMMON", 0, 0, false, true, "None"),
                InventoryItemEntity("visor_02", "VISOR", "Holo Visor", "RARE", 600, 0, true, true, "Holo Visor"),
                InventoryItemEntity("visor_03", "VISOR", "Cyber Mask", "EPIC", 1400, 40, false, true, "Cyber Mask"),
                InventoryItemEntity("visor_04", "VISOR", "VR Crown", "LEGENDARY", 3000, 150, false, false, "VR Crown"),

                InventoryItemEntity("wings_01", "WINGS", "None", "COMMON", 0, 0, false, true, "None"),
                InventoryItemEntity("wings_02", "WINGS", "Quantum Jets", "RARE", 900, 0, true, true, "Quantum Jets"),
                InventoryItemEntity("wings_03", "WINGS", "Energy Wings", "EPIC", 2200, 90, false, true, "Energy Wings"),
                InventoryItemEntity("wings_04", "WINGS", "Neon Cape", "LEGENDARY", 3500, 180, false, false, "Neon Cape")
            )
            database.inventoryDao().insertItems(defaultItems)

            // Seed daily missions
            val missions = listOf(
                DailyMissionEntity("m1", "Cyber Dominance", "Play 2 matches in Cyber Arena", 1, 2, 400, 250, false, "BATTLE"),
                DailyMissionEntity("m2", "Energy Collector", "Bank 10 Energy Cores in the conduit", 4, 10, 600, 350, false, "ENERGY"),
                DailyMissionEntity("m3", "Victory March", "Win 1 Quick Battle match", 1, 1, 800, 500, false, "VICTORY"),
                DailyMissionEntity("m4", "Social Vanguard", "High-five a friend or squadmate in Nexus City", 1, 1, 300, 200, false, "SOCIAL")
            )
            database.missionDao().insertMissions(missions)

            // Seed initial friends
            val friends = listOf(
                FriendEntity("f1", "Valkyrie_99", "#1102", 18, "Gold I", "ONLINE_HUB", "Anime Fringe", "#FF2A85", false, false),
                FriendEntity("f2", "NeonRacer", "#4821", 9, "Silver I", "IN_MATCH", "Cyber Spikes", "#00E5FF", false, false),
                FriendEntity("f3", "ShadowZero", "#9044", 24, "Diamond III", "ONLINE_HUB", "Neon Dreads", "#8A2BE2", false, false),
                FriendEntity("f4", "GlitchQueen", "#3312", 14, "Gold III", "OFFLINE", "Sleek Bob", "#FFD700", false, false)
            )
            database.friendDao().insertFriends(friends)

            // Seed Battle Pass Tiers (Season 1: Cyber Genesis)
            val tiers = (1..10).map { tier ->
                BattlePassTierEntity(
                    tier = tier,
                    requiredStars = tier * 10,
                    freeRewardTitle = when (tier % 3) {
                        1 -> "250 NEX Coins"
                        2 -> "XP Booster"
                        else -> "Profile Badge: Tier $tier"
                    },
                    freeRewardType = "COINS",
                    freeRewardAmount = 250,
                    isFreeClaimed = false,
                    premiumRewardTitle = when (tier) {
                        5 -> "Holo Visor - Chrome edition"
                        10 -> "Cyber Mecha Outfit (Legendary)"
                        else -> "40 NEX Gems"
                    },
                    premiumRewardType = if (tier == 5 || tier == 10) "ITEM" else "GEMS",
                    premiumRewardAmount = if (tier == 5 || tier == 10) 1 else 40,
                    isPremiumClaimed = false
                )
            }
            database.battlePassDao().insertTiers(tiers)

            // Seed sample community maps in Creator District
            val sampleMaps = listOf(
                CustomMapEntity(
                    id = "map_01",
                    title = "Neon Hexagon",
                    creatorName = "PixelMaster",
                    likesCount = 428,
                    playsCount = 1890,
                    obstacleData = "2:2,2:4,4:2,4:4,3:3",
                    energySpawnerData = "1:3,5:3,3:1,3:5"
                ),
                CustomMapEntity(
                    id = "map_02",
                    title = "Quantum Alley",
                    creatorName = "CyberGhost",
                    likesCount = 215,
                    playsCount = 840,
                    obstacleData = "1:2,1:3,1:4,5:2,5:3,5:4",
                    energySpawnerData = "3:3,2:1,4:5"
                )
            )
            sampleMaps.forEach { database.customMapDao().insertCustomMap(it) }
        }
    }

    suspend fun updateAvatar(config: AvatarConfig) = withContext(Dispatchers.IO) {
        database.userDao().updateAvatar(
            skinTone = config.skinToneHex,
            hairStyle = config.hairStyle,
            hairColor = config.hairColorHex,
            eyeColor = config.eyeColorHex,
            outfitStyle = config.outfitStyle,
            outfitColor = config.outfitColorHex,
            accentColor = config.accentColorHex,
            visorStyle = config.visorStyle,
            wingsStyle = config.wingsStyle,
            emote = config.emote
        )
    }

    suspend fun recordMatchResult(won: Boolean, kills: Int, energyBanked: Int) = withContext(Dispatchers.IO) {
        val earnedCoins = if (won) 350L else 150L
        val earnedXp = if (won) 450 else 200
        database.userDao().addRewards(coins = earnedCoins, xp = earnedXp)
        database.missionDao().incrementProgress("m1", 1)
        database.missionDao().incrementProgress("m2", energyBanked)
        if (won) {
            database.missionDao().incrementProgress("m3", 1)
        }
    }

    suspend fun claimMission(missionId: String, rewardCoins: Long, rewardXp: Int) = withContext(Dispatchers.IO) {
        database.missionDao().markClaimed(missionId)
        database.userDao().addRewards(coins = rewardCoins, xp = rewardXp)
    }

    suspend fun claimBattlePassTier(tier: Int, isPremium: Boolean, amount: Int, type: String) = withContext(Dispatchers.IO) {
        if (isPremium) {
            database.battlePassDao().claimPremiumReward(tier)
            if (type == "GEMS") {
                val user = database.userDao().getUser().firstOrNull()
                user?.let {
                    database.userDao().updateUser(it.copy(nexGems = it.nexGems + amount))
                }
            }
        } else {
            database.battlePassDao().claimFreeReward(tier)
            if (type == "COINS") {
                database.userDao().addRewards(coins = amount.toLong(), xp = 100)
            }
        }
    }

    suspend fun toggleSquadFriend(friendId: String, currentStatus: Boolean) = withContext(Dispatchers.IO) {
        database.friendDao().updateSquadStatus(friendId, !currentStatus)
    }

    suspend fun reportPlayer(friendId: String) = withContext(Dispatchers.IO) {
        database.friendDao().blockPlayer(friendId)
    }

    suspend fun saveCustomMap(map: CustomMapEntity) = withContext(Dispatchers.IO) {
        database.customMapDao().insertCustomMap(map)
    }
}
