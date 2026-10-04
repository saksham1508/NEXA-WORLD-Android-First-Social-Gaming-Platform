package com.example.model

data class AvatarConfig(
    val skinToneHex: String = "#F5D0A9",
    val hairStyle: String = "Cyber Spikes", // "Cyber Spikes", "Anime Fringe", "Neon Dreads", "Sleek Bob"
    val hairColorHex: String = "#00E5FF",
    val eyeColorHex: String = "#FF2A85",
    val outfitStyle: String = "Nexus Scout", // "Nexus Scout", "Cyber Ninja", "Solar Knight", "Void Runner"
    val outfitColorHex: String = "#1E293B",
    val accentColorHex: String = "#00E5FF",
    val visorStyle: String = "Holo Visor", // "None", "Holo Visor", "Cyber Mask", "VR Crown"
    val wingsStyle: String = "Quantum Jets", // "None", "Quantum Jets", "Energy Wings", "Neon Cape"
    val emote: String = "Ready" // "Ready", "Wave", "Victory", "Dance"
)
