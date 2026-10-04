package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BattlePassDao
import com.example.data.local.dao.CustomMapDao
import com.example.data.local.dao.FriendDao
import com.example.data.local.dao.InventoryDao
import com.example.data.local.dao.MissionDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.BattlePassTierEntity
import com.example.data.local.entity.CustomMapEntity
import com.example.data.local.entity.DailyMissionEntity
import com.example.data.local.entity.FriendEntity
import com.example.data.local.entity.InventoryItemEntity
import com.example.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        InventoryItemEntity::class,
        DailyMissionEntity::class,
        FriendEntity::class,
        BattlePassTierEntity::class,
        CustomMapEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NexaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun missionDao(): MissionDao
    abstract fun friendDao(): FriendDao
    abstract fun battlePassDao(): BattlePassDao
    abstract fun customMapDao(): CustomMapDao

    companion object {
        @Volatile
        private var INSTANCE: NexaDatabase? = null

        fun getInstance(context: Context): NexaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NexaDatabase::class.java,
                    "nexa_world.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
