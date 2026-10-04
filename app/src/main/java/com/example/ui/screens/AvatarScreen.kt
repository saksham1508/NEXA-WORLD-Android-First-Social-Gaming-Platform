package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AvatarConfig
import com.example.ui.components.AvatarCanvas
import com.example.ui.components.CyberButton
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
fun AvatarScreen(
    currentConfig: AvatarConfig,
    onSaveAvatar: (AvatarConfig) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var draftConfig by remember { mutableStateOf(currentConfig) }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var saveFeedbackVisible by remember { mutableStateOf(false) }

    val categories = listOf("HAIRSTYLE", "OUTFIT", "VISOR", "WINGS", "SKIN & ACCENT", "EMOTE")

    val hairStyles = listOf("Cyber Spikes", "Anime Fringe", "Neon Dreads", "Sleek Bob")
    val outfitStyles = listOf("Nexus Scout", "Cyber Ninja", "Solar Knight", "Void Runner")
    val visorStyles = listOf("None", "Holo Visor", "Cyber Mask", "VR Crown")
    val wingsStyles = listOf("None", "Quantum Jets", "Energy Wings", "Neon Cape")
    val emotes = listOf("Ready", "Wave", "Victory", "Dance")

    val skinTones = listOf(
        "#F5D0A9" to "Light Warm",
        "#E0AC69" to "Medium Golden",
        "#C68642" to "Deep Bronze",
        "#8D5524" to "Rich Espresso",
        "#E2E8F0" to "Cyber Pale"
    )

    val neonPalettes = listOf(
        "#00E5FF" to "Cyber Cyan",
        "#FF2A85" to "Neon Magenta",
        "#8A2BE2" to "Hyper Violet",
        "#FFD700" to "Solar Gold",
        "#00E676" to "Emerald Lime",
        "#1E293B" to "Midnight Stealth"
    )

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
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NexaCyan
                )
            }
            Text(
                text = "AVATAR STYLIST",
                color = NexaTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }

        // LIVE AVATAR PREVIEW CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.radialGradient(
                        listOf(NexaCardDark, NexaSurfaceDark, NexaBgDark)
                    )
                )
                .border(1.dp, NexaBorderDark, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            AvatarCanvas(
                config = draftConfig,
                size = 200.dp
            )

            // Current Style Tags
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NexaBgDark.copy(alpha = 0.8f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = draftConfig.outfitStyle,
                        color = NexaCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NexaBgDark.copy(alpha = 0.8f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = draftConfig.hairStyle,
                        color = NexaMagenta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // CATEGORY TABS
        ScrollableTabRow(
            selectedTabIndex = selectedCategoryIndex,
            containerColor = NexaSurfaceDark,
            contentColor = NexaCyan,
            edgePadding = 16.dp,
            divider = {}
        ) {
            categories.forEachIndexed { index, cat ->
                Tab(
                    selected = selectedCategoryIndex == index,
                    onClick = { selectedCategoryIndex = index },
                    text = {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (selectedCategoryIndex == index) FontWeight.Black else FontWeight.Bold
                        )
                    }
                )
            }
        }

        // CUSTOMIZATION CHOICES
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedCategoryIndex) {
                0 -> { // HAIRSTYLE
                    item {
                        Text(text = "SELECT HAIRSTYLE", color = NexaTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            hairStyles.forEach { style ->
                                OptionChip(
                                    label = style,
                                    isSelected = draftConfig.hairStyle == style,
                                    onClick = { draftConfig = draftConfig.copy(hairStyle = style) }
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "HAIR COLOR", color = NexaTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(neonPalettes) { (hex, name) ->
                                ColorSwatch(
                                    colorHex = hex,
                                    name = name,
                                    isSelected = draftConfig.hairColorHex.equals(hex, ignoreCase = true),
                                    onClick = { draftConfig = draftConfig.copy(hairColorHex = hex) }
                                )
                            }
                        }
                    }
                }
                1 -> { // OUTFIT
                    item {
                        Text(text = "ARMOR & OUTFIT PLATING", color = NexaTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            outfitStyles.forEach { outfit ->
                                OptionCard(
                                    title = outfit,
                                    isSelected = draftConfig.outfitStyle == outfit,
                                    onClick = { draftConfig = draftConfig.copy(outfitStyle = outfit) }
                                )
                            }
                        }
                    }
                }
                2 -> { // VISOR
                    item {
                        Text(text = "CYBER VISOR & HEADGEAR", color = NexaTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            visorStyles.forEach { visor ->
                                OptionChip(
                                    label = visor,
                                    isSelected = draftConfig.visorStyle == visor,
                                    onClick = { draftConfig = draftConfig.copy(visorStyle = visor) }
                                )
                            }
                        }
                    }
                }
                3 -> { // WINGS
                    item {
                        Text(text = "JETPACKS & WINGS", color = NexaTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            wingsStyles.forEach { wing ->
                                OptionCard(
                                    title = wing,
                                    isSelected = draftConfig.wingsStyle == wing,
                                    onClick = { draftConfig = draftConfig.copy(wingsStyle = wing) }
                                )
                            }
                        }
                    }
                }
                4 -> { // SKIN & ACCENT
                    item {
                        Text(text = "SKIN TONE", color = NexaTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(skinTones) { (hex, name) ->
                                ColorSwatch(
                                    colorHex = hex,
                                    name = name,
                                    isSelected = draftConfig.skinToneHex.equals(hex, ignoreCase = true),
                                    onClick = { draftConfig = draftConfig.copy(skinToneHex = hex) }
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(text = "ACCENT & ENERGY GLOW COLOR", color = NexaTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(neonPalettes) { (hex, name) ->
                                ColorSwatch(
                                    colorHex = hex,
                                    name = name,
                                    isSelected = draftConfig.accentColorHex.equals(hex, ignoreCase = true),
                                    onClick = { draftConfig = draftConfig.copy(accentColorHex = hex) }
                                )
                            }
                        }
                    }
                }
                5 -> { // EMOTES
                    item {
                        Text(text = "SIGNATURE EMOTE & ANIMATION", color = NexaTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            emotes.forEach { emote ->
                                OptionChip(
                                    label = emote,
                                    isSelected = draftConfig.emote == emote,
                                    onClick = { draftConfig = draftConfig.copy(emote = emote) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // SAVE BUTTON FIXED FOOTER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            CyberButton(
                text = "SAVE & EQUIP AVATAR",
                onClick = {
                    onSaveAvatar(draftConfig)
                    onBack()
                },
                glowColor = NexaCyan,
                modifier = Modifier.fillMaxWidth(),
                testTag = "save_avatar_button"
            )
        }
    }
}

@Composable
fun OptionChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) NexaCyan else NexaBorderDark
    val bgColor = if (isSelected) NexaCyan.copy(alpha = 0.2f) else NexaCardDark

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) NexaCyan else NexaTextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun OptionCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = NexaTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = NexaCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ColorSwatch(
    colorHex: String,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (_: Exception) {
        Color.White
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) Color.White else NexaBorderDark,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = if (colorHex == "#E2E8F0" || colorHex == "#FFD700" || colorHex == "#00E5FF") Color.Black else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            color = if (isSelected) NexaTextPrimary else NexaTextMuted,
            fontSize = 10.sp
        )
    }
}
