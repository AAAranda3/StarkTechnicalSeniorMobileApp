package com.stark.varg.presentation.util

/**
 * Utility for mapping Stark VARG riding modes (M1–M5) to specific disciplines and maps
 * (e.g. M1 Trail, M2 Hard Enduro, M3 Enduro, M4 Motocross, M5 Alpha).
 */
object VargModeUtil {

    fun getMapName(mode: String, defaultMap: String = "ENDURO"): String = when (mode.uppercase()) {
        "M1" -> "TRAIL"
        "M2" -> "HARD ENDURO"
        "M3" -> if (defaultMap.isNotBlank()) defaultMap.uppercase() else "ENDURO"
        "M4" -> "MOTOCROSS"
        "M5" -> "ALPHA"
        else -> defaultMap.uppercase()
    }

    fun getDisplayName(mode: String, defaultMap: String = "ENDURO"): String {
        val map = getMapName(mode, defaultMap)
        val formatted = map.lowercase().split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
        return "$mode $formatted"
    }
}
