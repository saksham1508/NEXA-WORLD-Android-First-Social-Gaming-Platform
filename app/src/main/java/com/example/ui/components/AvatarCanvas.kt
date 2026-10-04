package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.AvatarConfig

@Composable
fun AvatarCanvas(
    config: AvatarConfig,
    size: Dp = 160.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_bobbing")
    val bobbingOffset by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobbing"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val skinColor = parseColorSafely(config.skinToneHex, Color(0xFFF5D0A9))
    val hairColor = parseColorSafely(config.hairColorHex, Color(0xFF00E5FF))
    val eyeColor = parseColorSafely(config.eyeColorHex, Color(0xFFFF2A85))
    val outfitColor = parseColorSafely(config.outfitColorHex, Color(0xFF1E293B))
    val accentColor = parseColorSafely(config.accentColorHex, Color(0xFF00E5FF))

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val cx = this.size.width / 2f
            val cy = this.size.height / 2f + bobbingOffset

            // 1. Wings / Jetpack
            drawWings(config.wingsStyle, cx, cy, accentColor, glowAlpha)

            // 2. Body / Torso
            drawOutfit(config.outfitStyle, cx, cy, outfitColor, accentColor)

            // 3. Head & Face
            val headRadius = this.size.width * 0.22f
            val headCenterY = cy - headRadius * 0.5f

            // Head base
            drawCircle(
                color = skinColor,
                radius = headRadius,
                center = Offset(cx, headCenterY)
            )

            // Eyes
            val eyeOffsetX = headRadius * 0.42f
            val eyeOffsetY = headCenterY - headRadius * 0.05f
            val eyeRadius = headRadius * 0.16f

            // Left Eye
            drawCircle(Color.White, eyeRadius, Offset(cx - eyeOffsetX, eyeOffsetY))
            drawCircle(eyeColor, eyeRadius * 0.65f, Offset(cx - eyeOffsetX, eyeOffsetY))
            drawCircle(Color.Black, eyeRadius * 0.35f, Offset(cx - eyeOffsetX, eyeOffsetY))
            drawCircle(Color.White, eyeRadius * 0.18f, Offset(cx - eyeOffsetX - 1.5f, eyeOffsetY - 1.5f))

            // Right Eye
            drawCircle(Color.White, eyeRadius, Offset(cx + eyeOffsetX, eyeOffsetY))
            drawCircle(eyeColor, eyeRadius * 0.65f, Offset(cx + eyeOffsetX, eyeOffsetY))
            drawCircle(Color.Black, eyeRadius * 0.35f, Offset(cx + eyeOffsetX, eyeOffsetY))
            drawCircle(Color.White, eyeRadius * 0.18f, Offset(cx + eyeOffsetX - 1.5f, eyeOffsetY - 1.5f))

            // Cheerful Smile / Expression
            val mouthPath = Path().apply {
                moveTo(cx - headRadius * 0.22f, headCenterY + headRadius * 0.45f)
                quadraticTo(
                    cx, headCenterY + headRadius * 0.65f,
                    cx + headRadius * 0.22f, headCenterY + headRadius * 0.45f
                )
            }
            drawPath(mouthPath, Color(0xFF9E4751), style = Stroke(width = 3.5f))

            // 4. Hairstyle
            drawHair(config.hairStyle, cx, headCenterY, headRadius, hairColor, accentColor)

            // 5. Visor / Headgear
            drawVisor(config.visorStyle, cx, headCenterY, headRadius, accentColor, glowAlpha)

            // 6. Emote decoration
            if (config.emote == "Wave" || config.emote == "Victory") {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(accentColor.copy(alpha = 0.8f), Color.Transparent),
                        center = Offset(cx + headRadius * 1.1f, headCenterY - headRadius * 0.8f),
                        radius = 20f
                    ),
                    radius = 16f,
                    center = Offset(cx + headRadius * 1.1f, headCenterY - headRadius * 0.8f)
                )
            }
        }
    }
}

private fun DrawScope.drawWings(
    style: String,
    cx: Float,
    cy: Float,
    accentColor: Color,
    glowAlpha: Float
) {
    if (style == "None") return

    val wingSpan = size.width * 0.42f
    val wingPathLeft = Path().apply {
        moveTo(cx - 15f, cy + 10f)
        lineTo(cx - wingSpan, cy - 25f)
        lineTo(cx - wingSpan * 0.85f, cy + 20f)
        close()
    }
    val wingPathRight = Path().apply {
        moveTo(cx + 15f, cy + 10f)
        lineTo(cx + wingSpan, cy - 25f)
        lineTo(cx + wingSpan * 0.85f, cy + 20f)
        close()
    }

    val glowColor = accentColor.copy(alpha = if (style == "Energy Wings") glowAlpha else 0.7f)
    drawPath(wingPathLeft, glowColor, style = Fill)
    drawPath(wingPathRight, glowColor, style = Fill)

    drawPath(wingPathLeft, Color.White.copy(alpha = 0.8f), style = Stroke(width = 2f))
    drawPath(wingPathRight, Color.White.copy(alpha = 0.8f), style = Stroke(width = 2f))
}

private fun DrawScope.drawOutfit(
    style: String,
    cx: Float,
    cy: Float,
    outfitColor: Color,
    accentColor: Color
) {
    val bodyWidth = size.width * 0.48f
    val bodyHeight = size.height * 0.36f
    val bodyTop = cy + 10f

    // Torso Base
    val torsoPath = Path().apply {
        moveTo(cx - bodyWidth * 0.38f, bodyTop)
        lineTo(cx + bodyWidth * 0.38f, bodyTop)
        lineTo(cx + bodyWidth * 0.5f, bodyTop + bodyHeight)
        lineTo(cx - bodyWidth * 0.5f, bodyTop + bodyHeight)
        close()
    }
    drawPath(torsoPath, outfitColor)

    // Cyber Plating & Accents
    drawRoundRect(
        color = accentColor,
        topLeft = Offset(cx - bodyWidth * 0.22f, bodyTop + 8f),
        size = Size(bodyWidth * 0.44f, bodyHeight * 0.42f),
        cornerRadius = CornerRadius(6f, 6f),
        style = Stroke(width = 3f)
    )

    // Power Core Chest Jewel
    drawCircle(
        color = accentColor,
        radius = 8f,
        center = Offset(cx, bodyTop + bodyHeight * 0.32f)
    )
    drawCircle(
        color = Color.White,
        radius = 3.5f,
        center = Offset(cx, bodyTop + bodyHeight * 0.32f)
    )
}

private fun DrawScope.drawHair(
    style: String,
    cx: Float,
    cy: Float,
    headRadius: Float,
    hairColor: Color,
    accentColor: Color
) {
    when (style) {
        "Anime Fringe" -> {
            val hairPath = Path().apply {
                moveTo(cx - headRadius * 1.05f, cy - headRadius * 0.2f)
                lineTo(cx - headRadius * 0.8f, cy - headRadius * 1.15f)
                lineTo(cx - headRadius * 0.2f, cy - headRadius * 1.3f)
                lineTo(cx + headRadius * 0.4f, cy - headRadius * 1.25f)
                lineTo(cx + headRadius * 1.05f, cy - headRadius * 0.2f)
                lineTo(cx + headRadius * 0.6f, cy - headRadius * 0.4f)
                lineTo(cx + headRadius * 0.2f, cy - headRadius * 0.7f)
                lineTo(cx - headRadius * 0.2f, cy - headRadius * 0.5f)
                lineTo(cx - headRadius * 0.7f, cy - headRadius * 0.6f)
                close()
            }
            drawPath(hairPath, hairColor)
        }
        "Neon Dreads" -> {
            for (i in -3..3) {
                val dx = i * (headRadius * 0.28f)
                drawRoundRect(
                    color = if (i % 2 == 0) hairColor else accentColor,
                    topLeft = Offset(cx + dx - 6f, cy - headRadius * 1.2f),
                    size = Size(14f, headRadius * 1.4f),
                    cornerRadius = CornerRadius(7f, 7f)
                )
            }
        }
        "Sleek Bob" -> {
            val hairPath = Path().apply {
                moveTo(cx - headRadius * 1.15f, cy + headRadius * 0.4f)
                lineTo(cx - headRadius * 1.1f, cy - headRadius * 1.1f)
                lineTo(cx, cy - headRadius * 1.25f)
                lineTo(cx + headRadius * 1.1f, cy - headRadius * 1.1f)
                lineTo(cx + headRadius * 1.15f, cy + headRadius * 0.4f)
                lineTo(cx + headRadius * 0.85f, cy - headRadius * 0.2f)
                lineTo(cx, cy - headRadius * 0.6f)
                lineTo(cx - headRadius * 0.85f, cy - headRadius * 0.2f)
                close()
            }
            drawPath(hairPath, hairColor)
        }
        else -> { // "Cyber Spikes"
            val spikePath = Path().apply {
                moveTo(cx - headRadius * 1.1f, cy - headRadius * 0.3f)
                lineTo(cx - headRadius * 0.8f, cy - headRadius * 1.35f)
                lineTo(cx - headRadius * 0.3f, cy - headRadius * 0.9f)
                lineTo(cx, cy - headRadius * 1.5f)
                lineTo(cx + headRadius * 0.3f, cy - headRadius * 0.95f)
                lineTo(cx + headRadius * 0.85f, cy - headRadius * 1.3f)
                lineTo(cx + headRadius * 1.1f, cy - headRadius * 0.3f)
                lineTo(cx, cy - headRadius * 0.6f)
                close()
            }
            drawPath(spikePath, hairColor)
            drawPath(spikePath, accentColor.copy(alpha = 0.5f), style = Stroke(width = 2.5f))
        }
    }
}

private fun DrawScope.drawVisor(
    style: String,
    cx: Float,
    cy: Float,
    headRadius: Float,
    accentColor: Color,
    glowAlpha: Float
) {
    if (style == "None") return

    when (style) {
        "Holo Visor" -> {
            val visorWidth = headRadius * 1.6f
            val visorHeight = headRadius * 0.45f
            val visorY = cy - headRadius * 0.15f

            // Holographic Glass Visor
            drawRoundRect(
                color = accentColor.copy(alpha = 0.75f),
                topLeft = Offset(cx - visorWidth / 2f, visorY),
                size = Size(visorWidth, visorHeight),
                cornerRadius = CornerRadius(6f, 6f)
            )
            // Cyber glow scanline
            drawLine(
                color = Color.White.copy(alpha = glowAlpha),
                start = Offset(cx - visorWidth / 2f + 4f, visorY + visorHeight * 0.5f),
                end = Offset(cx + visorWidth / 2f - 4f, visorY + visorHeight * 0.5f),
                strokeWidth = 2.5f
            )
        }
        "Cyber Mask" -> {
            val maskWidth = headRadius * 1.4f
            val maskHeight = headRadius * 0.55f
            val maskY = cy + headRadius * 0.15f

            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(cx - maskWidth / 2f, maskY),
                size = Size(maskWidth, maskHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )
            drawCircle(
                color = accentColor,
                radius = 5f,
                center = Offset(cx, maskY + maskHeight * 0.5f)
            )
        }
        "VR Crown" -> {
            drawRoundRect(
                color = accentColor,
                topLeft = Offset(cx - headRadius * 0.9f, cy - headRadius * 0.95f),
                size = Size(headRadius * 1.8f, 16f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}

private fun parseColorSafely(hex: String, fallback: Color): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        when (cleanHex.length) {
            6 -> Color(android.graphics.Color.parseColor("#$cleanHex"))
            8 -> Color(android.graphics.Color.parseColor("#$cleanHex"))
            else -> fallback
        }
    } catch (_: Exception) {
        fallback
    }
}
