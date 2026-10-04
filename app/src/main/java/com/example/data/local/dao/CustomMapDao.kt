package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.CustomMapEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomMapDao {
    @Query("SELECT * FROM custom_maps ORDER BY createdAt DESC")
    fun getAllCustomMaps(): Flow<List<CustomMapEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomMap(map: CustomMapEntity)

    @Query("DELETE FROM custom_maps WHERE id = :mapId")
    suspend fun deleteMap(mapId: String)
}
