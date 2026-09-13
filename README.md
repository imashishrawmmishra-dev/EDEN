# EDEN — Environmental Intelligence & Sustainability Platform

A scientific environmental intelligence, calculation, and learning platform. Built with Jetpack Compose, Kotlin Coroutines, Room Database, and GPS live location telemetry.

---

## 💻 How to Run on Any Device

### 1. 📱 On Android Phone / Tablet (Native APK)
1. **Download the APK**: Download `EDEN-v5.2.0-LiveCarbon-debug.apk` directly from the project root.
2. **Transfer to phone**: Send the APK to your phone via USB cable, Google Drive, or messaging.
3. **Install**: Tap the `.apk` file on your device. When prompted by Android Security, tap **Settings** and enable *"Allow from this source"*.
4. **Launch**: Open **EDEN** to access the full offline Knowledge Graph, GHG Calculators, and GPS Live Location Carbon Tracker.

### 2. 💻 On Laptop / Desktop (Windows, macOS, Linux)
You have two convenient options to run EDEN on a computer:
- **Option A — Android Studio Desktop Emulator**:
  1. Open Android Studio on your PC/Mac.
  2. Start an Android Virtual Device (AVD).
  3. Drag and drop `EDEN-v5.2.0-LiveCarbon-debug.apk` onto the emulator window to install and run.
- **Option B — Desktop Android Runtime (BlueStacks / Windows Subsystem for Android)**:
  1. Open BlueStacks, Nox, or WSA on Windows 11.
  2. Click **Install APK** and select the APK file.

### 3. 🌐 Web Browser Access
- Connect directly through your cloud-hosted environment or web browser at your platform URL:
  `https://eden-environmental-intelligence.onrender.com`

---

## 🏛️ Architecture & System Structure

```
com.example/
├── calculator/                # Deterministic Engineering Engines
│   ├── CarbonCalculatorEngine.kt      # GHG Protocol Scope 1, 2, 3 calculations
│   ├── AirCalculatorEngine.kt         # EPA Method 2 Stack Gas Flow & Emissions
│   ├── WaterCalculatorEngine.kt       # Wastewater BOD/COD, PE, & Retention Time
│   ├── NoiseCalculatorEngine.kt       # OSHA / EPA Acoustic Decibel Summation
│   └── LiveCarbonTrackerEngine.kt     # GPS Real-Time Transit Emission Models
├── location/                  # Telemetry & GPS Sensor Integration
│   ├── LocationTracker.kt             # FusedLocationProviderClient reactive flow
│   └── LiveLocationState.kt           # GPS Coordinates, Speed, Altitude, & Accuracy
├── data/                      # Persistence & Networking Layer
│   ├── db/                            # Local Room Database
│   ├── model/                         # Domain Entities & Data Transfer Objects
│   ├── remote/                        # Grounded Environmental Inference Service
│   └── repository/                    # Single Source of Truth Repository
├── ui/                        # User Interface Layer (Jetpack Compose)
│   ├── screens/                       # Primary Feature Screens
│   │   ├── HomeScreen.kt              # Executive Dashboard & System Overview
│   │   ├── LiveCarbonTrackerScreen.kt # Live GPS Carbon Tracking & Transit Modes
│   │   ├── AskEdenScreen.kt           # Grounded Environmental Intelligence
│   │   ├── CalculatorsScreen.kt       # Multi-Tab Engineering Calculators
│   │   ├── KnowledgeGraphScreen.kt    # Multi-tier Environmental Knowledge Graph
│   │   ├── MonitoringScreen.kt        # Real-time Telemetry & NAAQS Limit Auditor
│   │   ├── LearningScreen.kt          # Professional Curriculum & Competency Quizzes
│   │   ├── ResourcesScreen.kt         # Curated Standards & Regulatory Library
│   │   └── ProfileScreen.kt           # Role Customization & System Diagnostics
│   ├── components/                    # Reusable Design System Components
│   └── theme/                         # Material 3 Design Tokens & Typography
└── viewmodel/                 # State Management
    └── EdenViewModel.kt               # Central Reactive State & Business Operations
```

---

## 🔄 Continuous Deployment Pipeline (CI/CD)

EDEN is fully configured for automated multi-target continuous delivery:

```
                  YOU
                   │
                   ▼
             GitHub repository
                   │
           ┌───────┴────────┐
           ▼                ▼
        Render          GitHub Actions
           │                │
           ▼                ▼
    EDEN Web/API       Android AAB & APK
           │                │
           ▼                ▼
    Live EDEN Site     Google Play Store / Releases
```

### Phase 1 — Automatic Website & API Deployment (GitHub → Render)
1. In [Render Dashboard](https://dashboard.render.com), click **New** > **Blueprint**.
2. Select your `EDEN` GitHub repository.
3. Render automatically reads `render.yaml` and launches your service.
4. Whenever you push to `main`, Render redeploys your web interface and API within minutes.

### Phase 2 — Automatic Android Builds (GitHub → GitHub Actions)
- Configured in `.github/workflows/android-ci-cd.yml`.
- On every push or pull request to `main`:
  1. Runs all unit and Robolectric tests (`./gradlew testDebugUnitTest`).
  2. Compiles the Android APK (`./gradlew assembleDebug`).
  3. Packages the Google Play App Bundle (`./gradlew bundleDebug`).
  4. Uploads both APK and AAB as downloadable build artifacts directly in the GitHub Actions tab.

### Phase 3 — Automatic Releases & Google Play (Tags → Play Store)
- Pushing a version tag (e.g. `git tag v5.2.0 && git push origin v5.2.0`) automatically generates a **GitHub Release** with the APK attached for direct download.
- For Google Play automation, add your Play Console service account JSON to your GitHub Secrets (`PLAY_STORE_JSON_KEY`) to auto-publish new tracks.

---

## 🔒 Security & Privacy Standard
- **Zero Client Identification**: No customer personal identities, telemetry trackers, or marketing analytics are collected.
- **Protected Keys**: API keys and secrets remain strictly encapsulated in backend configurations and are never exposed in user-facing UI.
- **Local Persistence**: All calculation history and field monitoring points remain securely stored in your local on-device Room database.
