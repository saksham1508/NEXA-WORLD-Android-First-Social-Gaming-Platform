package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AvatarConfig
import com.example.model.MatchmakingRegion
import com.example.ui.components.AvatarCanvas
import com.example.ui.components.CyberButton
import com.example.ui.theme.HealthGreen
import com.example.ui.theme.NexaBgDark
import com.example.ui.theme.NexaBorderDark
import com.example.ui.theme.NexaCardDark
import com.example.ui.theme.NexaCyan
import com.example.ui.theme.NexaGold
import com.example.ui.theme.NexaMagenta
import com.example.ui.theme.NexaPurple
import com.example.ui.theme.NexaSurfaceDark
import com.example.ui.theme.NexaTextMuted
import com.example.ui.theme.NexaTextPrimary
import com.example.ui.theme.NexaTextSecondary
import kotlinx.coroutines.delay

@Composable
fun MatchmakingScreen(
    avatarConfig: AvatarConfig,
    onMatchReady: () -> Unit,
    onCancel: () -> Unit
) {
    BackHandler { onCancel() }

    val regions = remember {
        listOf(
            MatchmakingRegion("in-bom", "Asia South (Mumbai)", "🇮🇳", 32),
            MatchmakingRegion("sg-sin", "Asia East (Singapore)", "🇸🇬", 48),
            MatchmakingRegion("eu-fra", "Europe (Frankfurt)", "🇪🇺", 125),
            MatchmakingRegion("us-iad", "US East (Virginia)", "🇺🇸", 185)
        )
    }

    var selectedRegion by remember { mutableStateOf(regions[0]) }
    var queueSeconds by remember { mutableIntStateOf(0) }
    var queuePhase by remember { mutableStateOf("Searching for balanced lobby...") }
    var playersFound by remember { mutableIntStateOf(1) }
    var matchFoundCountdown by remember { mutableIntStateOf(-1) }

    // Radar scanning rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val radarAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarAngle"
    )

    // Simulated matchmaking progress ticker
    LaunchedEffect(Unit) {
        while (matchFoundCountdown == -1) {
            delay(1000)
            queueSeconds++

            if (queueSeconds == 2) {
                playersFound = 3
                queuePhase = "Expanding skill bracket (Silver II - Gold I)..."
            } else if (queueSeconds == 4) {
                playersFound = 5
                queuePhase = "Selecting low-latency server (${selectedRegion.basePingMs}ms)..."
            } else if (queueSeconds == 6) {
                playersFound = 6
                queuePhase = "MATCH FOUND! Finalizing lobby..."
                matchFoundCountdown = 3
            }
        }

        // Countdown 3.. 2.. 1.. -> Start!
        while (matchFoundCountdown > 0) {
            delay(1000)
            matchFoundCountdown--
        }
        onMatchReady()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBgDark)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "MATCHMAKING QUEUE",
                    color = NexaTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Cyber Arena 3v3 // Casual & Ranked",
                    color = NexaCyan,
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel Queue",
                    tint = NexaTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // RADAR VISUALIZER WITH AVATAR IN CENTER
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
                .background(NexaSurfaceDark)
                .border(2.dp, NexaCyan.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Radar Circles & Sweep
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val r = size.width / 2f

                // Concentric Range Rings
                drawCircle(NexaBorderDark, r * 0.33f, Offset(cx, cy), style = Stroke(1.5f))
                drawCircle(NexaBorderDark, r * 0.66f, Offset(cx, cy), style = Stroke(1.5f))
                drawCircle(NexaCyan.copy(alpha = 0.5f), r - 4f, Offset(cx, cy), style = Stroke(2f))

                // Crosshairs
                drawLine(NexaBorderDark, Offset(cx, 0f), Offset(cx, size.height), 1f)
                drawLine(NexaBorderDark, Offset(0f, cy), Offset(size.width, cy), 1f)

                // Rotating Radar Beam
                val sweepAngleRad = Math.toRadians(radarAngle.toDouble())
                val beamEndX = cx + Math.cos(sweepAngleRad).toFloat() * r
                val beamEndY = cy + Math.sin(sweepAngleRad).toFloat() * r

                drawLine(
                    color = NexaCyan.copy(alpha = 0.85f),
                    start = Offset(cx, cy),
                    end = Offset(beamEndX, beamEndY),
                    strokeWidth = 3f
                )
            }

            // Central Player Avatar
            AvatarCanvas(
                config = avatarConfig,
                size = 110.dp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // STATUS TEXT & TIMER
        if (matchFoundCountdown > 0) {
            Text(
                text = "ENTERING ARENA IN",
                color = NexaGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "$matchFoundCountdown",
                color = NexaGold,
                fontSize = 44.sp,
                fontWeight = FontWeight.Black
            )
        } else {
            val minutes = queueSeconds / 60
            val seconds = queueSeconds % 60
            Text(
                text = "%02d:%02d".format(minutes, seconds),
                color = NexaTextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = queuePhase,
                color = NexaCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PLAYERS LOBBY COUNTER
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(NexaCardDark)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = NexaCyan,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Players Matched: $playersFound / 6",
                color = NexaTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // REGION SELECTOR
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "SELECT SERVER REGION",
                color = NexaTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                regions.forEach { region ->
                    val isSelected = selectedRegion.id == region.id
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NexaCardDark else NexaSurfaceDark)
                            .border(1.5.dp, if (isSelected) NexaCyan else NexaBorderDark, RoundedCornerShape(12.dp))
                            .clickable { selectedRegion = region }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = region.flagEmoji, fontSize = 18.sp)
                            Text(
                                text = "${region.basePingMs}ms",
                                color = if (region.basePingMs < 60) HealthGreen else NexaGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // CANCEL QUEUE BUTTON
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            CyberButton(
                text = "CANCEL MATCHMAKING",
                onClick = onCancel,
                glowColor = NexaBorderDark,
                textColor = NexaTextPrimary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
