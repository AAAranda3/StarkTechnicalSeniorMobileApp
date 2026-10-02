package com.stark.varg.presentation

import app.cash.turbine.test
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
import com.stark.varg.domain.usecase.GetTelemetryUseCase
import com.stark.varg.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TelemetryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val getTelemetryUseCase: GetTelemetryUseCase = mockk()

    private fun buildSnapshot(warnings: List<Warning> = listOf(
        Warning("W_MOT_TEMP_HIGH", "Motor temperature elevated", WarningSeverity.WARNING),
    )) = TelemetrySnapshot(
        bike = BikeInfo("Stark VARG MX 1.2", "Alpha", "3.4.1", "https://img.example.com/bike.webp"),
        timestamp = "2025-05-19T10:32:45Z",
        battery = BatteryStatus(73, 38, 34.7, ChargingState.DISCHARGING),
        motor = MotorStatus(52.4, 61.2),
        rideSettings = RideSettings("enduro", 80, 45, 60),
        session = SessionData(3742, 24.7, 94.1),
        diagnostics = DiagnosticsData(emptyList(), warnings),
    )

    // --- Positive cases -------------------------------------------------------

    @Test
    fun `emits Loading then Success on successful repository response`() = runTest {
        val snapshot = buildSnapshot()
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = TelemetryViewModel(getTelemetryUseCase)

        viewModel.uiState.test {
            assertEquals(TelemetryUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertEquals(TelemetryUiState.Success(snapshot), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry after error transitions back to Loading then Success`() = runTest {
        val snapshot = buildSnapshot()
        coEvery { getTelemetryUseCase() } returnsMany listOf(
            Result.failure(RuntimeException("Network error")),
            Result.success(snapshot),
        )

        val viewModel = TelemetryViewModel(getTelemetryUseCase)

        viewModel.uiState.test {
            assertEquals(TelemetryUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertEquals(TelemetryUiState.Error("Network error"), awaitItem())

            viewModel.loadTelemetry()

            assertEquals(TelemetryUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertEquals(TelemetryUiState.Success(snapshot), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Negative cases -------------------------------------------------------

    @Test
    fun `usecase failure emits Error with exception message`() = runTest {
        coEvery { getTelemetryUseCase() } returns Result.failure(RuntimeException("Parse failed"))

        val viewModel = TelemetryViewModel(getTelemetryUseCase)
        advanceUntilIdle()

        assertEquals(TelemetryUiState.Error("Parse failed"), viewModel.uiState.value)
    }

    @Test
    fun `usecase failure with null message emits Unknown error`() = runTest {
        coEvery { getTelemetryUseCase() } returns Result.failure(RuntimeException())

        val viewModel = TelemetryViewModel(getTelemetryUseCase)
        advanceUntilIdle()

        assertEquals(TelemetryUiState.Error("Unknown error"), viewModel.uiState.value)
    }

    // --- Corner cases ---------------------------------------------------------

    @Test
    fun `initial state is Loading before usecase call completes`() = runTest {
        coEvery { getTelemetryUseCase() } coAnswers {
            delay(1_000)
            Result.success(buildSnapshot())
        }

        val viewModel = TelemetryViewModel(getTelemetryUseCase)

        assertEquals(TelemetryUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `success with no warnings exposes empty warnings list`() = runTest {
        val snapshot = buildSnapshot(warnings = emptyList())
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = TelemetryViewModel(getTelemetryUseCase)
        advanceUntilIdle()

        val state = viewModel.uiState.value as TelemetryUiState.Success
        assertTrue(state.snapshot.diagnostics.warnings.isEmpty())
    }
}
