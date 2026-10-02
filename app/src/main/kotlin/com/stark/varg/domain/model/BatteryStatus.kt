package com.stark.varg.domain.model

data class BatteryStatus(
    val stateOfChargePct: Int,
    val estimatedRangeKm: Int,
    val temperatureC: Double,
    val chargingState: ChargingState,
)
