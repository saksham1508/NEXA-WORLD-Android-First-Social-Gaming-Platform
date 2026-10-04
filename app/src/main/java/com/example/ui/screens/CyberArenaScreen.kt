package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ArenaEntity
import com.example.model.AvatarConfig
import com.example.model.EnergyNode
import com.example.model.GameParticle
import com.example.model.GameScore
import com.example.model.GraphicsProfile
import com.example.model.KillFeedEntry
import com.example.model.Projectile
import com.example.model.Team
import com.example.ui.components.AvatarCanvas
import com.example.ui.components.CyberButton
import com.example.ui.theme.EnergyCoreColor
import com.example.ui.theme.HealthGreen
import com.example.ui.theme.HealthRed
import com.example.ui.theme.NexaBgDark
import com.example.ui.theme.NexaBorderDark
import com.example.ui.theme.NexaCardDark
import com.example.ui.theme.NexaCyan
import com.example.ui.theme.NexaCyanDark
import com.example.ui.theme.NexaGold
import com.example.ui.theme.NexaMagenta
import com.example.ui.theme.NexaOrange
import com.example.ui.theme.NexaPurple
import com.example.ui.theme.NexaSurfaceDark
import com.example.ui.theme.NexaTextMuted
import com.example.ui.theme.NexaTextPrimary
import com.example.ui.theme.NexaTextSecondary
import com.example.ui.theme.TeamBlueColor
import com.example.ui.theme.TeamRedColor
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun CyberArenaScreen(
    avatarConfig: AvatarConfig,
    graphicsProfile: GraphicsProfile,
    onMatchFinished: (won: Boolean, kills: Int, energyBanked: Int) -> Unit,
    onExitArena: () -> Unit
) {
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator }

    BackHandler {
        onExitArena()
    }

    // Joystick Touch Controls
    var joystickActive by remember { mutableStateOf(false) }
    var joystickCenterX by remember { mutableFloatStateOf(0f) }
    var joystickCenterY by remember { mutableFloatStateOf(0f) }
    var joystickKnobX by remember { mutableFloatStateOf(0f) }
    var joystickKnobY by remember { mutableFloatStateOf(0f) }
    var moveVectorX by remember { mutableFloatStateOf(0f) }
    var moveVectorY by remember { mutableFloatStateOf(0f) }

    // Dash cooldown
    var dashCooldownProgress by remember { mutableFloatStateOf(0f) }
    var canDash by remember { mutableStateOf(true) }

    // Game match state
    var gameScore by remember { mutableStateOf(GameScore()) }
    var isMatchOver by remember { mutableStateOf(false) }
    var playerKills by remember { mutableIntStateOf(0) }
    var playerBankedEnergy by remember { mutableIntStateOf(0) }

    // Network Simulation state
    var simulatedPing by remember { mutableIntStateOf(38) }
    var isSimulatedLag by remember { mutableStateOf(false) }

    // Kill Feed
    val killFeed = remember { mutableStateListOf<KillFeedEntry>() }

    // Game Entities
    val entities = remember {
        mutableStateListOf(
            // Player
            ArenaEntity(
                id = "player",
                name = "ApexCipher",
                isPlayer = true,
                team = Team.BLUE,
                x = 100f,
                y = 500f,
                radius = 22f
            ),
            // Blue Teammates
            ArenaEntity(
                id = "bot_blue_1",
                name = "Valkyrie_99",
                isPlayer = false,
                team = Team.BLUE,
                x = 80f,
                y = 420f,
                radius = 20f
            ),
            ArenaEntity(
                id = "bot_blue_2",
                name = "Striker_03",
                isPlayer = false,
                team = Team.BLUE,
                x = 120f,
                y = 580f,
                radius = 20f
            ),
            // Red Opponents
            ArenaEntity(
                id = "bot_red_1",
                name = "Viper_X",
                isPlayer = false,
                team = Team.RED,
                x = 750f,
                y = 500f,
                radius = 20f
            ),
            ArenaEntity(
                id = "bot_red_2",
                name = "Omega_9",
                isPlayer = false,
                team = Team.RED,
                x = 780f,
                y = 420f,
                radius = 20f
            ),
            ArenaEntity(
                id = "bot_red_3",
                name = "Blaze_Z",
                isPlayer = false,
                team = Team.RED,
                x = 720f,
                y = 580f,
                radius = 20f
            )
        )
    }

    val energyNodes = remember {
        mutableStateListOf(
            EnergyNode(1, 400f, 320f),
            EnergyNode(2, 400f, 500f),
            EnergyNode(3, 400f, 680f),
            EnergyNode(4, 260f, 500f),
            EnergyNode(5, 540f, 500f)
        )
    }

    val projectiles = remember { mutableStateListOf<Projectile>() }
    val particles = remember { mutableStateListOf<GameParticle>() }

    fun triggerHaptic(durationMs: Long = 40) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    fun firePlayerBlaster() {
        val player = entities.firstOrNull { it.isPlayer } ?: return
        if (player.hp <= 0f) return

        // Find closest enemy or aim in move direction
        var aimAngle = player.angle
        val enemies = entities.filter { it.team != player.team && it.hp > 0f }
        if (enemies.isNotEmpty()) {
            val closest = enemies.minByOrNull {
                val dx = it.x - player.x
                val dy = it.y - player.y
                dx * dx + dy * dy
            }
            if (closest != null) {
                aimAngle = atan2(closest.y - player.y, closest.x - player.x)
            }
        }

        val speed = 14f
        val proj = Projectile(
            id = System.nanoTime(),
            ownerId = player.id,
            team = player.team,
            x = player.x + cos(aimAngle) * 26f,
            y = player.y + sin(aimAngle) * 26f,
            vx = cos(aimAngle) * speed,
            vy = sin(aimAngle) * speed
        )
        projectiles.add(proj)
        triggerHaptic(25)
    }

    fun triggerPlayerDash() {
        val player = entities.firstOrNull { it.isPlayer } ?: return
        if (!canDash || player.hp <= 0f) return

        player.isDashing = true
        player.dashTimer = 0.25f
        canDash = false
        dashCooldownProgress = 1f

        // Boost speed in move or facing angle
        val speed = 18f
        val angle = if (moveVectorX != 0f || moveVectorY != 0f) {
            atan2(moveVectorY, moveVectorX)
        } else {
            player.angle
        }
        player.vx = cos(angle) * speed
        player.vy = sin(angle) * speed

        // Dash particles
        for (i in 0 until (12 * graphicsProfile.particleDensity).toInt()) {
            particles.add(
                GameParticle(
                    x = player.x,
                    y = player.y,
                    vx = (Random.nextFloat() - 0.5f) * 6f,
                    vy = (Random.nextFloat() - 0.5f) * 6f,
                    color = 0xFF00E5FF,
                    size = Random.nextFloat() * 6f + 3f,
                    lifetime = 0.4f,
                    maxLifetime = 0.4f
                )
            )
        }
        triggerHaptic(60)
    }

    // 60 FPS Game Loop
    LaunchedEffect(isMatchOver) {
        if (isMatchOver) return@LaunchedEffect

        var lastFrameNanos = 0L
        var timerAccumulator = 0f
        var botDecisionAccumulator = 0f
        var pingJitterAccumulator = 0f

        while (!isMatchOver) {
            withFrameNanos { now ->
                if (lastFrameNanos == 0L) {
                    lastFrameNanos = now
                    return@withFrameNanos
                }

                val dtNanos = now - lastFrameNanos
                lastFrameNanos = now
                var dt = dtNanos / 1_000_000_000f
                if (dt > 0.05f) dt = 0.05f // Clamp dt to prevent tunneling on frame hitch

                // Ping simulation jitter
                pingJitterAccumulator += dt
                if (pingJitterAccumulator > 1.2f) {
                    pingJitterAccumulator = 0f
                    simulatedPing = if (isSimulatedLag) Random.nextInt(160, 290) else Random.nextInt(32, 54)
                }

                // Match time countdown
                timerAccumulator += dt
                if (timerAccumulator >= 1f) {
                    timerAccumulator = 0f
                    val newTime = gameScore.timeRemainingSeconds - 1
                    if (newTime <= 0) {
                        val winner = if (gameScore.blueScore >= gameScore.redScore) Team.BLUE else Team.RED
                        gameScore = gameScore.copy(timeRemainingSeconds = 0, isGameOver = true, winnerTeam = winner)
                        isMatchOver = true
                        onMatchFinished(winner == Team.BLUE, playerKills, playerBankedEnergy)
                    } else {
                        gameScore = gameScore.copy(timeRemainingSeconds = newTime)
                    }
                }

                // Update Dash Cooldown
                if (!canDash) {
                    dashCooldownProgress -= dt / 3.0f
                    if (dashCooldownProgress <= 0f) {
                        dashCooldownProgress = 0f
                        canDash = true
                    }
                }

                // Energy Nodes Respawn Ticker
                energyNodes.forEach { node ->
                    if (!node.isAvailable) {
                        node.respawnTimer -= dt
                        if (node.respawnTimer <= 0f) {
                            node.isAvailable = true
                        }
                    }
                }

                // Player Movement
                val player = entities.firstOrNull { it.isPlayer }
                if (player != null && player.hp > 0f) {
                    if (player.isDashing) {
                        player.dashTimer -= dt
                        if (player.dashTimer <= 0f) {
                            player.isDashing = false
                        }
                    } else {
                        val moveSpeed = 5.5f
                        player.vx = moveVectorX * moveSpeed
                        player.vy = moveVectorY * moveSpeed
                        if (moveVectorX != 0f || moveVectorY != 0f) {
                            player.angle = atan2(moveVectorY, moveVectorX)
                        }
                    }
                }

                // Bot AI Decision Making
                botDecisionAccumulator += dt
                val triggerBotFire = botDecisionAccumulator >= 0.75f
                if (triggerBotFire) botDecisionAccumulator = 0f

                entities.forEach { entity ->
                    // Respawn logic
                    if (entity.hp <= 0f) {
                        entity.respawnTimer -= dt
                        if (entity.respawnTimer <= 0f) {
                            entity.hp = entity.maxHp
                            entity.x = if (entity.team == Team.BLUE) 100f else 750f
                            entity.y = Random.nextFloat() * 300f + 350f
                            entity.energyCores = 0
                        }
                        return@forEach
                    }

                    // Bot Logic
                    if (!entity.isPlayer) {
                        // If has 3+ cores, head to Conduit to bank!
                        val targetX: Float
                        val targetY: Float

                        if (entity.energyCores >= 3) {
                            // Conduit destination
                            if (entity.team == Team.BLUE) {
                                targetX = 90f
                                targetY = 500f
                            } else {
                                targetX = 760f
                                targetY = 500f
                            }
                        } else {
                            // Find nearest available energy node or nearest enemy
                            val availableNodes = energyNodes.filter { it.isAvailable }
                            if (availableNodes.isNotEmpty()) {
                                val closestNode = availableNodes.minByOrNull {
                                    val dx = it.x - entity.x
                                    val dy = it.y - entity.y
                                    dx * dx + dy * dy
                                }!!
                                targetX = closestNode.x
                                targetY = closestNode.y
                            } else {
                                targetX = 400f
                                targetY = 500f
                            }
                        }

                        val dx = targetX - entity.x
                        val dy = targetY - entity.y
                        val dist = sqrt(dx * dx + dy * dy)
                        if (dist > 10f) {
                            val botSpeed = 3.6f
                            entity.vx = (dx / dist) * botSpeed
                            entity.vy = (dy / dist) * botSpeed
                            entity.angle = atan2(dy, dx)
                        }

                        // Bot Fire at Opponents
                        if (triggerBotFire && Random.nextFloat() < 0.65f) {
                            val opponents = entities.filter { it.team != entity.team && it.hp > 0f }
                            val targetOpponent = opponents.minByOrNull {
                                val odx = it.x - entity.x
                                val ody = it.y - entity.y
                                odx * odx + ody * ody
                            }
                            if (targetOpponent != null) {
                                val odx = targetOpponent.x - entity.x
                                val ody = targetOpponent.y - entity.y
                                val odist = sqrt(odx * odx + ody * ody)
                                if (odist < 380f) {
                                    val fAngle = atan2(ody, odx)
                                    val bSpeed = 12f
                                    projectiles.add(
                                        Projectile(
                                            id = System.nanoTime() + entity.id.hashCode(),
                                            ownerId = entity.id,
                                            team = entity.team,
                                            x = entity.x + cos(fAngle) * 22f,
                                            y = entity.y + sin(fAngle) * 22f,
                                            vx = cos(fAngle) * bSpeed,
                                            vy = sin(fAngle) * bSpeed
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Apply entity velocity and arena bounds
                    entity.x += entity.vx
                    entity.y += entity.vy

                    // Arena bounds clamping (Width: 850, Height: 1000)
                    entity.x = entity.x.coerceIn(50f, 800f)
                    entity.y = entity.y.coerceIn(120f, 880f)

                    // Pick up Energy Cores
                    energyNodes.forEach { node ->
                        if (node.isAvailable && entity.energyCores < 5) {
                            val ndx = entity.x - node.x
                            val ndy = entity.y - node.y
                            if (sqrt(ndx * ndx + ndy * ndy) < entity.radius + 18f) {
                                node.isAvailable = false
                                node.respawnTimer = 6f
                                entity.energyCores += node.value
                                if (entity.isPlayer) {
                                    triggerHaptic(30)
                                }
                            }
                        }
                    }

                    // Bank Cores in Team Conduit
                    if (entity.energyCores > 0) {
                        // Blue Conduit: x < 120 && 440 < y < 560
                        if (entity.team == Team.BLUE && entity.x < 130f && entity.y in 430f..570f) {
                            val banked = entity.energyCores
                            entity.energyCores = 0
                            val newBlue = gameScore.blueScore + banked
                            if (entity.isPlayer) {
                                playerBankedEnergy += banked
                                triggerHaptic(80)
                            }
                            if (newBlue >= gameScore.targetScore) {
                                gameScore = gameScore.copy(blueScore = newBlue, isGameOver = true, winnerTeam = Team.BLUE)
                                isMatchOver = true
                                onMatchFinished(true, playerKills, playerBankedEnergy)
                            } else {
                                gameScore = gameScore.copy(blueScore = newBlue)
                            }
                        }
                        // Red Conduit: x > 720 && 440 < y < 560
                        if (entity.team == Team.RED && entity.x > 720f && entity.y in 430f..570f) {
                            val banked = entity.energyCores
                            entity.energyCores = 0
                            val newRed = gameScore.redScore + banked
                            if (newRed >= gameScore.targetScore) {
                                gameScore = gameScore.copy(redScore = newRed, isGameOver = true, winnerTeam = Team.RED)
                                isMatchOver = true
                                onMatchFinished(false, playerKills, playerBankedEnergy)
                            } else {
                                gameScore = gameScore.copy(redScore = newRed)
                            }
                        }
                    }
                }

                // Update Projectiles
                val projIterator = projectiles.iterator()
                while (projIterator.hasNext()) {
                    val p = projIterator.next()
                    p.x += p.vx
                    p.y += p.vy
                    p.lifetime -= dt

                    var hit = false
                    if (p.lifetime <= 0f || p.x < 20f || p.x > 830f || p.y < 80f || p.y > 920f) {
                        projIterator.remove()
                        continue
                    }

                    // Check hit on opposing team entities
                    for (target in entities) {
                        if (target.team != p.team && target.hp > 0f) {
                            val pdx = target.x - p.x
                            val pdy = target.y - p.y
                            if (sqrt(pdx * pdx + pdy * pdy) < target.radius + 8f) {
                                target.hp -= p.damage
                                hit = true

                                // Particles on hit
                                for (i in 0 until (6 * graphicsProfile.particleDensity).toInt()) {
                                    particles.add(
                                        GameParticle(
                                            x = target.x,
                                            y = target.y,
                                            vx = (Random.nextFloat() - 0.5f) * 5f,
                                            vy = (Random.nextFloat() - 0.5f) * 5f,
                                            color = if (target.team == Team.BLUE) 0xFF00E5FF else 0xFFFF3366,
                                            size = 4f,
                                            lifetime = 0.35f,
                                            maxLifetime = 0.35f
                                        )
                                    )
                                }

                                if (target.hp <= 0f) {
                                    target.hp = 0f
                                    target.respawnTimer = 4.5f
                                    target.energyCores = 0 // Lose cores

                                    val killer = entities.firstOrNull { it.id == p.ownerId }
                                    if (killer?.isPlayer == true) {
                                        playerKills++
                                        triggerHaptic(120)
                                    }

                                    // Add to kill feed
                                    val killerName = killer?.name ?: "Drone"
                                    killFeed.add(
                                        0,
                                        KillFeedEntry(
                                            text = "$killerName vaporized ${target.name}",
                                            highlightColor = if (p.team == Team.BLUE) 0xFF00E5FF else 0xFFFF3366
                                        )
                                    )
                                    if (killFeed.size > 4) killFeed.removeLast()
                                }
                                break
                            }
                        }
                    }

                    if (hit) {
                        projIterator.remove()
                    }
                }

                // Update Particles
                val partIterator = particles.iterator()
                while (partIterator.hasNext()) {
                    val pt = partIterator.next()
                    pt.x += pt.vx
                    pt.y += pt.vy
                    pt.lifetime -= dt
                    pt.alpha = pt.lifetime / pt.maxLifetime
                    if (pt.lifetime <= 0f) {
                        partIterator.remove()
                    }
                }
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBgDark)
    ) {
        val arenaWidth = constraints.maxWidth.toFloat()
        val arenaHeight = constraints.maxHeight.toFloat()
        val scaleX = arenaWidth / 850f
        val scaleY = arenaHeight / 1000f

        // 1. GAME RENDER CANVAS
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw Sci-Fi Arena Grid
            val gridStep = 45f * scaleX
            var gx = 0f
            while (gx < arenaWidth) {
                drawLine(
                    color = NexaBorderDark.copy(alpha = 0.25f),
                    start = Offset(gx, 0f),
                    end = Offset(gx, arenaHeight),
                    strokeWidth = 1f
                )
                gx += gridStep
            }
            var gy = 0f
            while (gy < arenaHeight) {
                drawLine(
                    color = NexaBorderDark.copy(alpha = 0.25f),
                    start = Offset(0f, gy),
                    end = Offset(arenaWidth, gy),
                    strokeWidth = 1f
                )
                gy += gridStep
            }

            // Draw Team Conduits
            // Blue Conduit (Bottom/Left)
            drawRoundRect(
                color = TeamBlueColor.copy(alpha = 0.25f),
                topLeft = Offset(20f * scaleX, 420f * scaleY),
                size = Size(110f * scaleX, 160f * scaleY),
                cornerRadius = CornerRadius(16f, 16f)
            )
            drawRoundRect(
                color = TeamBlueColor,
                topLeft = Offset(20f * scaleX, 420f * scaleY),
                size = Size(110f * scaleX, 160f * scaleY),
                cornerRadius = CornerRadius(16f, 16f),
                style = Stroke(width = 2.5f)
            )

            // Red Conduit (Top/Right)
            drawRoundRect(
                color = TeamRedColor.copy(alpha = 0.25f),
                topLeft = Offset(720f * scaleX, 420f * scaleY),
                size = Size(110f * scaleX, 160f * scaleY),
                cornerRadius = CornerRadius(16f, 16f)
            )
            drawRoundRect(
                color = TeamRedColor,
                topLeft = Offset(720f * scaleX, 420f * scaleY),
                size = Size(110f * scaleX, 160f * scaleY),
                cornerRadius = CornerRadius(16f, 16f),
                style = Stroke(width = 2.5f)
            )

            // Draw Energy Nodes
            energyNodes.forEach { node ->
                val nx = node.x * scaleX
                val ny = node.y * scaleY
                if (node.isAvailable) {
                    // Pulsing Core
                    drawCircle(
                        color = EnergyCoreColor.copy(alpha = 0.35f),
                        radius = 24f * scaleX,
                        center = Offset(nx, ny)
                    )
                    drawCircle(
                        color = EnergyCoreColor,
                        radius = 12f * scaleX,
                        center = Offset(nx, ny)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 5f * scaleX,
                        center = Offset(nx, ny)
                    )
                } else {
                    // Spawner base pad
                    drawCircle(
                        color = NexaBorderDark,
                        radius = 14f * scaleX,
                        center = Offset(nx, ny),
                        style = Stroke(width = 2f)
                    )
                }
            }

            // Draw Particles
            particles.forEach { p ->
                drawCircle(
                    color = Color(p.color).copy(alpha = p.alpha),
                    radius = p.size * scaleX,
                    center = Offset(p.x * scaleX, p.y * scaleY)
                )
            }

            // Draw Projectiles
            projectiles.forEach { p ->
                val px = p.x * scaleX
                val py = p.y * scaleY
                val pColor = if (p.team == Team.BLUE) TeamBlueColor else TeamRedColor
                drawCircle(
                    color = pColor,
                    radius = 7f * scaleX,
                    center = Offset(px, py)
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.5f * scaleX,
                    center = Offset(px, py)
                )
            }

            // Draw Entities
            entities.forEach { entity ->
                if (entity.hp <= 0f) return@forEach
                val ex = entity.x * scaleX
                val ey = entity.y * scaleY
                val er = entity.radius * scaleX
                val teamColor = if (entity.team == Team.BLUE) TeamBlueColor else TeamRedColor

                // Dash Aura
                if (entity.isDashing) {
                    drawCircle(
                        color = teamColor.copy(alpha = 0.45f),
                        radius = er * 1.6f,
                        center = Offset(ex, ey)
                    )
                }

                // Entity Body
                drawCircle(
                    color = NexaCardDark,
                    radius = er,
                    center = Offset(ex, ey)
                )
                drawCircle(
                    color = teamColor,
                    radius = er,
                    center = Offset(ex, ey),
                    style = Stroke(width = if (entity.isPlayer) 3.5f else 2.5f)
                )

                // Directional Aim Pointer
                val pX = ex + cos(entity.angle) * (er + 6f * scaleX)
                val pY = ey + sin(entity.angle) * (er + 6f * scaleX)
                drawLine(
                    color = teamColor,
                    start = Offset(ex, ey),
                    end = Offset(pX, pY),
                    strokeWidth = 3f * scaleX
                )

                // Energy Cores Indicator Rings
                if (entity.energyCores > 0) {
                    for (c in 0 until entity.energyCores) {
                        val cAngle = (c * (360f / 5f)) * (Math.PI.toFloat() / 180f)
                        val cx = ex + cos(cAngle) * (er + 12f * scaleX)
                        val cy = ey + sin(cAngle) * (er + 12f * scaleX)
                        drawCircle(
                            color = EnergyCoreColor,
                            radius = 4.5f * scaleX,
                            center = Offset(cx, cy)
                        )
                    }
                }

                // Health Bar
                val barWidth = er * 2.2f
                val barHeight = 4.5f * scaleY
                val barTop = ey - er - 14f * scaleY
                val hpRatio = (entity.hp / entity.maxHp).coerceIn(0f, 1f)

                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.6f),
                    topLeft = Offset(ex - barWidth / 2f, barTop),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(2f, 2f)
                )
                drawRoundRect(
                    color = if (hpRatio > 0.4f) HealthGreen else HealthRed,
                    topLeft = Offset(ex - barWidth / 2f, barTop),
                    size = Size(barWidth * hpRatio, barHeight),
                    cornerRadius = CornerRadius(2f, 2f)
                )
            }
        }

        // 2. HUD OVERLAYS
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            // TOP BAR: Scores, Timer, Telemetry & Leave Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Exit button
                IconButton(
                    onClick = onExitArena,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(NexaCardDark.copy(alpha = 0.85f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Arena",
                        tint = NexaTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Match Scoreboard HUD
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(NexaCardDark.copy(alpha = 0.92f))
                        .border(1.dp, NexaBorderDark, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Blue Team Score
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(TeamBlueColor)
                        )
                        Text(
                            text = "${gameScore.blueScore}",
                            color = TeamBlueColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }

                    // Timer
                    val minutes = gameScore.timeRemainingSeconds / 60
                    val seconds = gameScore.timeRemainingSeconds % 60
                    Text(
                        text = "%02d:%02d".format(minutes, seconds),
                        color = NexaTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    // Red Team Score
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${gameScore.redScore}",
                            color = TeamRedColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(TeamRedColor)
                        )
                    }
                }

                // Telemetry & Lag Simulation Toggle
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NexaCardDark.copy(alpha = 0.85f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (isSimulatedLag) Icons.Default.WifiOff else Icons.Default.Wifi,
                        contentDescription = null,
                        tint = if (isSimulatedLag) HealthRed else HealthGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${simulatedPing}ms",
                        color = if (isSimulatedLag) HealthRed else HealthGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Objective Sub-Header Banner
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(12.dp))
                    .background(NexaBgDark.copy(alpha = 0.7f))
                    .padding(horizontal = 10.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = EnergyCoreColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Collect Cores & Bank in Blue Conduit (Target: 50)",
                    color = NexaTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Kill Feed (Top Left)
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth(0.5f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                killFeed.forEach { feed ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NexaCardDark.copy(alpha = 0.75f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = feed.text,
                            color = Color(feed.highlightColor),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // BOTTOM CONTROLS ROW: Left Virtual Joystick, Right Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp, start = 12.dp, end = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // 1. VIRTUAL ANALOG JOYSTICK (Left Thumb)
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(NexaCardDark.copy(alpha = 0.45f))
                        .border(1.5.dp, NexaCyan.copy(alpha = 0.35f), CircleShape)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    joystickActive = true
                                    joystickCenterX = size.width / 2f
                                    joystickCenterY = size.height / 2f
                                    val dx = offset.x - joystickCenterX
                                    val dy = offset.y - joystickCenterY
                                    val maxDist = size.width * 0.4f
                                    val dist = sqrt(dx * dx + dy * dy)
                                    val clamped = dist.coerceAtMost(maxDist)
                                    val angle = atan2(dy, dx)
                                    joystickKnobX = cos(angle) * clamped
                                    joystickKnobY = sin(angle) * clamped
                                    moveVectorX = (joystickKnobX / maxDist).coerceIn(-1f, 1f)
                                    moveVectorY = (joystickKnobY / maxDist).coerceIn(-1f, 1f)
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    val maxDist = size.width * 0.4f
                                    val newX = joystickKnobX + dragAmount.x
                                    val newY = joystickKnobY + dragAmount.y
                                    val dist = sqrt(newX * newX + newY * newY)
                                    val angle = atan2(newY, newX)
                                    val clamped = dist.coerceAtMost(maxDist)
                                    joystickKnobX = cos(angle) * clamped
                                    joystickKnobY = sin(angle) * clamped
                                    moveVectorX = (joystickKnobX / maxDist).coerceIn(-1f, 1f)
                                    moveVectorY = (joystickKnobY / maxDist).coerceIn(-1f, 1f)
                                },
                                onDragEnd = {
                                    joystickActive = false
                                    joystickKnobX = 0f
                                    joystickKnobY = 0f
                                    moveVectorX = 0f
                                    moveVectorY = 0f
                                },
                                onDragCancel = {
                                    joystickActive = false
                                    joystickKnobX = 0f
                                    joystickKnobY = 0f
                                    moveVectorX = 0f
                                    moveVectorY = 0f
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Joystick Knob
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(joystickKnobX.roundToInt(), joystickKnobY.roundToInt()) }
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(NexaCyan, NexaCyanDark)
                                )
                            )
                            .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                    )
                }

                // 2. RIGHT COMBAT & DASH BUTTONS (Right Thumb)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // QUANTUM DASH BUTTON
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                if (canDash) Brush.radialGradient(listOf(NexaMagenta, NexaPurple))
                                else Brush.radialGradient(listOf(NexaBorderDark, NexaCardDark))
                            )
                            .border(1.5.dp, if (canDash) NexaMagenta else NexaBorderDark, CircleShape)
                            .clickable(enabled = canDash) {
                                triggerPlayerDash()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = "Quantum Dash",
                                tint = if (canDash) Color.White else NexaTextMuted,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = if (canDash) "DASH" else "%.1fs".format(dashCooldownProgress * 3.0f),
                                color = if (canDash) Color.White else NexaTextMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // PRIMARY BLASTER FIRE BUTTON
                    Box(
                        modifier = Modifier
                            .testTag("blaster_fire_button")
                            .size(78.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(NexaCyan, NexaCyanDark)
                                )
                            )
                            .border(2.dp, Color.White.copy(alpha = 0.9f), CircleShape)
                            .shadow(16.dp, CircleShape, spotColor = NexaCyan)
                            .clickable {
                                firePlayerBlaster()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GpsFixed,
                                contentDescription = "Fire Blaster",
                                tint = NexaBgDark,
                                modifier = Modifier.size(34.dp)
                            )
                            Text(
                                text = "FIRE",
                                color = NexaBgDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // 3. VICTORY / DEFEAT MODAL OVERLAY
        AnimatedVisibility(
            visible = isMatchOver,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            val won = gameScore.winnerTeam == Team.BLUE
            val earnedCoins = if (won) 350L else 150L
            val earnedXp = if (won) 450 else 200

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NexaSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            listOf(if (won) NexaGold else TeamRedColor, NexaPurple)
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar Victory Badge
                        AvatarCanvas(
                            config = avatarConfig.copy(emote = if (won) "Victory" else "Ready"),
                            size = 110.dp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (won) "VICTORY!" else "DEFEAT",
                            color = if (won) NexaGold else TeamRedColor,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = if (won) "Cyber Arena Champions" else "Good match! Refine your tactics",
                            color = NexaTextSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Match Stats Summary
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(NexaCardDark)
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "$playerKills", color = NexaCyan, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Kills", color = NexaTextMuted, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "$playerBankedEnergy", color = EnergyCoreColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Cores Banked", color = NexaTextMuted, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "+$earnedXp", color = NexaPurple, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text(text = "XP Earned", color = NexaTextMuted, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "+$earnedCoins", color = NexaGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Coins", color = NexaTextMuted, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        CyberButton(
                            text = "CLAIM REWARDS & RETURN",
                            onClick = onExitArena,
                            glowColor = if (won) NexaGold else NexaCyan,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "claim_rewards_button"
                        )
                    }
                }
            }
        }
    }
}
