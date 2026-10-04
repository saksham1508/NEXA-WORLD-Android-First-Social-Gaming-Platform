package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.BattlePassTierEntity
import com.example.data.local.entity.CustomMapEntity
import com.example.data.local.entity.DailyMissionEntity
import com.example.data.local.entity.FriendEntity
import com.example.data.local.entity.InventoryItemEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.NexaRepository
import com.example.model.AvatarConfig
import com.example.model.GraphicsProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    AVATAR_CUSTOMIZER,
    MATCHMAKING,
    CYBER_ARENA,
    BATTLE_PASS,
    SOCIAL,
    CREATOR_DISTRICT,
    SETTINGS
}

class NexaViewModel(private val repository: NexaRepository) : ViewModel() {

    val user: StateFlow<UserEntity?> = repository.userFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val avatarConfig: StateFlow<AvatarConfig> = user.map { u ->
        if (u != null) {
            AvatarConfig(
                skinToneHex = u.skinTone,
                hairStyle = u.hairStyle,
                hairColorHex = u.hairColor,
                eyeColorHex = u.eyeColor,
                outfitStyle = u.outfitStyle,
                outfitColorHex = u.outfitColor,
                accentColorHex = u.accentColor,
                visorStyle = u.visorStyle,
                wingsStyle = u.wingsStyle,
                emote = u.emote
            )
        } else {
            AvatarConfig()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AvatarConfig())

    val inventory: StateFlow<List<InventoryItemEntity>> = repository.inventoryFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val missions: StateFlow<List<DailyMissionEntity>> = repository.missionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val friends: StateFlow<List<FriendEntity>> = repository.friendsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val battlePassTiers: StateFlow<List<BattlePassTierEntity>> = repository.battlePassFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customMaps: StateFlow<List<CustomMapEntity>> = repository.customMapsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _graphicsProfile = MutableStateFlow(GraphicsProfile.MEDIUM)
    val graphicsProfile: StateFlow<GraphicsProfile> = _graphicsProfile.asStateFlow()

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun navigateBack() {
        if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun setGraphicsProfile(profile: GraphicsProfile) {
        _graphicsProfile.value = profile
    }

    fun saveAvatar(config: AvatarConfig) {
        viewModelScope.launch {
            repository.updateAvatar(config)
        }
    }

    fun recordMatchFinished(won: Boolean, kills: Int, energyBanked: Int) {
        viewModelScope.launch {
            repository.recordMatchResult(won, kills, energyBanked)
        }
    }

    fun claimMission(missionId: String, rewardCoins: Long, rewardXp: Int) {
        viewModelScope.launch {
            repository.claimMission(missionId, rewardCoins, rewardXp)
        }
    }

    fun claimBattlePassTier(tier: Int, isPremium: Boolean, amount: Int, type: String) {
        viewModelScope.launch {
            repository.claimBattlePassTier(tier, isPremium, amount, type)
        }
    }

    fun toggleSquadFriend(friendId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleSquadFriend(friendId, currentStatus)
        }
    }

    fun reportPlayer(friendId: String) {
        viewModelScope.launch {
            repository.reportPlayer(friendId)
        }
    }

    fun saveCustomMap(map: CustomMapEntity) {
        viewModelScope.launch {
            repository.saveCustomMap(map)
        }
    }
}
