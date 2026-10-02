package com.stark.varg.domain.repository

import com.stark.varg.domain.model.TelemetrySnapshot

interface TelemetryRepository {
    suspend fun getTelemetrySnapshot(): Result<TelemetrySnapshot>
}
