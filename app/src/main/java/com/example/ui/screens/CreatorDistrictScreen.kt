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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.local.entity.CustomMapEntity
import com.example.ui.components.CyberButton
import com.example.ui.theme.EnergyCoreColor
import com.example.ui.theme.HealthGreen
import com.example.ui.theme.HealthRed
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

enum class GridCellType(val label: String, val color: Color) {
    EMPTY("Empty", Color.Transparent),
    BARRIER("Cyber Wall", NexaBorderDark),
    ENERGY_SPAWNER("Core Node", EnergyCoreColor),
    HAZARD_TURRET("Laser Hazard", HealthRed)
}

@Composable
fun CreatorDistrictScreen(
    customMaps: List<CustomMapEntity>,
    onSaveMap: (CustomMapEntity) -> Unit,
    onPlayMap: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var mapTitle by remember { mutableStateOf("My Cyber Arena") }
    var selectedTool by remember { mutableStateOf(GridCellType.BARRIER) }

    // 6x6 Grid State (0 to 35)
    val gridCells = remember {
        mutableStateListOf<GridCellType>().apply {
            repeat(36) { add(GridCellType.EMPTY) }
            // Preset some initial obstacles
            this[14] = GridCellType.BARRIER
            this[15] = GridCellType.BARRIER
            this[20] = GridCellType.BARRIER
            this[21] = GridCellType.BARRIER
            this[9] = GridCellType.ENERGY_SPAWNER
            this[26] = GridCellType.ENERGY_SPAWNER
        }
    }

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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NexaCyan)
                }
                Column {
                    Text(
                        text = "CREATOR DISTRICT",
                        color = NexaTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "User-Generated Arena Foundry",
                        color = NexaPurple,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(NexaPurple.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "SANDBOX", color = NexaPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. ARENA EDITOR CARD
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NexaSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NexaPurple, NexaCyan)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = mapTitle,
                            onValueChange = { mapTitle = it },
                            label = { Text("Arena Title", color = NexaTextSecondary, fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NexaCyan,
                                unfocusedBorderColor = NexaBorderDark,
                                focusedTextColor = NexaTextPrimary,
                                unfocusedTextColor = NexaTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tool Palettes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            GridCellType.entries.forEach { type ->
                                val isSelected = selectedTool == type
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) NexaCardHover else NexaCardDark)
                                        .border(1.dp, if (isSelected) NexaCyan else NexaBorderDark, RoundedCornerShape(10.dp))
                                        .clickable { selectedTool = type }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = type.label,
                                        color = if (isSelected) NexaCyan else NexaTextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 6x6 GRID MATRIX
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(NexaBgDark)
                                .border(1.dp, NexaBorderDark, RoundedCornerShape(14.dp))
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            for (row in 0 until 6) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    for (col in 0 until 6) {
                                        val idx = row * 6 + col
                                        val cellType = gridCells[idx]

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(
                                                    when (cellType) {
                                                        GridCellType.BARRIER -> NexaBorderDark
                                                        GridCellType.ENERGY_SPAWNER -> EnergyCoreColor.copy(alpha = 0.35f)
                                                        GridCellType.HAZARD_TURRET -> HealthRed.copy(alpha = 0.35f)
                                                        GridCellType.EMPTY -> NexaCardDark
                                                    }
                                                )
                                                .border(
                                                    0.5.dp,
                                                    if (cellType != GridCellType.EMPTY) cellType.color else NexaBorderDark.copy(alpha = 0.3f),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .clickable {
                                                    gridCells[idx] = if (gridCells[idx] == selectedTool) GridCellType.EMPTY else selectedTool
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            when (cellType) {
                                                GridCellType.ENERGY_SPAWNER -> Icon(
                                                    imageVector = Icons.Default.Bolt,
                                                    contentDescription = null,
                                                    tint = EnergyCoreColor,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                GridCellType.HAZARD_TURRET -> Icon(
                                                    imageVector = Icons.Default.Warning,
                                                    contentDescription = null,
                                                    tint = HealthRed,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                GridCellType.BARRIER -> Box(
                                                    modifier = Modifier
                                                        .size(12.dp)
                                                        .background(NexaCyan.copy(alpha = 0.8f))
                                                )
                                                else -> {}
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // ACTION BUTTONS
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NexaCardDark)
                                    .clickable {
                                        for (i in 0 until 36) gridCells[i] = GridCellType.EMPTY
                                    }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("CLEAR", color = NexaTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Box(
                                modifier = Modifier
                                    .weight(2f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NexaCyan)
                                    .clickable {
                                        val obstacles = mutableListOf<String>()
                                        val spawners = mutableListOf<String>()
                                        for (i in 0 until 36) {
                                            val r = i / 6
                                            val c = i % 6
                                            if (gridCells[i] == GridCellType.BARRIER) obstacles.add("$c:$r")
                                            if (gridCells[i] == GridCellType.ENERGY_SPAWNER) spawners.add("$c:$r")
                                        }
                                        val newMap = CustomMapEntity(
                                            id = "map_${System.currentTimeMillis()}",
                                            title = mapTitle.ifEmpty { "Custom Arena" },
                                            creatorName = "ApexCipher",
                                            likesCount = 1,
                                            playsCount = 0,
                                            obstacleData = obstacles.joinToString(","),
                                            energySpawnerData = spawners.joinToString(",")
                                        )
                                        onSaveMap(newMap)
                                    }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("SAVE & PUBLISH", color = NexaBgDark, fontSize = 12.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }

            // 2. COMMUNITY CURATED MAPS
            item {
                Text(
                    text = "COMMUNITY POPULAR ARENAS",
                    color = NexaTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            items(customMaps) { map ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NexaCardDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NexaBorderDark, NexaBorderDark)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = map.title, color = NexaTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "By ${map.creatorName}", color = NexaCyan, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = NexaMagenta, modifier = Modifier.size(12.dp))
                                    Text(text = "${map.likesCount}", color = NexaTextSecondary, fontSize = 10.sp)
                                }
                                Text(text = "${map.playsCount} matches played", color = NexaTextMuted, fontSize = 10.sp)
                            }
                        }

                        CyberButton(
                            text = "PLAY",
                            onClick = onPlayMap,
                            icon = Icons.Default.PlayArrow,
                            glowColor = NexaCyan,
                            modifier = Modifier.width(90.dp)
                        )
                    }
                }
            }
        }
    }
}
