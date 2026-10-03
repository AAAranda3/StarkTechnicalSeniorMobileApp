package com.stark.varg.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stark.varg.domain.usecase.GetTelemetryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PowerModeProfile(
    val maxPowerHp: Int,
    val engineBrakingPct: Int,
    val regenPct: Int,
)

@HiltViewModel
class TelemetryViewModel @Inject constructor(
    private val getTelemetryUseCase: GetTelemetryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TelemetryUiState>(TelemetryUiState.Loading)
    val uiState: StateFlow<TelemetryUiState> = _uiState.asStateFlow()

    // Interactive State (Maintained across screen transitions)
    private val _currentDestination = MutableStateFlow<TelemetryDestination>(TelemetryDestination.Dashboard)
    val currentDestination: StateFlow<TelemetryDestination> = _currentDestination.asStateFlow()

    private val _selectedMode = MutableStateFlow("M3")
    val selectedMode: StateFlow<String> = _selectedMode.asStateFlow()

    // Independent configurations per mode: M1, M2, M3, M4, M5
    private val _modeProfiles = MutableStateFlow<Map<String, PowerModeProfile>>(
        mapOf(
            "M1" to PowerModeProfile(maxPowerHp = 35, engineBrakingPct = 25, regenPct = 40),
            "M2" to PowerModeProfile(maxPowerHp = 50, engineBrakingPct = 35, regenPct = 50),
            "M3" to PowerModeProfile(maxPowerHp = 80, engineBrakingPct = 45, regenPct = 60),
            "M4" to PowerModeProfile(maxPowerHp = 70, engineBrakingPct = 50, regenPct = 65),
            "M5" to PowerModeProfile(maxPowerHp = 80, engineBrakingPct = 60, regenPct = 75),
        )
    )
    val modeProfiles: StateFlow<Map<String, PowerModeProfile>> = _modeProfiles.asStateFlow()

    // Values strictly for the currently selected mode
    private val _maxPowerHp = MutableStateFlow(80)
    val maxPowerHp: StateFlow<Int> = _maxPowerHp.asStateFlow()

    private val _engineBrakingPct = MutableStateFlow(45)
    val engineBrakingPct: StateFlow<Int> = _engineBrakingPct.asStateFlow()

    private val _regenPct = MutableStateFlow(60)
    val regenPct: StateFlow<Int> = _regenPct.asStateFlow()

    private val _chargeTargetPct = MutableStateFlow(80)
    val chargeTargetPct: StateFlow<Int> = _chargeTargetPct.asStateFlow()

    init {
        loadTelemetry()
    }

    fun loadTelemetry() {
        viewModelScope.launch {
            _uiState.value = TelemetryUiState.Loading
            getTelemetryUseCase().fold(
                onSuccess = { snapshot ->
                    // Initialize M3 (active mode from JSON snapshot) with its actual telemetry values
                    val updated = _modeProfiles.value.toMutableMap()
                    val m3Profile = PowerModeProfile(
                        maxPowerHp = snapshot.rideSettings.maxPowerHp,
                        engineBrakingPct = snapshot.rideSettings.engineBrakingPct,
                        regenPct = snapshot.rideSettings.regenPct,
                    )
                    updated["M3"] = m3Profile
                    _modeProfiles.value = updated

                    val active = updated[_selectedMode.value] ?: m3Profile
                    _maxPowerHp.value = active.maxPowerHp
                    _engineBrakingPct.value = active.engineBrakingPct
                    _regenPct.value = active.regenPct

                    _uiState.value = TelemetryUiState.Success(snapshot)
                },
                onFailure = { _uiState.value = TelemetryUiState.Error(it.message ?: "Unknown error") },
            )
        }
    }

    fun navigateTo(destination: TelemetryDestination) {
        _currentDestination.value = destination
    }

    fun navigateBack() {
        _currentDestination.value = TelemetryDestination.Dashboard
    }

    fun selectMode(mode: String) {
        _selectedMode.value = mode
        val profile = _modeProfiles.value[mode] ?: PowerModeProfile(80, 45, 60)
        _maxPowerHp.value = profile.maxPowerHp
        _engineBrakingPct.value = profile.engineBrakingPct
        _regenPct.value = profile.regenPct
    }

    fun updateMaxPower(hp: Int) {
        val mode = _selectedMode.value
        val current = _modeProfiles.value[mode] ?: PowerModeProfile(hp, 45, 60)
        val updated = current.copy(maxPowerHp = hp)
        _modeProfiles.value += (mode to updated)
        _maxPowerHp.value = hp
    }

    fun updateEngineBraking(pct: Int) {
        val mode = _selectedMode.value
        val current = _modeProfiles.value[mode] ?: PowerModeProfile(80, pct, 60)
        val updated = current.copy(engineBrakingPct = pct)
        _modeProfiles.value += (mode to updated)
        _engineBrakingPct.value = pct
    }

    fun updateRegen(pct: Int) {
        val mode = _selectedMode.value
        val current = _modeProfiles.value[mode] ?: PowerModeProfile(80, 45, pct)
        val updated = current.copy(regenPct = pct)
        _modeProfiles.value += (mode to updated)
        _regenPct.value = pct
    }

    fun updateChargeTarget(target: Int) {
        _chargeTargetPct.value = target
    }
}
