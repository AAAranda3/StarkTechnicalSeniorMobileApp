package com.stark.varg.presentation

import app.cash.turbine.test
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.BikeInfo
import com.stark.varg.domain.model.ChargingState
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.domain.model.SavedPowerMode
import com.stark.varg.domain.model.SessionData
import com.stark.varg.domain.model.TelemetrySnapshot
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.domain.model.UserPreferences
import com.stark.varg.domain.model.Warning
import com.stark.varg.domain.model.WarningSeverity
import com.stark.varg.domain.repository.UserPreferencesRepository
import com.stark.varg.domain.usecase.GetTelemetryUseCase
import com.stark.varg.domain.usecase.ObserveLiveTelemetryUseCase
import com.stark.varg.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FakeUserPreferencesRepository(
    initialPreferences: UserPreferences = UserPreferences(),
) : UserPreferencesRepository {
    private val _prefs = MutableStateFlow(initialPreferences)
    override val preferencesFlow: Flow<UserPreferences> = _prefs.asStateFlow()

    override suspend fun saveSelectedMode(mode: String) {
        _prefs.value = _prefs.value.copy(selectedMode = mode)
    }

    override suspend fun savePowerModeProfile(mode: String, profile: SavedPowerMode) {
        val updated = _prefs.value.modeProfiles.toMutableMap()
        updated[mode] = profile
        _prefs.value = _prefs.value.copy(modeProfiles = updated)
    }

    override suspend fun saveChargeTargetPct(targetPct: Int) {
        _prefs.value = _prefs.value.copy(chargeTargetPct = targetPct)
    }

    override suspend fun saveUnitSystem(unitSystem: UnitSystem) {
        _prefs.value = _prefs.value.copy(unitSystem = unitSystem)
    }

    override suspend fun saveLanguage(language: AppLanguage) {
        _prefs.value = _prefs.value.copy(language = language)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TelemetryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val getTelemetryUseCase: GetTelemetryUseCase = mockk()
    private val observeLiveTelemetryUseCase: ObserveLiveTelemetryUseCase = mockk(relaxed = true)
    private var fakeUserPreferencesRepository = FakeUserPreferencesRepository()

    private fun createViewModel(
        userPreferencesRepository: UserPreferencesRepository = fakeUserPreferencesRepository,
        observeLiveTelemetry: ObserveLiveTelemetryUseCase = observeLiveTelemetryUseCase,
    ): TelemetryViewModel = TelemetryViewModel(getTelemetryUseCase, userPreferencesRepository, observeLiveTelemetry)

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

        val viewModel = createViewModel()

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

        val viewModel = createViewModel()

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

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(TelemetryUiState.Error("Parse failed"), viewModel.uiState.value)
    }

    @Test
    fun `usecase failure with null message emits Unknown error`() = runTest {
        coEvery { getTelemetryUseCase() } returns Result.failure(RuntimeException())

        val viewModel = createViewModel()
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

        val viewModel = createViewModel()

        assertEquals(TelemetryUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `success with no warnings exposes empty warnings list`() = runTest {
        val snapshot = buildSnapshot(warnings = emptyList())
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value as TelemetryUiState.Success
        assertTrue(state.snapshot.diagnostics.warnings.isEmpty())
    }

    @Test
    fun `each power profile is individual and modifying one profile modifies only that profile`() = runTest {
        val snapshot = buildSnapshot()
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = createViewModel()
        advanceUntilIdle()

        // Initial active mode is M3 (80 HP, 45% Engine Braking, 60% Regen)
        assertEquals("M3", viewModel.selectedMode.value)
        assertEquals(80, viewModel.maxPowerHp.value)
        assertEquals(45, viewModel.engineBrakingPct.value)
        assertEquals(60, viewModel.regenPct.value)

        // Switch to M4 and customize it to 56 HP
        viewModel.selectMode("M4")
        assertEquals("M4", viewModel.selectedMode.value)
        viewModel.updateMaxPower(56)
        viewModel.updateEngineBraking(52)
        viewModel.updateRegen(68)

        assertEquals(56, viewModel.maxPowerHp.value)
        assertEquals(52, viewModel.engineBrakingPct.value)
        assertEquals(68, viewModel.regenPct.value)

        // Switch to M1: must have M1's own independent values
        viewModel.selectMode("M1")
        assertEquals("M1", viewModel.selectedMode.value)
        assertEquals(35, viewModel.maxPowerHp.value)
        assertEquals(25, viewModel.engineBrakingPct.value)
        assertEquals(40, viewModel.regenPct.value)

        // Modify M1's max power to 42 HP
        viewModel.updateMaxPower(42)
        assertEquals(42, viewModel.maxPowerHp.value)

        // Switch back to M4: M4 MUST still have 56 HP (not affected by M1's edit)
        viewModel.selectMode("M4")
        assertEquals(56, viewModel.maxPowerHp.value)
        assertEquals(52, viewModel.engineBrakingPct.value)
        assertEquals(68, viewModel.regenPct.value)

        // Switch back to M3: M3 MUST still have 80 HP (not affected by M4 or M1)
        viewModel.selectMode("M3")
        assertEquals(80, viewModel.maxPowerHp.value)
        assertEquals(45, viewModel.engineBrakingPct.value)
        assertEquals(60, viewModel.regenPct.value)

        // Switch back to M1: M1 MUST still have 42 HP
        viewModel.selectMode("M1")
        assertEquals(42, viewModel.maxPowerHp.value)
    }

    @Test
    fun `toggleCharging switches chargingState to CHARGING and back to DISCHARGING`() = runTest {
        val snapshot = buildSnapshot()
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(false, viewModel.isCharging.value)

        // Toggle to start charging
        viewModel.toggleCharging()
        assertTrue(viewModel.isCharging.value)
        val state1 = viewModel.uiState.value as TelemetryUiState.Success
        assertEquals(ChargingState.CHARGING, state1.snapshot.battery.chargingState)

        // Toggle to stop charging
        viewModel.toggleCharging()
        assertEquals(false, viewModel.isCharging.value)
        val state2 = viewModel.uiState.value as TelemetryUiState.Success
        assertEquals(ChargingState.DISCHARGING, state2.snapshot.battery.chargingState)
    }

    @Test
    fun `refreshTelemetry resets uiState to Loading then loads snapshot`() = runTest {
        val snapshot = buildSnapshot()
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            assertEquals(TelemetryUiState.Success(snapshot), awaitItem())

            viewModel.refreshTelemetry()

            assertEquals(TelemetryUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertEquals(TelemetryUiState.Success(snapshot), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Persistence cases ----------------------------------------------------

    @Test
    fun `preferences flow updates selectedMode and custom power profiles in viewModel`() = runTest {
        val snapshot = buildSnapshot()
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.selectMode("M2")
        viewModel.updateMaxPower(48)
        advanceUntilIdle()

        fakeUserPreferencesRepository.preferencesFlow.test {
            val latest = awaitItem()
            assertEquals("M2", latest.selectedMode)
            assertEquals(48, latest.modeProfiles["M2"]?.maxPowerHp)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleUnitSystem alternates between METRIC and IMPERIAL and persists`() = runTest {
        val snapshot = buildSnapshot()
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(UnitSystem.METRIC, viewModel.unitSystem.value)

        viewModel.toggleUnitSystem()
        advanceUntilIdle()
        assertEquals(UnitSystem.IMPERIAL, viewModel.unitSystem.value)

        viewModel.toggleUnitSystem()
        advanceUntilIdle()
        assertEquals(UnitSystem.METRIC, viewModel.unitSystem.value)
    }

    // --- Live Stream cases ----------------------------------------------------

    @Test
    fun `toggleLiveStream toggles stream state and collects live snapshots`() = runTest {
        val initialSnapshot = buildSnapshot()
        val liveSnapshot = buildSnapshot().copy(
            motor = MotorStatus(75.0, 68.0),
            session = SessionData(3800, 25.1, 102.5),
        )

        coEvery { getTelemetryUseCase() } returns Result.success(initialSnapshot)
        coEvery { observeLiveTelemetryUseCase() } returns flowOf(liveSnapshot)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(false, viewModel.isLiveStreamActive.value)

        viewModel.toggleLiveStream()
        advanceUntilIdle()

        assertEquals(true, viewModel.isLiveStreamActive.value)
        val state = viewModel.uiState.value
        assertTrue(state is TelemetryUiState.Success)
        assertEquals(75.0, (state as TelemetryUiState.Success).snapshot.motor.powerHp, 0.01)

        // Toggle again to stop
        viewModel.toggleLiveStream()
        advanceUntilIdle()
        assertEquals(false, viewModel.isLiveStreamActive.value)
    }

    @Test
    fun `selectLanguage updates language and saves to repository`() = runTest {
        val snapshot = buildSnapshot()
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(AppLanguage.SPANISH, viewModel.language.value)

        viewModel.selectLanguage(AppLanguage.ENGLISH)
        advanceUntilIdle()

        assertEquals(AppLanguage.ENGLISH, viewModel.language.value)
    }

    @Test
    fun `navigateTo Settings updates currentDestination and navigateBack returns to Dashboard`() = runTest {
        val snapshot = buildSnapshot()
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(TelemetryDestination.Dashboard, viewModel.currentDestination.value)

        viewModel.navigateTo(TelemetryDestination.Settings)
        assertEquals(TelemetryDestination.Settings, viewModel.currentDestination.value)

        viewModel.navigateBack()
        assertEquals(TelemetryDestination.Dashboard, viewModel.currentDestination.value)
    }

    @Test
    fun `restoreFactoryModes resets customized power modes to factory defaults`() = runTest {
        val snapshot = buildSnapshot()
        coEvery { getTelemetryUseCase() } returns Result.success(snapshot)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateMaxPower(40)
        advanceUntilIdle()
        assertEquals(40, viewModel.maxPowerHp.value)

        viewModel.restoreFactoryModes()
        advanceUntilIdle()
        assertEquals(80, viewModel.maxPowerHp.value)
    }
}
