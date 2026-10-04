package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.FriendEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {
    @Query("SELECT * FROM friends WHERE isBlocked = 0")
    fun getActiveFriends(): Flow<List<FriendEntity>>

    @Query("SELECT * FROM friends WHERE isBlocked = 1")
    fun getBlockedPlayers(): Flow<List<FriendEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(friends: List<FriendEntity>)

    @Update
    suspend fun updateFriend(friend: FriendEntity)

    @Query("UPDATE friends SET isInSquad = :inSquad WHERE id = :friendId")
    suspend fun updateSquadStatus(friendId: String, inSquad: Boolean)

    @Query("UPDATE friends SET isBlocked = 1 WHERE id = :friendId")
    suspend fun blockPlayer(friendId: String)
}
