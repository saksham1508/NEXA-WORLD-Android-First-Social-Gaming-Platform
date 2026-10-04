package com.example.model

enum class Team {
    BLUE,
    RED
}

data class ArenaEntity(
    val id: String,
    val name: String,
    val isPlayer: Boolean,
    val team: Team,
    var x: Float,
    var y: Float,
    var vx: Float = 0f,
    var vy: Float = 0f,
    val radius: Float = 24f,
    var hp: Float = 100f,
    val maxHp: Float = 100f,
    var energyCores: Int = 0,
    var isDashing: Boolean = false,
    var dashTimer: Float = 0f,
    var dashCooldown: Float = 0f,
    var respawnTimer: Float = 0f,
    var kills: Int = 0,
    var angle: Float = 0f
)

data class EnergyNode(
    val id: Int,
    val x: Float,
    val y: Float,
    val value: Int = 1,
    var isAvailable: Boolean = true,
    var respawnTimer: Float = 0f
)

data class Projectile(
    val id: Long,
    val ownerId: String,
    val team: Team,
    var x: Float,
    var y: Float,
    val vx: Float,
    val vy: Float,
    val damage: Float = 25f,
    var lifetime: Float = 1.2f
)

data class GameParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Long,
    val size: Float,
    var alpha: Float = 1f,
    var lifetime: Float = 0.5f,
    val maxLifetime: Float = 0.5f
)

data class KillFeedEntry(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val highlightColor: Long
)

data class MatchmakingRegion(
    val id: String,
    val name: String,
    val flagEmoji: String,
    val basePingMs: Int
)

data class GameScore(
    val blueScore: Int = 0,
    val redScore: Int = 0,
    val targetScore: Int = 50,
    val timeRemainingSeconds: Int = 180,
    val isGameOver: Boolean = false,
    val winnerTeam: Team? = null
)
