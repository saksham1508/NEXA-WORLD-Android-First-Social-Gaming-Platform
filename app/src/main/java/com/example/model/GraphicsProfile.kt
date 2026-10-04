package com.example.model

enum class GraphicsProfile(
    val title: String,
    val description: String,
    val targetFps: Int,
    val particleDensity: Float,
    val showGlowAndShadows: Boolean
) {
    LOW(
        title = "Low (Battery Saver)",
        description = "Optimized for budget phones. Capped at 30 FPS with streamlined visuals.",
        targetFps = 30,
        particleDensity = 0.3f,
        showGlowAndShadows = false
    ),
    MEDIUM(
        title = "Balanced (Standard)",
        description = "Smooth 60 FPS gameplay with balanced lighting and energy particles.",
        targetFps = 60,
        particleDensity = 0.7f,
        showGlowAndShadows = true
    ),
    HIGH(
        title = "Ultra (Maximum FX)",
        description = "Full 60 FPS fidelity, cyber lens flares, particle bursts, and dynamic trails.",
        targetFps = 60,
        particleDensity = 1.0f,
        showGlowAndShadows = true
    )
}
