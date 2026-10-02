package com.stark.varg.data.source

import com.stark.varg.data.dto.TelemetryDto

interface TelemetryDataSource {
    suspend fun fetchSnapshot(): TelemetryDto
}
