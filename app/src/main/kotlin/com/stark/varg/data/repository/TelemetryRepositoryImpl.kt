package com.stark.varg.data.repository

import com.stark.varg.data.mapper.toDomain
import com.stark.varg.data.source.TelemetryDataSource
import com.stark.varg.domain.model.TelemetrySnapshot
import com.stark.varg.domain.repository.TelemetryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.math.sin

class TelemetryRepositoryImpl @Inject constructor(
    private val dataSource: TelemetryDataSource,
) : TelemetryRepository {

    override suspend fun getTelemetrySnapshot(): Result<TelemetrySnapshot> = runCatching {
        dataSource.fetchSnapshot().toDomain()
    }

    override fun observeLiveTelemetryStream(): Flow<TelemetrySnapshot> = flow {
        val base = getTelemetrySnapshot().getOrThrow()
        var currentDistance = base.session.distanceKm
        var step = 0

        while (true) {
            delay(250) // 4 Hz CAN broadcast rate
            step++

            // Dynamic oscillating power curve simulating track throttle modulation
            val hpModulation = sin(step * 0.25) * 18.0
            val simulatedHp = (base.motor.powerHp + hpModulation).coerceIn(20.0, 80.0)

            // Dynamic thermal progression based on motor load
            val tempVariation = (simulatedHp - 45.0) * 0.02
            val simulatedTemp = (base.motor.temperatureC + tempVariation).coerceIn(45.0, 85.0)

            // Gradual trip distance accumulator
            currentDistance += 0.003
            val simulatedSpeed = (35.0 + sin(step * 0.25 + 0.5) * 30.0).coerceIn(15.0, 110.0)

            val updatedSnapshot = base.copy(
                motor = base.motor.copy(
                    powerHp = (simulatedHp * 10).roundToInt() / 10.0,
                    temperatureC = (simulatedTemp * 10).roundToInt() / 10.0,
                ),
                session = base.session.copy(
                    distanceKm = (currentDistance * 100).roundToInt() / 100.0,
                    maxSpeedKmh = base.session.maxSpeedKmh.coerceAtLeast((simulatedSpeed * 10).roundToInt() / 10.0),
                ),
            )
            emit(updatedSnapshot)
        }
    }
}
