package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EnergyCoreColor
import com.example.ui.theme.HealthGreen
import com.example.ui.theme.HealthRed
import com.example.ui.theme.NexaBgDark
import com.example.ui.theme.NexaBorderDark
import com.example.ui.theme.NexaCardDark
import com.example.ui.theme.NexaCyan
import com.example.ui.theme.NexaGold
import com.example.ui.theme.NexaMagenta
import com.example.ui.theme.NexaOrange
import com.example.ui.theme.NexaPurple
import com.example.ui.theme.NexaTextMuted
import com.example.ui.theme.NexaTextPrimary
import com.example.ui.theme.NexaTextSecondary

@Composable
fun CurrencyPill(
    icon: ImageVector,
    amount: String,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(NexaCardDark.copy(alpha = 0.85f))
            .border(1.dp, iconTint.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = amount,
            color = NexaTextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TopBarHud(
    level: Int,
    coins: Long,
    gems: Int,
    pingMs: Int = 42,
    isReconnecting: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Player Level Pill
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(NexaCardDark.copy(alpha = 0.9f))
                .border(1.dp, NexaCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(NexaCyan),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$level",
                    color = NexaBgDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Text(
                text = "LVL",
                color = NexaCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Currencies
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CurrencyPill(
                icon = Icons.Default.MonetizationOn,
                amount = if (coins > 9999) "${coins / 1000}k" else "$coins",
                iconTint = NexaGold
            )
            CurrencyPill(
                icon = Icons.Default.Diamond,
                amount = "$gems",
                iconTint = NexaMagenta
            )
        }

        // Network telemetry pill
        NetworkStatusPill(pingMs = pingMs, isReconnecting = isReconnecting)
    }
}

@Composable
fun NetworkStatusPill(
    pingMs: Int,
    isReconnecting: Boolean = false,
    modifier: Modifier = Modifier
) {
    val statusColor = when {
        isReconnecting -> HealthRed
        pingMs < 60 -> HealthGreen
        pingMs < 130 -> NexaOrange
        else -> HealthRed
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(NexaCardDark.copy(alpha = 0.8f))
            .border(1.dp, statusColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(statusColor)
        )
        Text(
            text = if (isReconnecting) "RECONNECTING" else "${pingMs}ms",
            color = statusColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CyberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    glowColor: Color = NexaCyan,
    textColor: Color = NexaBgDark,
    enabled: Boolean = true,
    testTag: String = "cyber_button"
) {
    val buttonBackground = if (enabled) {
        Brush.horizontalGradient(
            listOf(glowColor, glowColor.copy(alpha = 0.85f))
        )
    } else {
        Brush.horizontalGradient(
            listOf(NexaBorderDark, NexaBorderDark)
        )
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .shadow(if (enabled) 12.dp else 0.dp, RoundedCornerShape(14.dp), spotColor = glowColor)
            .clip(RoundedCornerShape(14.dp))
            .background(buttonBackground)
            .border(1.dp, Color.White.copy(alpha = if (enabled) 0.4f else 0.1f), RoundedCornerShape(14.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) textColor else NexaTextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = text,
                color = if (enabled) textColor else NexaTextMuted,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun RarityBadge(rarity: String, modifier: Modifier = Modifier) {
    val (color, text) = when (rarity.uppercase()) {
        "LEGENDARY" -> NexaGold to "LEGENDARY"
        "EPIC" -> NexaPurple to "EPIC"
        "RARE" -> NexaCyan to "RARE"
        else -> NexaTextMuted to "COMMON"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.18f))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
    }
}
