package com.stark.varg.data.source

import android.content.Context
import com.squareup.moshi.Moshi
import com.stark.varg.data.dto.TelemetryDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalTelemetryDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi,
) : TelemetryDataSource {

    override suspend fun fetchSnapshot(): TelemetryDto = withContext(Dispatchers.IO) {
        // Simulate vehicle CAN bus / network handshake latency so the user experiences the high-tech skeleton loading process
        kotlinx.coroutines.delay(950L)
        val json = context.assets.open("telemetry_snapshot.json")
            .bufferedReader()
            .use { it.readText() }
        val adapter = moshi.adapter(TelemetryDto::class.java)
        adapter.fromJson(json)
            ?: throw IllegalStateException("Failed to parse telemetry_snapshot.json")
    }
}
