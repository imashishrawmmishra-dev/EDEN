package com.example.data.model

data class UserAuthProfile(
    val isLoggedIn: Boolean = false,
    val authMethod: String = "GUEST", // "PHONE", "GOOGLE", "PURPOSE_DESIGNATION", "GUEST"
    val displayName: String = "Guest Environmentalist",
    val phoneNumber: String = "",
    val countryCode: String = "+1",
    val email: String = "",
    val purpose: String = "Environmental Intelligence & Action",
    val designation: String = "Sustainability Researcher",
    val rewardPoints: Int = 0,
    val organization: String = "EDEN Community"
) {
    val badgeTitle: String
        get() = when {
            rewardPoints >= 500 -> "🌿 Net-Zero Champion"
            rewardPoints >= 300 -> "🌍 Carbon Auditor"
            rewardPoints >= 200 -> "🍃 Eco Pioneer"
            rewardPoints >= 100 -> "🌱 Green Sentinel"
            else -> "🌾 Environmental Observer"
        }
}
