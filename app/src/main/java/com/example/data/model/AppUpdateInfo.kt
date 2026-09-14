package com.example.data.model

enum class UpdateState {
    IDLE,
    CHECKING,
    UPDATE_AVAILABLE,
    DOWNLOADING,
    READY_TO_INSTALL,
    UP_TO_DATE,
    ERROR
}

data class AppUpdateInfo(
    val currentVersionCode: Int = 1,
    val currentVersionName: String = "1.0.0",
    val latestVersionCode: Int = 2,
    val latestVersionName: String = "1.1.0",
    val releaseTitle: String = "EDEN v1.1.0 — Global Environmental Intelligence Update",
    val releaseDate: String = "September 2026",
    val apkSizeMb: Double = 14.8,
    val isMandatory: Boolean = false,
    val releaseNotes: List<String> = listOf(
        "Auto-update synchronization engine for instant customer updates",
        "Updated IPCC AR6 GWP100 factors and 2026 emission coefficients",
        "Enhanced WHO 2021 live sensor monitoring & health advisories",
        "Battery and network background sync optimization",
        "Offline Knowledge Graph cache improvements"
    ),
    val downloadUrl: String = "https://github.com/eden-intel/eden-app/releases/download/v1.1.0/eden-v1.1.0.apk",
    val playStoreUrl: String = "https://play.google.com/store/apps/details?id=com.aistudio.eden.envintel",
    val isUpdateAvailable: Boolean = true,
    val autoCheckEnabled: Boolean = true,
    val autoDownloadOnWifi: Boolean = true,
    val lastCheckedTimestamp: Long = System.currentTimeMillis(),
    val status: UpdateState = UpdateState.UPDATE_AVAILABLE,
    val downloadProgress: Float = 0f,
    val updateChannel: String = "Stable (Production)"
)
