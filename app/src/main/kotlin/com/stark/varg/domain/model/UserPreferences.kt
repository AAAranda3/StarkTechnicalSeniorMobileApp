package com.stark.varg.domain.model

enum class UnitSystem {
    METRIC,
    IMPERIAL,
}

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val flagEmoji: String,
) {
    SPANISH("es", "Español", "🇪🇸"),
    ENGLISH("en", "English", "🇬🇧"),
}

data class SavedPowerMode(
    val maxPowerHp: Int,
    val engineBrakingPct: Int,
    val regenPct: Int,
)

data class UserPreferences(
    val selectedMode: String = "M3",
    val modeProfiles: Map<String, SavedPowerMode> = defaultModeProfiles(),
    val chargeTargetPct: Int = 80,
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val language: AppLanguage = AppLanguage.SPANISH,
) {
    companion object {
        fun defaultModeProfiles(): Map<String, SavedPowerMode> = mapOf(
            "M1" to SavedPowerMode(maxPowerHp = 35, engineBrakingPct = 25, regenPct = 40),
            "M2" to SavedPowerMode(maxPowerHp = 50, engineBrakingPct = 35, regenPct = 50),
            "M3" to SavedPowerMode(maxPowerHp = 80, engineBrakingPct = 45, regenPct = 60),
            "M4" to SavedPowerMode(maxPowerHp = 70, engineBrakingPct = 50, regenPct = 65),
            "M5" to SavedPowerMode(maxPowerHp = 80, engineBrakingPct = 60, regenPct = 75),
        )
    }
}
