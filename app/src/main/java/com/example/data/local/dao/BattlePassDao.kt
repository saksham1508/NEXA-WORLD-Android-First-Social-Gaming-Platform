package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.BattlePassTierEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BattlePassDao {
    @Query("SELECT * FROM battle_pass_tiers ORDER BY tier ASC")
    fun getAllTiers(): Flow<List<BattlePassTierEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTiers(tiers: List<BattlePassTierEntity>)

    @Query("UPDATE battle_pass_tiers SET isFreeClaimed = 1 WHERE tier = :tier")
    suspend fun claimFreeReward(tier: Int)

    @Query("UPDATE battle_pass_tiers SET isPremiumClaimed = 1 WHERE tier = :tier")
    suspend fun claimPremiumReward(tier: Int)
}
