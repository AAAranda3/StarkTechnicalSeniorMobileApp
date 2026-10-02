package com.stark.varg.data.mapper

import com.stark.varg.data.dto.BatteryDto
import com.stark.varg.data.dto.BikeDto
import com.stark.varg.data.dto.DiagnosticsDto
import com.stark.varg.data.dto.MotorDto
import com.stark.varg.data.dto.RideSettingsDto
import com.stark.varg.data.dto.SessionDto
import com.stark.varg.data.dto.TelemetryDto
import com.stark.varg.data.dto.WarningDto
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.BikeInfo
import com.stark.varg.domain.model.ChargingState
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.domain.model.SessionData
import com.stark.varg.domain.model.TelemetrySnapshot
import com.stark.varg.domain.model.Warning
import com.stark.varg.domain.model.WarningSeverity

fun TelemetryDto.toDomain(): TelemetrySnapshot = TelemetrySnapshot(
    bike = bike.toDomain(),
    timestamp = timestamp,
    battery = battery.toDomain(),
    motor = motor.toDomain(),
    rideSettings = rideSettings.toDomain(),
    session = session.toDomain(),
    diagnostics = diagnostics.toDomain(),
)

private fun BikeDto.toDomain() = BikeInfo(
    model = model,
    variant = variant,
    firmwareVersion = firmwareVersion,
    imageUrl = imageUrl,
)

private fun BatteryDto.toDomain() = BatteryStatus(
    stateOfChargePct = stateOfChargePct,
    estimatedRangeKm = estimatedRangeKm,
    temperatureC = temperatureC,
    chargingState = when (chargingState.lowercase()) {
        "charging" -> ChargingState.CHARGING
        "discharging" -> ChargingState.DISCHARGING
        else -> ChargingState.UNKNOWN
    },
)

private fun MotorDto.toDomain() = MotorStatus(
    powerHp = powerHp,
    temperatureC = temperatureC,
)

private fun RideSettingsDto.toDomain() = RideSettings(
    powerMap = powerMap,
    maxPowerHp = maxPowerHp,
    engineBrakingPct = engineBrakingPct,
    regenPct = regenPct,
)

private fun SessionDto.toDomain() = SessionData(
    durationS = durationS,
    distanceKm = distanceKm,
    maxSpeedKmh = maxSpeedKmh,
)

private fun DiagnosticsDto.toDomain() = DiagnosticsData(
    faultCodes = faultCodes,
    warnings = warnings.map { it.toDomain() },
)

private fun WarningDto.toDomain() = Warning(
    code = code,
    message = message,
    severity = when (severity.lowercase()) {
        "warning" -> WarningSeverity.WARNING
        "critical" -> WarningSeverity.CRITICAL
        "info" -> WarningSeverity.INFO
        else -> WarningSeverity.UNKNOWN
    },
)
