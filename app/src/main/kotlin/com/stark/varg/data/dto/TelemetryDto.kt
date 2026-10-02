package com.stark.varg.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TelemetryDto(
    val bike: BikeDto,
    val timestamp: String,
    val battery: BatteryDto,
    val motor: MotorDto,
    @Json(name = "ride_settings") val rideSettings: RideSettingsDto,
    val session: SessionDto,
    val diagnostics: DiagnosticsDto,
)

@JsonClass(generateAdapter = true)
data class BikeDto(
    val model: String,
    val variant: String,
    @Json(name = "firmware_version") val firmwareVersion: String,
    @Json(name = "image_url") val imageUrl: String,
)

@JsonClass(generateAdapter = true)
data class BatteryDto(
    @Json(name = "state_of_charge_pct") val stateOfChargePct: Int,
    @Json(name = "estimated_range_km") val estimatedRangeKm: Int,
    @Json(name = "temperature_c") val temperatureC: Double,
    @Json(name = "charging_state") val chargingState: String,
)

@JsonClass(generateAdapter = true)
data class MotorDto(
    @Json(name = "power_hp") val powerHp: Double,
    @Json(name = "temperature_c") val temperatureC: Double,
)

@JsonClass(generateAdapter = true)
data class RideSettingsDto(
    @Json(name = "power_map") val powerMap: String,
    @Json(name = "max_power_hp") val maxPowerHp: Int,
    @Json(name = "engine_braking_pct") val engineBrakingPct: Int,
    @Json(name = "regen_pct") val regenPct: Int,
)

@JsonClass(generateAdapter = true)
data class SessionDto(
    @Json(name = "duration_s") val durationS: Int,
    @Json(name = "distance_km") val distanceKm: Double,
    @Json(name = "max_speed_kmh") val maxSpeedKmh: Double,
)

@JsonClass(generateAdapter = true)
data class DiagnosticsDto(
    @Json(name = "fault_codes") val faultCodes: List<String>,
    val warnings: List<WarningDto>,
)

@JsonClass(generateAdapter = true)
data class WarningDto(
    val code: String,
    val message: String,
    val severity: String,
)
