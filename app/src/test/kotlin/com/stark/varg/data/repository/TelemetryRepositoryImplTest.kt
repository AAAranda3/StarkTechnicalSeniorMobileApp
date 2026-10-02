package com.stark.varg.data.repository

import com.stark.varg.data.dto.BatteryDto
import com.stark.varg.data.dto.BikeDto
import com.stark.varg.data.dto.DiagnosticsDto
import com.stark.varg.data.dto.MotorDto
import com.stark.varg.data.dto.RideSettingsDto
import com.stark.varg.data.dto.SessionDto
import com.stark.varg.data.dto.TelemetryDto
import com.stark.varg.data.dto.WarningDto
import com.stark.varg.data.source.TelemetryDataSource
import com.stark.varg.domain.model.ChargingState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class TelemetryRepositoryImplTest {

    private val dataSource: TelemetryDataSource = mockk()
    private val repository = TelemetryRepositoryImpl(dataSource)

    private fun buildDto() = TelemetryDto(
        bike = BikeDto("Stark VARG MX 1.2", "Alpha", "3.4.1", "https://img.example.com/bike.webp"),
        timestamp = "2025-05-19T10:32:45Z",
        battery = BatteryDto(73, 38, 34.7, "discharging"),
        motor = MotorDto(52.4, 61.2),
        rideSettings = RideSettingsDto("enduro", 80, 45, 60),
        session = SessionDto(3742, 24.7, 94.1),
        diagnostics = DiagnosticsDto(
            emptyList(),
            listOf(WarningDto("W_MOT_TEMP_HIGH", "Motor temperature elevated", "warning")),
        ),
    )

    // --- Positive cases -------------------------------------------------------

    @Test
    fun `successful fetch returns Result success with mapped snapshot`() = runTest {
        coEvery { dataSource.fetchSnapshot() } returns buildDto()

        val result = repository.getTelemetrySnapshot()

        assertTrue(result.isSuccess)
        val snapshot = result.getOrThrow()
        assertEquals("Stark VARG MX 1.2", snapshot.bike.model)
        assertEquals(73, snapshot.battery.stateOfChargePct)
        assertEquals(ChargingState.DISCHARGING, snapshot.battery.chargingState)
        assertEquals(1, snapshot.diagnostics.warnings.size)
    }

    // --- Negative cases -------------------------------------------------------

    @Test
    fun `IOException from dataSource returns Result failure`() = runTest {
        coEvery { dataSource.fetchSnapshot() } throws IOException("File not found")

        val result = repository.getTelemetrySnapshot()

        assertTrue(result.isFailure)
        assertEquals("File not found", result.exceptionOrNull()!!.message)
    }

    @Test
    fun `RuntimeException from dataSource returns Result failure`() = runTest {
        coEvery { dataSource.fetchSnapshot() } throws RuntimeException("Unexpected error")

        val result = repository.getTelemetrySnapshot()

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `exception with null message is preserved in Result`() = runTest {
        coEvery { dataSource.fetchSnapshot() } throws RuntimeException()

        val result = repository.getTelemetrySnapshot()

        assertTrue(result.isFailure)
        assertNull(result.exceptionOrNull()!!.message)
    }

    // --- Corner case ---------------------------------------------------------

    @Test
    fun `empty fault codes and warnings from dataSource map to empty collections`() = runTest {
        val dto = buildDto().copy(
            diagnostics = DiagnosticsDto(emptyList(), emptyList()),
        )
        coEvery { dataSource.fetchSnapshot() } returns dto

        val snapshot = repository.getTelemetrySnapshot().getOrThrow()

        assertTrue(snapshot.diagnostics.faultCodes.isEmpty())
        assertTrue(snapshot.diagnostics.warnings.isEmpty())
    }
}
