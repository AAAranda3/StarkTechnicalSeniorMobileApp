package com.stark.varg.domain.model

data class TelemetrySnapshot(
    val bike: BikeInfo,
    val timestamp: String,
    val battery: BatteryStatus,
    val motor: MotorStatus,
    val rideSettings: RideSettings,
    val session: SessionData,
    val diagnostics: DiagnosticsData,
)
