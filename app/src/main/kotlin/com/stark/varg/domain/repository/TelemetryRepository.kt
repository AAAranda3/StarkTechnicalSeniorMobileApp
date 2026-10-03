package com.stark.varg.domain.repository

import com.stark.varg.domain.model.TelemetrySnapshot
import kotlinx.coroutines.flow.Flow

interface TelemetryRepository {
    suspend fun getTelemetrySnapshot(): Result<TelemetrySnapshot>
    fun observeLiveTelemetryStream(): Flow<TelemetrySnapshot>
}
