package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GraphicsProfile
import com.example.ui.theme.HealthGreen
import com.example.ui.theme.NexaBgDark
import com.example.ui.theme.NexaBorderDark
import com.example.ui.theme.NexaCardDark
import com.example.ui.theme.NexaCardHover
import com.example.ui.theme.NexaCyan
import com.example.ui.theme.NexaGold
import com.example.ui.theme.NexaMagenta
import com.example.ui.theme.NexaSurfaceDark
import com.example.ui.theme.NexaTextMuted
import com.example.ui.theme.NexaTextPrimary
import com.example.ui.theme.NexaTextSecondary

@Composable
fun SettingsScreen(
    currentProfile: GraphicsProfile,
    onProfileChanged: (GraphicsProfile) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var sfxVolume by remember { mutableFloatStateOf(0.8f) }
    var bgmVolume by remember { mutableFloatStateOf(0.65f) }
    var hapticsEnabled by remember { mutableStateOf(true) }
    var safeChatFilterStrict by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBgDark)
            .statusBarsPadding()
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NexaCyan)
            }
            Text(
                text = "SETTINGS & SAFETY CENTER",
                color = NexaTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. GRAPHICS PROFILES
            item {
                Text(
                    text = "DEVICE GRAPHICS OPTIMIZATION",
                    color = NexaTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    GraphicsProfile.entries.forEach { profile ->
                        val isSelected = currentProfile == profile

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onProfileChanged(profile) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isSelected) NexaCardHover else NexaCardDark),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(
                                    listOf(if (isSelected) NexaCyan else NexaBorderDark, NexaBorderDark)
                                )
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = profile.title, color = NexaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (profile == GraphicsProfile.LOW) HealthGreen.copy(alpha = 0.2f) else NexaCyan.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "${profile.targetFps} FPS",
                                                color = if (profile == GraphicsProfile.LOW) HealthGreen else NexaCyan,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = profile.description, color = NexaTextMuted, fontSize = 11.sp)
                                }

                                if (isSelected) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NexaCyan)
                                }
                            }
                        }
                    }
                }
            }

            // 2. YOUTH SAFETY & PRIVACY CENTER
            item {
                Text(
                    text = "CHILD & YOUTH SAFETY CENTER",
                    color = NexaTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NexaSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(HealthGreen.copy(alpha = 0.5f), NexaBorderDark)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = HealthGreen)
                            Text(text = "Youth Protection Guidelines", color = NexaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Text(
                            text = "Nexa World is committed to a clean, non-predatory, and safe environment for young gamers (13-24). In-game voice is restricted to verified parties, and all open chat is monitored by automated real-time profanity and toxic phrase filters.",
                            color = NexaTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Strict Safe Chat Filter", color = NexaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Masks toxic and aggressive phrasing with ***", color = NexaTextMuted, fontSize = 10.sp)
                            }
                            Switch(
                                checked = safeChatFilterStrict,
                                onCheckedChange = { safeChatFilterStrict = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = NexaBgDark,
                                    checkedTrackColor = HealthGreen
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Haptic Vibration", color = NexaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Vibrate on blaster fire and dash", color = NexaTextMuted, fontSize = 10.sp)
                            }
                            Switch(
                                checked = hapticsEnabled,
                                onCheckedChange = { hapticsEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = NexaBgDark,
                                    checkedTrackColor = NexaCyan
                                )
                            )
                        }
                    }
                }
            }

            // 3. AUDIO CONTROLS
            item {
                Text(
                    text = "AUDIO & SOUND EFFECTS",
                    color = NexaTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NexaCardDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NexaBorderDark, NexaBorderDark)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Sound Effects (SFX)", color = NexaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "${(sfxVolume * 100).toInt()}%", color = NexaCyan, fontSize = 12.sp)
                            }
                            Slider(
                                value = sfxVolume,
                                onValueChange = { sfxVolume = it },
                                colors = SliderDefaults.colors(thumbColor = NexaCyan, activeTrackColor = NexaCyan)
                            )
                        }

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Music & Ambience (BGM)", color = NexaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "${(bgmVolume * 100).toInt()}%", color = NexaMagenta, fontSize = 12.sp)
                            }
                            Slider(
                                value = bgmVolume,
                                onValueChange = { bgmVolume = it },
                                colors = SliderDefaults.colors(thumbColor = NexaMagenta, activeTrackColor = NexaMagenta)
                            )
                        }
                    }
                }
            }

            // 4. APP & LEGAL METRICS
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NexaSurfaceDark)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Nexa World Client v1.0.0 (Release Build 101)", color = NexaTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Network Protocol: Authoritative WSS / UDP Gateway", color = NexaTextMuted, fontSize = 11.sp)
                        Text(text = "Zero Pay-to-Win Guarantee // Google Play Policy Compliant", color = NexaCyan, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
