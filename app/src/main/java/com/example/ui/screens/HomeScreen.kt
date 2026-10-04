package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.DailyMissionEntity
import com.example.data.local.entity.FriendEntity
import com.example.data.local.entity.UserEntity
import com.example.model.AvatarConfig
import com.example.ui.components.AvatarCanvas
import com.example.ui.components.CyberButton
import com.example.ui.components.TopBarHud
import com.example.ui.theme.EnergyCoreColor
import com.example.ui.theme.HealthGreen
import com.example.ui.theme.NexaBgDark
import com.example.ui.theme.NexaBorderDark
import com.example.ui.theme.NexaCardDark
import com.example.ui.theme.NexaCardHover
import com.example.ui.theme.NexaCyan
import com.example.ui.theme.NexaGold
import com.example.ui.theme.NexaMagenta
import com.example.ui.theme.NexaPurple
import com.example.ui.theme.NexaSurfaceDark
import com.example.ui.theme.NexaTextMuted
import com.example.ui.theme.NexaTextPrimary
import com.example.ui.theme.NexaTextSecondary

@Composable
fun HomeScreen(
    user: UserEntity?,
    avatarConfig: AvatarConfig,
    missions: List<DailyMissionEntity>,
    friends: List<FriendEntity>,
    onQuickPlay: () -> Unit,
    onNavigateToAvatar: () -> Unit,
    onNavigateToSocial: () -> Unit,
    onNavigateToBattlePass: () -> Unit,
    onNavigateToCreatorDistrict: () -> Unit,
    onClaimMission: (missionId: String, coins: Long, xp: Int) -> Unit
) {
    val level = user?.level ?: 1
    val coins = user?.nexCoins ?: 1000L
    val gems = user?.nexGems ?: 50
    val rankTier = user?.rankTier ?: "Silver III"
    val title = user?.selectedTitle ?: "Cyber Pioneer"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBgDark)
    ) {
        // Top HUD
        TopBarHud(level = level, coins = coins, gems = gems)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // 1. HERO BANNER & AVATAR SHOWCASE
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    // Background Image
                    Image(
                        painter = painterResource(id = R.drawable.img_nexus_hero_banner),
                        contentDescription = "Nexus City",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Cyber Gradient Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        NexaBgDark.copy(alpha = 0.95f),
                                        NexaBgDark.copy(alpha = 0.65f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Hero Content Row
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            // Season Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NexaMagenta.copy(alpha = 0.25f))
                                    .border(1.dp, NexaMagenta, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "SEASON 01 // CYBER GENESIS",
                                    color = NexaMagenta,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "NEXA WORLD",
                                color = NexaTextPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )

                            Text(
                                text = "Nexus City Social Hub",
                                color = NexaCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(HealthGreen)
                                )
                                Text(
                                    text = "14,820 Citizens Online",
                                    color = NexaTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Avatar Preview with customize overlay
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(NexaCardDark.copy(alpha = 0.6f))
                                .border(1.dp, NexaCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                                .clickable(onClick = onNavigateToAvatar)
                                .padding(8.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            AvatarCanvas(config = avatarConfig, size = 110.dp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NexaCyan)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "WARDROBE",
                                    color = NexaBgDark,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // 2. PRIMARY ACTION: QUICK PLAY CTA
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    CyberButton(
                        text = "QUICK BATTLE (CYBER ARENA 3v3)",
                        onClick = onQuickPlay,
                        icon = Icons.Default.SportsEsports,
                        glowColor = NexaCyan,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "home_quick_play_button"
                    )
                }
            }

            // 3. PLAYER STATS MINI-CARD
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NexaSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NexaBorderDark, NexaCyan.copy(alpha = 0.3f))))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = user?.username ?: "ApexCipher",
                                color = NexaTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = title,
                                color = NexaCyan,
                                fontSize = 12.sp
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = rankTier, color = NexaGold, fontSize = 14.sp, fontWeight = FontWeight.Black)
                                Text(text = "Rank", color = NexaTextMuted, fontSize = 10.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "${user?.matchesWon ?: 0}W", color = HealthGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Victories", color = NexaTextMuted, fontSize = 10.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "${user?.energyBanked ?: 0}", color = EnergyCoreColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Cores", color = NexaTextMuted, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            // 4. DISTRICTS CAROUSEL
            item {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "EXPLORE NEXUS DISTRICTS",
                        color = NexaTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            DistrictCard(
                                title = "Cyber Arena",
                                subtitle = "3v3 Energy Core Clash",
                                color = NexaCyan,
                                icon = Icons.Default.Bolt,
                                onClick = onQuickPlay
                            )
                        }
                        item {
                            DistrictCard(
                                title = "Creator Foundry",
                                subtitle = "Build & Test UGC Arenas",
                                color = NexaPurple,
                                icon = Icons.Default.Build,
                                onClick = onNavigateToCreatorDistrict
                            )
                        }
                        item {
                            DistrictCard(
                                title = "Avatar Boutique",
                                subtitle = "Wardrobe & Cyber Styles",
                                color = NexaMagenta,
                                icon = Icons.Default.Checkroom,
                                onClick = onNavigateToAvatar
                            )
                        }
                        item {
                            DistrictCard(
                                title = "Squad Cantina",
                                subtitle = "Party Up & Safe Chat",
                                color = NexaGold,
                                icon = Icons.Default.Group,
                                onClick = onNavigateToSocial
                            )
                        }
                    }
                }
            }

            // 5. DAILY MISSIONS
            item {
                Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DAILY MISSIONS",
                            color = NexaTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Resets in 14h",
                            color = NexaTextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    missions.forEach { mission ->
                        MissionRow(mission = mission, onClaim = onClaimMission)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DistrictCard(
    title: String,
    subtitle: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .height(115.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NexaCardDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(color.copy(alpha = 0.5f), NexaBorderDark)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    color = NexaTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = NexaTextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun MissionRow(
    mission: DailyMissionEntity,
    onClaim: (missionId: String, coins: Long, xp: Int) -> Unit
) {
    val progress = (mission.currentProgress.toFloat() / mission.targetGoal.toFloat()).coerceIn(0f, 1f)
    val isCompleted = mission.currentProgress >= mission.targetGoal

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NexaSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(if (isCompleted && !mission.isClaimed) NexaGold.copy(alpha = 0.6f) else NexaBorderDark, NexaBorderDark)
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mission.title,
                    color = NexaTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = mission.description,
                    color = NexaTextMuted,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (isCompleted) HealthGreen else NexaCyan,
                        trackColor = NexaCardDark
                    )
                    Text(
                        text = "${mission.currentProgress}/${mission.targetGoal}",
                        color = NexaTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Reward / Claim button
            if (mission.isClaimed) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NexaCardDark)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "CLAIMED", color = NexaTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            } else if (isCompleted) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(NexaGold)
                        .clickable { onClaim(mission.id, mission.rewardCoins, mission.rewardXp) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(text = "CLAIM +${mission.rewardCoins}", color = NexaBgDark, fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "+${mission.rewardCoins}C", color = NexaGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
