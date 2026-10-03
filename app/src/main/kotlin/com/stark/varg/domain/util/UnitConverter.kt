package com.stark.varg.domain.util

import com.stark.varg.domain.model.UnitSystem
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Universal scientific unit conversion utility for Stark VARG telemetry.
 * Formats SI baseline telemetry into Metric or Imperial units.
 */
object UnitConverter {
    private const val KM_TO_MILES = 0.621371192

    fun formatDistance(km: Double, unitSystem: UnitSystem): Pair<String, String> {
        return if (unitSystem == UnitSystem.METRIC) {
            String.format(Locale.US, "%.1f", km) to "km"
        } else {
            String.format(Locale.US, "%.1f", km * KM_TO_MILES) to "mi"
        }
    }

    fun formatRange(rangeKm: Int, unitSystem: UnitSystem): Pair<String, String> {
        return if (unitSystem == UnitSystem.METRIC) {
            "$rangeKm" to "km"
        } else {
            "${(rangeKm * KM_TO_MILES).roundToInt()}" to "mi"
        }
    }

    fun formatSpeed(kmh: Double, unitSystem: UnitSystem): Pair<String, String> {
        return if (unitSystem == UnitSystem.METRIC) {
            String.format(Locale.US, "%.1f", kmh) to "km/h"
        } else {
            String.format(Locale.US, "%.1f", kmh * KM_TO_MILES) to "mph"
        }
    }

    fun formatTemperature(celsius: Double, unitSystem: UnitSystem): Pair<String, String> {
        return if (unitSystem == UnitSystem.METRIC) {
            String.format(Locale.US, "%.1f", celsius) to "°C"
        } else {
            val fahrenheit = celsius * 1.8 + 32.0
            String.format(Locale.US, "%.1f", fahrenheit) to "°F"
        }
    }
}
