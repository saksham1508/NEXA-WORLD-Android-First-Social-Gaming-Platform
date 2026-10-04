package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.DailyMissionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MissionDao {
    @Query("SELECT * FROM daily_missions")
    fun getAllMissions(): Flow<List<DailyMissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissions(missions: List<DailyMissionEntity>)

    @Update
    suspend fun updateMission(mission: DailyMissionEntity)

    @Query("UPDATE daily_missions SET currentProgress = MIN(targetGoal, currentProgress + :increment) WHERE id = :missionId")
    suspend fun incrementProgress(missionId: String, increment: Int)

    @Query("UPDATE daily_missions SET isClaimed = 1 WHERE id = :missionId")
    suspend fun markClaimed(missionId: String)
}
