package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val releaseDate: String = "September 15, 2026",
    val lastUpdateTime: String = "Sep 15, 2026 • 10:55 PM",
    val apkSizeMb: Double = 14.8,
    val isMandatory: Boolean = false,
    val releaseNotes: List<String> = listOf(
        "20 ESG, EIA & Environmental Solutions domains with OTP-gated dossiers",
        "Automated sync engine with real-time last update verification",
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
) {
    fun getFormattedLastCheck(): String {
        return try {
            val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
            sdf.format(Date(lastCheckedTimestamp))
        } catch (_: Exception) {
            "Sep 15, 2026 • 10:55 PM"
        }
    }
}
