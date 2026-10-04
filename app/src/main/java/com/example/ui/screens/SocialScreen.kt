package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.example.data.local.entity.FriendEntity
import com.example.model.AvatarConfig
import com.example.ui.components.AvatarCanvas
import com.example.ui.components.CyberButton
import com.example.ui.theme.HealthGreen
import com.example.ui.theme.HealthRed
import com.example.ui.theme.NexaBgDark
import com.example.ui.theme.NexaBorderDark
import com.example.ui.theme.NexaCardDark
import com.example.ui.theme.NexaCardHover
import com.example.ui.theme.NexaCyan
import com.example.ui.theme.NexaGold
import com.example.ui.theme.NexaMagenta
import com.example.ui.theme.NexaOrange
import com.example.ui.theme.NexaPurple
import com.example.ui.theme.NexaSurfaceDark
import com.example.ui.theme.NexaTextMuted
import com.example.ui.theme.NexaTextPrimary
import com.example.ui.theme.NexaTextSecondary

data class ChatMessage(
    val sender: String,
    val text: String,
    val isSystem: Boolean = false,
    val isQuickPreset: Boolean = false
)

@Composable
fun SocialScreen(
    avatarConfig: AvatarConfig,
    friends: List<FriendEntity>,
    onToggleSquad: (friendId: String, currentStatus: Boolean) -> Unit,
    onReportPlayer: (friendId: String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("SQUAD FIRETEAM", "FRIENDS", "SAFE CHAT")

    var reportingFriend by remember { mutableStateOf<FriendEntity?>(null) }
    var reportReason by remember { mutableStateOf("Toxic Behavior") }

    val chatMessages = remember {
        mutableStateListOf(
            ChatMessage("System", "Welcome to Nexus City Safe Cantina. Youth Protection Filter active.", isSystem = true),
            ChatMessage("Valkyrie_99", "Awesome match earlier! Ready for another Cyber Arena round?", isQuickPreset = true),
            ChatMessage("ShadowZero", "Let's party up! Heading to Creator Foundry.", isQuickPreset = true)
        )
    }

    var customMessageInput by remember { mutableStateOf("") }

    val quickPhrases = listOf(
        "Let's party up for Cyber Arena!",
        "Need backup at Blue Conduit!",
        "Awesome avatar style!",
        "GG squad! Well played!",
        "Heading to Nexus Central Plaza."
    )

    fun sendSafeMessage(text: String, isQuick: Boolean = false) {
        val sanitized = text.trim()
        if (sanitized.isEmpty()) return

        // Safety profanity filter
        val forbidden = listOf("hate", "kill", "dumb", "stupid", "idiot", "trash")
        var isFiltered = false
        var safeText = sanitized
        for (badWord in forbidden) {
            if (safeText.contains(badWord, ignoreCase = true)) {
                safeText = safeText.replace(Regex("(?i)$badWord"), "***")
                isFiltered = true
            }
        }

        chatMessages.add(
            ChatMessage(
                sender = "ApexCipher",
                text = safeText,
                isQuickPreset = isQuick
            )
        )
        customMessageInput = ""
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
            Column {
                Text(
                    text = "SOCIAL & SQUADS",
                    color = NexaTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Safe Community & Youth Protected",
                    color = HealthGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(HealthGreen.copy(alpha = 0.2f))
                    .border(1.dp, HealthGreen.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = HealthGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "SAFE MODE",
                        color = HealthGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // TABS
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = NexaSurfaceDark,
            contentColor = NexaCyan,
            divider = {}
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = tabTitle,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Black else FontWeight.Bold
                        )
                    }
                )
            }
        }

        // TAB CONTENT
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> { // SQUAD FIRETEAM
                    val squadMembers = friends.filter { it.isInSquad }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentPadding = PaddingValues(bottom = 90.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "SQUAD LOBBY (1-4 PLAYERS)",
                                color = NexaTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Slot 1: Player (Squad Leader)
                        item {
                            SquadMemberCard(
                                name = "ApexCipher (Leader)",
                                rank = "Silver III",
                                isReady = true,
                                isPlayer = true,
                                avatarConfig = avatarConfig,
                                onAction = {}
                            )
                        }

                        // Slot 2: Friends in Squad
                        items(squadMembers) { member ->
                            SquadMemberCard(
                                name = member.username,
                                rank = member.rankTier,
                                isReady = true,
                                isPlayer = false,
                                avatarConfig = avatarConfig.copy(hairStyle = member.avatarHair),
                                onAction = { onToggleSquad(member.id, true) }
                            )
                        }

                        // Empty Slots
                        val emptySlots = (3 - squadMembers.size).coerceAtLeast(0)
                        items(emptySlots) {
                            EmptySquadSlot(onInvite = { selectedTab = 1 })
                        }
                    }
                }
                1 -> { // FRIENDS LIST
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentPadding = PaddingValues(bottom = 90.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(friends) { friend ->
                            FriendRow(
                                friend = friend,
                                onToggleSquad = { onToggleSquad(friend.id, friend.isInSquad) },
                                onReport = { reportingFriend = friend }
                            )
                        }
                    }
                }
                2 -> { // SAFE CHAT
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        // Quick Tactical Presets Carousel
                        Text(
                            text = "QUICK TACTICAL PRESETS",
                            color = NexaTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(quickPhrases) { phrase ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(NexaCardDark)
                                        .border(1.dp, NexaCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                        .clickable { sendSafeMessage(phrase, isQuick = true) }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = phrase,
                                        color = NexaCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Message Stream
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(NexaSurfaceDark)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(chatMessages) { msg ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (msg.isSystem) NexaBgDark
                                            else if (msg.sender == "ApexCipher") NexaCyan.copy(alpha = 0.15f)
                                            else NexaCardDark
                                        )
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = msg.sender,
                                            color = if (msg.isSystem) NexaGold else NexaCyan,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                        if (msg.isQuickPreset) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(NexaPurple.copy(alpha = 0.3f))
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text(text = "PRESET", color = NexaPurple, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = msg.text,
                                        color = NexaTextPrimary,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Filtered Custom Input Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .padding(bottom = 75.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = customMessageInput,
                                onValueChange = { customMessageInput = it },
                                placeholder = { Text("Send safe message...", color = NexaTextMuted, fontSize = 13.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("chat_message_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NexaCyan,
                                    unfocusedBorderColor = NexaBorderDark,
                                    focusedContainerColor = NexaCardDark,
                                    unfocusedContainerColor = NexaCardDark,
                                    focusedTextColor = NexaTextPrimary,
                                    unfocusedTextColor = NexaTextPrimary
                                ),
                                shape = RoundedCornerShape(14.dp),
                                singleLine = true
                            )

                            IconButton(
                                onClick = { sendSafeMessage(customMessageInput) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(NexaCyan)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = NexaBgDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // REPORT PLAYER MODAL
        AnimatedVisibility(visible = reportingFriend != null) {
            reportingFriend?.let { target ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = NexaSurfaceDark),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(HealthRed, NexaBorderDark)))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = HealthRed)
                                Text(
                                    text = "REPORT / BLOCK PLAYER",
                                    color = HealthRed,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Reporting: ${target.username} (${target.tag})",
                                color = NexaTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(text = "Select violation reason:", color = NexaTextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(6.dp))

                            val reasons = listOf("Toxic Behavior / Chat", "Inappropriate Name", "Suspicious Cheating", "Unsportsmanlike Griefing")
                            reasons.forEach { r ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (reportReason == r) NexaCardHover else NexaCardDark)
                                        .clickable { reportReason = r }
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = r,
                                        color = if (reportReason == r) NexaCyan else NexaTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = if (reportReason == r) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(NexaCardDark)
                                        .clickable { reportingFriend = null }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("CANCEL", color = NexaTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(HealthRed)
                                        .clickable {
                                            onReportPlayer(target.id)
                                            reportingFriend = null
                                        }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("SUBMIT & BLOCK", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SquadMemberCard(
    name: String,
    rank: String,
    isReady: Boolean,
    isPlayer: Boolean,
    avatarConfig: AvatarConfig,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NexaCardDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NexaCyan.copy(alpha = 0.4f), NexaBorderDark)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarCanvas(config = avatarConfig, size = 52.dp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, color = NexaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = rank, color = NexaCyan, fontSize = 11.sp)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(HealthGreen.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "READY", color = HealthGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            if (!isPlayer) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onAction) {
                    Icon(imageVector = Icons.Default.Block, contentDescription = "Remove", tint = NexaTextMuted, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun EmptySquadSlot(onInvite: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(NexaSurfaceDark)
            .border(1.dp, NexaBorderDark, RoundedCornerShape(16.dp))
            .clickable(onClick = onInvite)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = NexaCyan, modifier = Modifier.size(18.dp))
            Text(text = "INVITE FRIEND TO FIRETEAM", color = NexaCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FriendRow(
    friend: FriendEntity,
    onToggleSquad: () -> Unit,
    onReport: () -> Unit
) {
    val statusColor = when (friend.status) {
        "ONLINE_HUB" -> HealthGreen
        "IN_MATCH" -> NexaOrange
        else -> NexaTextMuted
    }
    val statusLabel = when (friend.status) {
        "ONLINE_HUB" -> "Nexus Central Hub"
        "IN_MATCH" -> "In Cyber Arena"
        else -> "Offline"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NexaCardDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NexaBorderDark, NexaBorderDark)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(NexaSurfaceDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = NexaCyan)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${friend.username} ${friend.tag}", color = NexaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(statusColor))
                    Text(text = statusLabel, color = statusColor, fontSize = 10.sp)
                }
            }

            // Quick Squad Toggle
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (friend.isInSquad) NexaMagenta.copy(alpha = 0.2f) else NexaCyan.copy(alpha = 0.2f))
                    .border(1.dp, if (friend.isInSquad) NexaMagenta else NexaCyan, RoundedCornerShape(8.dp))
                    .clickable(onClick = onToggleSquad)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (friend.isInSquad) "IN SQUAD" else "+ SQUAD",
                    color = if (friend.isInSquad) NexaMagenta else NexaCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(onClick = onReport) {
                Icon(imageVector = Icons.Default.Flag, contentDescription = "Report", tint = NexaTextMuted, modifier = Modifier.size(16.dp))
            }
        }
    }
}
