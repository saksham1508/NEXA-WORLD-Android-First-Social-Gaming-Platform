package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUser(userId: String = "user_player_01"): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET nexCoins = nexCoins + :coins, currentXp = currentXp + :xp WHERE id = :userId")
    suspend fun addRewards(userId: String = "user_player_01", coins: Long, xp: Int)

    @Query("UPDATE users SET skinTone = :skinTone, hairStyle = :hairStyle, hairColor = :hairColor, eyeColor = :eyeColor, outfitStyle = :outfitStyle, outfitColor = :outfitColor, accentColor = :accentColor, visorStyle = :visorStyle, wingsStyle = :wingsStyle, emote = :emote WHERE id = :userId")
    suspend fun updateAvatar(
        userId: String = "user_player_01",
        skinTone: String,
        hairStyle: String,
        hairColor: String,
        eyeColor: String,
        outfitStyle: String,
        outfitColor: String,
        accentColor: String,
        visorStyle: String,
        wingsStyle: String,
        emote: String
    )
}
