package com.stark.varg.domain.usecase

import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.BikeInfo
import com.stark.varg.domain.model.ChargingState
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.domain.model.SessionData
import com.stark.varg.domain.model.TelemetrySnapshot
import com.stark.varg.domain.repository.TelemetryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class GetTelemetryUseCaseTest {

    private val repository: TelemetryRepository = mockk()
    private val useCase = GetTelemetryUseCase(repository)

    private val fakeSnapshot = TelemetrySnapshot(
        bike = BikeInfo("Stark VARG MX 1.2", "Alpha", "3.4.1", "https://img.example.com/bike.webp"),
        timestamp = "2025-05-19T10:32:45Z",
        battery = BatteryStatus(73, 38, 34.7, ChargingState.DISCHARGING),
        motor = MotorStatus(52.4, 61.2),
        rideSettings = RideSettings("enduro", 80, 45, 60),
        session = SessionData(3742, 24.7, 94.1),
        diagnostics = DiagnosticsData(emptyList(), emptyList()),
    )

    @Test
    fun `invoke delegates to repository and returns success`() = runTest {
        coEvery { repository.getTelemetrySnapshot() } returns Result.success(fakeSnapshot)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(fakeSnapshot, result.getOrNull())
        coVerify(exactly = 1) { repository.getTelemetrySnapshot() }
    }

    @Test
    fun `invoke delegates to repository and returns failure on error`() = runTest {
        val error = IOException("Network failure")
        coEvery { repository.getTelemetrySnapshot() } returns Result.failure(error)

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
        coVerify(exactly = 1) { repository.getTelemetrySnapshot() }
    }
}
