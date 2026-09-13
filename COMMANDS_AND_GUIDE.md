# 🌿 EDEN: Environmental Intelligence & Sustainability Platform
## Full Source Code, Build Commands & Technical Guide

---

### 1. Direct Downloads
- **Source Code Archive (.ZIP)**: Download from your Render site at `/EDEN-full-source-code.zip` or clone via GitHub.
- **Compiled Android APK**: Download from your Render site at `/EDEN-v5.2.0-LiveCarbon-debug.apk` or via GitHub Releases / Artifacts.
- **GitHub Repository**: [https://github.com/imashishrawmmishra-dev/EDEN](https://github.com/imashishrawmmishra-dev/EDEN)

---

### 2. Complete Commands Reference

#### A. Cloning the Repository
```bash
git clone https://github.com/imashishrawmmishra-dev/EDEN.git
cd EDEN
```

#### B. Building the Android App Locally (Terminal / Command Prompt)

**1. Make the Gradle wrapper executable (Linux / macOS):**
```bash
chmod +x ./gradlew
```

**2. Assemble the Debug APK:**
```bash
./gradlew assembleDebug
# Output location: app/build/outputs/apk/debug/app-debug.apk
```

**3. Assemble the Android App Bundle (AAB) for Google Play Store:**
```bash
./gradlew bundleDebug
# Output location: app/build/outputs/bundle/debug/app-debug.aab
```

**4. Run All Unit & Robolectric Tests:**
```bash
./gradlew testDebugUnitTest
```

**5. Clean Build:**
```bash
./gradlew clean
```

**6. Install directly to a connected Android Phone (via USB/ADB):**
```bash
./gradlew installDebug
```

---

### 3. Running with Android Studio (Recommended IDE)
1. Open **Android Studio** (Koala / Ladybug or newer).
2. Click **Open** and select the root `EDEN` folder.
3. Gradle will sync automatically with JDK 17 or 21.
4. Select your connected device or emulator and press **Shift + F10** (or the green **Run ▶** button).

---

### 4. Continuous Integration & Cloud Deployment Commands

#### GitHub Actions Workflow (`.github/workflows/android-ci-cd.yml`):
- Triggers on every push to `main`.
- Sets up JDK 21 and the Android SDK.
- Automatically generates the debug keystore and signs both the APK and AAB.
- Runs all unit tests.
- Uploads the fresh artifacts to GitHub Actions for instant download.

#### Render Web Deployment (`render.yaml`):
- Blueprint deployed at `https://eden-environmental-intelligence-1tut.onrender.com`.
- Automatically serves the full web platform and direct download links.

---

### 5. Application Architecture & Tech Stack
- **Framework**: Jetpack Compose (Kotlin 2.0.21, Jetpack Compose Bom 2024.10.01)
- **Design System**: Material Design 3 (Dynamic Color Schemes, Window Insets, Edge-to-Edge)
- **Local Persistence**: Room 2.6.1 Database (KSP-generated DAOs for GPS journeys, offsets, and activities)
- **Live Location Tracking**: Android FusedLocationProviderClient + Foreground Location Service
- **CO2 Calculation Engine**: Real-time IPCC / DEFRA emission factors across vehicle types, energy consumption, and dietary choices
- **AI Analytics**: Gemini 2.5 Flash environmental audits, photo analysis, and eco-recommendations
