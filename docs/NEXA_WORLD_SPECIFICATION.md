# NEXA WORLD: MASTER SPECIFICATION & ARCHITECTURAL BLUEPRINT

## A. Complete Product Requirements Document (PRD)
### 1. Vision & Executive Summary
Nexa World is an original, Android-first social gaming universe created to deliver hyper-engaging, short-form (3–10 minute) multiplayer experiences coupled with persistent avatar customization, virtual social hangouts, and future creator-generated mini-games. Designed with tier-1, tier-2, and tier-3 players in mind, the platform excels on low-to-mid range devices and variable mobile networks (2G/3G/4G/5G).

### 2. Target Persona & User Demographics
- **Primary Audience:** Ages 13–24 mobile-first gamers.
- **Geographic Priority:** India and emerging digital markets across South Asia, Southeast Asia, and Latin America.
- **Hardware Profile:** Entry-level (2–4GB RAM, MediaTek Helio/Snapdragon 400 series) to High-end flagship devices.
- **Network Constraints:** High packet loss tolerance, variable ping (40ms - 250ms), low data footprint.

### 3. Core Value Propositions
1. **Instant Action:** Playable within 15 seconds of launch; no multi-gigabyte mandatory patches on day 1.
2. **Social Belonging:** Dynamic social hubs (Nexus City) to express identity, show off badges, and party up with friends safely.
3. **No Pay-to-Win:** 100% cosmetic economy, ethical progression, transparent battle pass.
4. **Adaptive Graphics Engine:** 3-tier graphics profile (Low, Medium, High) dynamically scaling draw load.

---

## B. Game Design Document (GDD)
### 1. World Lore & Universe: Nexa World
In the 26th Century, humanity united around the "Nexa Core"—a clean quantum-fusion matrix powering hovering sky districts. Citizens assemble in **Nexus City** to participate in friendly athletic and tactical simulations across multiple specialized districts:
- **Nexus City (Zone 1):** Social hub, shops, meeting grounds, hologram plaza.
- **Cyber Arena (Zone 2):** Fast-paced tactical arena matches, competitive ranked cups.
- **Sky Islands (Zone 3):** Vertical parkour, checkpoint dashes, and exploration puzzles.
- **Battle District (Zone 4):** 4v4 territory control and objective warfare.
- **Creator District (Zone 5):** Sandbox foundry for player-built arenas and obstacles.

### 2. Core Game Loop: Cyber Arena (Energy Core Clash)
1. **Lobby & Matchmaking:** 10-second queue matches 2 teams of 3 (or 1v1v1v1 FFA).
2. **Spawn & Positioning:** Players enter the hexagonal cyber arena with virtual twin-stick controls (Move & Aim Blaster) plus a Dash/Dodge ability.
3. **Objective:** Collect glowing Energy Cores that spawn across dynamic nodes, bank them in team conduits, and disable opponent carriers.
4. **Win Condition:** First team to bank 100 energy units or highest score when the 3:00 minute match clock expires.
5. **Post-Match:** Instant reward breakdown (XP, NEX Coins, Battle Pass Stars, Daily Mission validation).

### 3. Controls & Physics Mechanics
- **Virtual Analog Movement Stick:** Left-hand touch zone with dynamic floating origin and smooth deadzone dampening.
- **Directional Blaster / Ability Pad:** Right-hand aim arc with auto-assist tracking nearby enemies within 45 degrees.
- **Quantum Dash:** 3.5s cooldown burst for evasion, dodging enemy blast bolts or snatching energy orbs.

---

## C. Technical Architecture
```
+-------------------------------------------------------------------------+
|                              Android Client                             |
|  [Jetpack Compose UI] <--> [Compose Canvas 60 FPS Game Loop Engine]     |
|          |                                   |                          |
|  [ViewModels / StateFlow]            [Audio / Haptic Feedback]          |
|          |                                   |                          |
|  [AppRepository] <---------------------------+                          |
|     |         |                                                         |
|     v         v                                                         |
| [Room DB]  [Multiplayer Network Adapter (WebSocket / REST Gateway)]     |
+-----+-------------+-----------------------------------------------------+
      |             |
      v             v
+-------------------------------------------------------------------------+
|                              Cloud Backend                              |
|   [API Gateway & Rate Limiter] ---> [Auth Service (OAuth / Guest / JWT)]|
|                               ---> [Matchmaking Queue Service (Redis)]  |
|                               ---> [Authoritative Game Server (Go/WS)]  |
|                               ---> [Social & Moderation Engine]         |
|                               ---> [PostgreSQL + Read Replicas]         |
+-------------------------------------------------------------------------+
```

---

## D. Database ERD & Schemas
### PostgreSQL Master Relational Schema
```sql
-- Users & Profiles
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(32) UNIQUE NOT NULL,
    tag VARCHAR(6) NOT NULL,
    email VARCHAR(255),
    role VARCHAR(16) DEFAULT 'PLAYER',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE profiles (
    user_id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    level INT DEFAULT 1,
    xp BIGINT DEFAULT 0,
    rank_tier VARCHAR(24) DEFAULT 'BRONZE_I',
    rank_points INT DEFAULT 0,
    nex_coins BIGINT DEFAULT 500,
    nex_gems INT DEFAULT 50,
    avatar_config JSONB NOT NULL,
    selected_title VARCHAR(48) DEFAULT 'Novice Pioneer',
    matches_played INT DEFAULT 0,
    matches_won INT DEFAULT 0,
    energy_collected INT DEFAULT 0
);

-- Inventory & Items
CREATE TABLE inventory_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    item_id VARCHAR(64) NOT NULL,
    category VARCHAR(32) NOT NULL,
    is_equipped BOOLEAN DEFAULT FALSE,
    acquired_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Social & Parties
CREATE TABLE friendships (
    user_id UUID REFERENCES users(id),
    friend_id UUID REFERENCES users(id),
    status VARCHAR(16) DEFAULT 'ACCEPTED',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    PRIMARY KEY (user_id, friend_id)
);

-- Matches & Leaderboards
CREATE TABLE matches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mode VARCHAR(32) NOT NULL,
    winner_team VARCHAR(16),
    duration_seconds INT NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE match_participants (
    match_id UUID REFERENCES matches(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id),
    team VARCHAR(16) NOT NULL,
    score INT DEFAULT 0,
    kills INT DEFAULT 0,
    energy_banked INT DEFAULT 0,
    PRIMARY KEY (match_id, user_id)
);

-- Safety & Moderation
CREATE TABLE moderation_reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reporter_id UUID REFERENCES users(id),
    reported_id UUID REFERENCES users(id),
    reason VARCHAR(64) NOT NULL,
    details TEXT,
    status VARCHAR(16) DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

---

## E. API Architecture
### REST Endpoints
- `POST /v1/auth/guest` - Anonymous device-bound onboarding.
- `GET /v1/player/profile` - Fetch XP, rank, currencies, and stats.
- `PUT /v1/player/avatar` - Update avatar visual customization.
- `GET /v1/shop/battlepass` - Tiers, status, and reward track.
- `POST /v1/shop/battlepass/claim` - Claim tier item.
- `GET /v1/social/friends` - Friend list with real-time online status.
- `POST /v1/social/report` - Child safety and harassment reporting.

### Real-Time WebSocket Protocol (Game Loop)
- `C2S_MATCH_JOIN { ticket, mode, region }`
- `S2C_MATCH_STATE { tick, entities: [{ id, x, y, hp, team, state }] }`
- `C2S_INPUT_MOVE { angle, magnitude, seq }`
- `C2S_INPUT_DASH { dir_x, dir_y, seq }`
- `C2S_INPUT_FIRE { target_x, target_y, seq }`
- `S2C_EVENT_SCORE { team, newScore, message }`
- `S2C_MATCH_OVER { winner, rewards: { xp, coins } }`

---

## F. Android Architecture
- **Framework:** Jetpack Compose (100% declarative UI and Canvas game render pipeline).
- **Architecture Pattern:** Clean Architecture + MVVM + Unidirectional Data Flow (UDF).
- **Local Persistence:** Room Database with reactive Kotlin `Flow<T>`.
- **Game Engine:** Custom lightweight, high-performance Compose Canvas 60 FPS Game Loop with `withFrameMillisClock` or `LaunchedEffect` game ticker, object pooling, and zero garbage-collection per frame.
- **Asynchrony:** Kotlin Coroutines + StateFlow / SharedFlow.

---

## G. Multiplayer Architecture
- **Authoritative Server:** Client transmits only normalized inputs; server simulates physics, collision, and energy banking.
- **Client Prediction:** Client displays local player velocity immediately while reconciling discrepancies via sequence IDs.
- **Linear Interpolation (LERP):** Remote players render smoothly over past 100ms packet buffers.
- **Resilient Reconnection:** Automatic backoff reconnection within 15 seconds without losing match state.

---

## H. UI/UX Screen Map
1. **Home Screen (Nexus Hub):** Avatar hero showcase, Quick Play FAB, Daily Missions preview, Party Lobby indicator.
2. **Avatar Customizer Screen:** Interactive 2D avatar model, selector tabs (Hair, Outfit, Visor, Wings, Colors, Emotes).
3. **Play / Matchmaking Screen:** Game mode carousel, region ping indicator, matchmaking radar animation.
4. **Cyber Arena (Live Game Screen):** Dual virtual analog controls, team score bars, energy core markers, killfeed, ping HUD.
5. **Battle Pass Screen:** Season 1 progression track (Free vs Premium), tier reward preview.
6. **Social & Friends Screen:** Squad management, friend requests, safe chat with quick presets, player report modal.
7. **Creator District (Map Builder):** Grid arena editor for custom obstacle placement and instant playtesting.
8. **Settings & Safety Center:** Graphics profile selector (Low/Med/High), audio controls, Youth Safety & Privacy center.

---

## I. MVP Feature List
- [x] Full Guest & Player Profile lifecycle (Level, XP, Rank, Coins, Gems).
- [x] Interactive Avatar Customizer with live dynamic preview.
- [x] Interactive Nexus City Social Hub.
- [x] Playable 60 FPS Cyber Arena Mini-Game with AI bots, virtual joystick, energy capture, abilities, and dynamic scoring.
- [x] Skill-based Matchmaking Simulator with region & ping telemetry.
- [x] Season 1 Battle Pass progression system.
- [x] Daily Missions with claimable rewards.
- [x] Social Squad & Friends list with Safe Chat & Quick tactical presets.
- [x] Child Safety & Reporting modal with profanity filter.
- [x] 3-tier Graphics Profile (Low / Medium / High).
- [x] Creator District Map Editor sandbox.
- [x] Persistent Room database for player progression.

---

## J. Development Roadmap
- **Sprint 1-2:** Foundation, Room DB, Theme, Core Engine Loop.
- **Sprint 3-4:** Cyber Arena mechanics, Bot state machines, Touch controls.
- **Sprint 5-6:** Avatar Customizer, Nexus City Hub, Social Systems.
- **Sprint 7-8:** Battle Pass, Missions, Economy, Safety Center.
- **Sprint 9-10:** Optimization, 60 FPS validation, Closed Beta & Play Store prep.

---

## K. Estimated Development Team Requirements
- 1 Lead Game Architect & Technical Lead
- 2 Senior Android (Jetpack Compose / Game Systems) Engineers
- 2 Backend (Go / Node.js / WebSocket) Engineers
- 1 Technical Game Artist / 2D/3D Animator
- 1 UI/UX Designer (Mobile Game Specialization)
- 1 QA & Performance Automation Engineer

---

## L. Estimated Infrastructure Requirements
- **Matchmaking & API:** 3x AWS ECS / GCP Cloud Run instances (Auto-scaling 2-10 instances).
- **Game Servers:** Dedicated low-latency EC2 / GCE instances in Mumbai (`ap-south-1`) and Singapore (`ap-southeast-1`).
- **Database:** Managed PostgreSQL (db.m6g.large) with Redis cluster for pub/sub session state.
- **CDN:** Cloudflare / CloudFront for asset bundles.

---

## M. Security Architecture
- TLS 1.3 for all REST and WSS traffic.
- Zero client-authoritative state for economy, xp, or match outcomes.
- Rate-limiting token bucket per player IP and session token.
- Salted & hashed JWT credentials with short-lived session refresh rotation.

---

## N. Testing Strategy
- Unit tests for game logic, math vectors, mission progress, and repository operations.
- Robolectric local JVM testing for ViewModel states and Room DAOs.
- Performance profiling for memory leaks and 60 FPS frame time (<16.6ms per tick).

---

## O. Google Play Launch Checklist
- [x] Unique package name (`com.aistudio.nexaworld.kxrvsp`).
- [x] Adaptive icon with proper mipmap density fallbacks.
- [x] Zero broad storage permissions; uses modern Android APIs.
- [x] Compliant privacy policy and COPPA/Child safety compliance disclosures.
- [x] Android 15 / 16 (API 35/36) compatibility.
