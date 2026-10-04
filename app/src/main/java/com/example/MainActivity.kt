package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.NexaDatabase
import com.example.data.repository.NexaRepository
import com.example.ui.screens.AvatarScreen
import com.example.ui.screens.BattlePassScreen
import com.example.ui.screens.CreatorDistrictScreen
import com.example.ui.screens.CyberArenaScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MatchmakingScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SocialScreen
import com.example.ui.theme.MyApplicationTheme
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
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.NexaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NexaWorldApp()
            }
        }
    }
}

@Composable
fun NexaWorldApp() {
    val context = LocalContext.current
    val repository = remember {
        val db = NexaDatabase.getInstance(context)
        NexaRepository(db)
    }

    val viewModel: NexaViewModel = viewModel { NexaViewModel(repository) }

    val user by viewModel.user.collectAsStateWithLifecycle()
    val avatarConfig by viewModel.avatarConfig.collectAsStateWithLifecycle()
    val missions by viewModel.missions.collectAsStateWithLifecycle()
    val friends by viewModel.friends.collectAsStateWithLifecycle()
    val battlePassTiers by viewModel.battlePassTiers.collectAsStateWithLifecycle()
    val customMaps by viewModel.customMaps.collectAsStateWithLifecycle()
    val graphicsProfile by viewModel.graphicsProfile.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = NexaBgDark,
        bottomBar = {
            // Hide bottom bar during match or active matchmaking to provide full immersive arena HUD
            if (currentScreen != AppScreen.CYBER_ARENA && currentScreen != AppScreen.MATCHMAKING) {
                NexaBottomNav(
                    currentScreen = currentScreen,
                    onScreenSelected = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.HOME -> HomeScreen(
                        user = user,
                        avatarConfig = avatarConfig,
                        missions = missions,
                        friends = friends,
                        onQuickPlay = { viewModel.navigateTo(AppScreen.MATCHMAKING) },
                        onNavigateToAvatar = { viewModel.navigateTo(AppScreen.AVATAR_CUSTOMIZER) },
                        onNavigateToSocial = { viewModel.navigateTo(AppScreen.SOCIAL) },
                        onNavigateToBattlePass = { viewModel.navigateTo(AppScreen.BATTLE_PASS) },
                        onNavigateToCreatorDistrict = { viewModel.navigateTo(AppScreen.CREATOR_DISTRICT) },
                        onClaimMission = { id, coins, xp -> viewModel.claimMission(id, coins, xp) }
                    )

                    AppScreen.AVATAR_CUSTOMIZER -> AvatarScreen(
                        currentConfig = avatarConfig,
                        onSaveAvatar = { viewModel.saveAvatar(it) },
                        onBack = { viewModel.navigateBack() }
                    )

                    AppScreen.MATCHMAKING -> MatchmakingScreen(
                        avatarConfig = avatarConfig,
                        onMatchReady = { viewModel.navigateTo(AppScreen.CYBER_ARENA) },
                        onCancel = { viewModel.navigateBack() }
                    )

                    AppScreen.CYBER_ARENA -> CyberArenaScreen(
                        avatarConfig = avatarConfig,
                        graphicsProfile = graphicsProfile,
                        onMatchFinished = { won, kills, energy ->
                            viewModel.recordMatchFinished(won, kills, energy)
                        },
                        onExitArena = { viewModel.navigateTo(AppScreen.HOME) }
                    )

                    AppScreen.BATTLE_PASS -> BattlePassScreen(
                        level = user?.level ?: 1,
                        coins = user?.nexCoins ?: 1000L,
                        gems = user?.nexGems ?: 50,
                        tiers = battlePassTiers,
                        onClaimReward = { tier, isPremium, amount, type ->
                            viewModel.claimBattlePassTier(tier, isPremium, amount, type)
                        },
                        onBack = { viewModel.navigateBack() }
                    )

                    AppScreen.SOCIAL -> SocialScreen(
                        avatarConfig = avatarConfig,
                        friends = friends,
                        onToggleSquad = { id, status -> viewModel.toggleSquadFriend(id, status) },
                        onReportPlayer = { id -> viewModel.reportPlayer(id) },
                        onBack = { viewModel.navigateBack() }
                    )

                    AppScreen.CREATOR_DISTRICT -> CreatorDistrictScreen(
                        customMaps = customMaps,
                        onSaveMap = { viewModel.saveCustomMap(it) },
                        onPlayMap = { viewModel.navigateTo(AppScreen.CYBER_ARENA) },
                        onBack = { viewModel.navigateBack() }
                    )

                    AppScreen.SETTINGS -> SettingsScreen(
                        currentProfile = graphicsProfile,
                        onProfileChanged = { viewModel.setGraphicsProfile(it) },
                        onBack = { viewModel.navigateBack() }
                    )
                }
            }
        }
    }
}

@Composable
fun NexaBottomNav(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = NexaSurfaceDark.copy(alpha = 0.95f),
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(NexaBorderDark, NexaCyan.copy(alpha = 0.3f), NexaBorderDark)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavButton(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = currentScreen == AppScreen.HOME,
                onClick = { onScreenSelected(AppScreen.HOME) }
            )
            NavButton(
                icon = Icons.Default.Checkroom,
                label = "Stylist",
                isSelected = currentScreen == AppScreen.AVATAR_CUSTOMIZER,
                onClick = { onScreenSelected(AppScreen.AVATAR_CUSTOMIZER) }
            )
            // Central Highlighted Quick Play Tab
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(NexaCyan, NexaCyan.copy(alpha = 0.8f))
                        )
                    )
                    .shadow(12.dp, CircleShape, spotColor = NexaCyan)
                    .clickable { onScreenSelected(AppScreen.MATCHMAKING) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SportsEsports,
                    contentDescription = "Quick Battle",
                    tint = NexaBgDark,
                    modifier = Modifier.size(28.dp)
                )
            }
            NavButton(
                icon = Icons.Default.WorkspacePremium,
                label = "Pass",
                isSelected = currentScreen == AppScreen.BATTLE_PASS,
                onClick = { onScreenSelected(AppScreen.BATTLE_PASS) }
            )
            NavButton(
                icon = Icons.Default.Group,
                label = "Squads",
                isSelected = currentScreen == AppScreen.SOCIAL,
                onClick = { onScreenSelected(AppScreen.SOCIAL) }
            )
            NavButton(
                icon = Icons.Default.Build,
                label = "Studio",
                isSelected = currentScreen == AppScreen.CREATOR_DISTRICT,
                onClick = { onScreenSelected(AppScreen.CREATOR_DISTRICT) }
            )
            NavButton(
                icon = Icons.Default.Settings,
                label = "Settings",
                isSelected = currentScreen == AppScreen.SETTINGS,
                onClick = { onScreenSelected(AppScreen.SETTINGS) }
            )
        }
    }
}

@Composable
fun NavButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (isSelected) NexaCyan else NexaTextMuted

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            color = tint,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
        )
    }
}
