package com.stark.varg.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.ChargingState
import com.stark.varg.domain.model.SavedPowerMode
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.domain.model.UserPreferences
import com.stark.varg.domain.repository.UserPreferencesRepository
import com.stark.varg.domain.usecase.GetTelemetryUseCase
import com.stark.varg.domain.usecase.ObserveLiveTelemetryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val userPreferencesRepository: UserPreferencesRepository,
    private val observeLiveTelemetryUseCase: ObserveLiveTelemetryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TelemetryUiState>(TelemetryUiState.Loading)
    val uiState: StateFlow<TelemetryUiState> = _uiState.asStateFlow()

    // Interactive State (Maintained across screen transitions)
    private val _currentDestination = MutableStateFlow<TelemetryDestination>(TelemetryDestination.Dashboard)
    val currentDestination: StateFlow<TelemetryDestination> = _currentDestination.asStateFlow()

    private val _selectedMode = MutableStateFlow("M3")
    val selectedMode: StateFlow<String> = _selectedMode.asStateFlow()

    // Unit System (Metric vs Imperial)
    private val _unitSystem = MutableStateFlow(UnitSystem.METRIC)
    val unitSystem: StateFlow<UnitSystem> = _unitSystem.asStateFlow()

    // Application Language (Spanish vs English)
    private val _language = MutableStateFlow(AppLanguage.SPANISH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    // Live CAN-Bus Stream Mode State
    private val _isLiveStreamActive = MutableStateFlow(false)
    val isLiveStreamActive: StateFlow<Boolean> = _isLiveStreamActive.asStateFlow()

    private var liveStreamJob: Job? = null

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

    // Battery Charging Process State
    private val _isCharging = MutableStateFlow(false)
    val isCharging: StateFlow<Boolean> = _isCharging.asStateFlow()

    private val _chargingPowerKw = MutableStateFlow(3.3) // Stark Integrated Stand Charger (3.3 kW)
    val chargingPowerKw: StateFlow<Double> = _chargingPowerKw.asStateFlow()

    private var chargingSimulationJob: Job? = null

    init {
        observeUserPreferences()
        loadTelemetry()
    }

    fun toggleLiveStream() {
        if (_isLiveStreamActive.value) {
            stopLiveStream()
        } else {
            startLiveStream()
        }
    }

    private fun startLiveStream() {
        _isLiveStreamActive.value = true
        liveStreamJob?.cancel()
        liveStreamJob = viewModelScope.launch {
            observeLiveTelemetryUseCase().collect { snapshot ->
                _uiState.value = TelemetryUiState.Success(snapshot)
            }
        }
    }

    private fun stopLiveStream() {
        _isLiveStreamActive.value = false
        liveStreamJob?.cancel()
        liveStreamJob = null
    }

    private fun observeUserPreferences() {
        viewModelScope.launch {
            userPreferencesRepository.preferencesFlow.collect { prefs ->
                _selectedMode.value = prefs.selectedMode
                _chargeTargetPct.value = prefs.chargeTargetPct
                _unitSystem.value = prefs.unitSystem
                _language.value = prefs.language

                val mappedProfiles = prefs.modeProfiles.mapValues { (_, saved) ->
                    PowerModeProfile(
                        maxPowerHp = saved.maxPowerHp,
                        engineBrakingPct = saved.engineBrakingPct,
                        regenPct = saved.regenPct,
                    )
                }
                _modeProfiles.value = mappedProfiles

                val active = mappedProfiles[prefs.selectedMode] ?: PowerModeProfile(80, 45, 60)
                _maxPowerHp.value = active.maxPowerHp
                _engineBrakingPct.value = active.engineBrakingPct
                _regenPct.value = active.regenPct
            }
        }
    }

    fun loadTelemetry() {
        chargingSimulationJob?.cancel()
        liveStreamJob?.cancel()
        _isLiveStreamActive.value = false
        viewModelScope.launch {
            _uiState.value = TelemetryUiState.Loading
            getTelemetryUseCase().fold(
                onSuccess = { snapshot ->
                    // Initialize M3 from snapshot only if not customized yet
                    val currentProfiles = _modeProfiles.value.toMutableMap()
                    if (!currentProfiles.containsKey("M3")) {
                        val m3Profile = PowerModeProfile(
                            maxPowerHp = snapshot.rideSettings.maxPowerHp,
                            engineBrakingPct = snapshot.rideSettings.engineBrakingPct,
                            regenPct = snapshot.rideSettings.regenPct,
                        )
                        currentProfiles["M3"] = m3Profile
                        _modeProfiles.value = currentProfiles
                    }

                    val active = currentProfiles[_selectedMode.value] ?: PowerModeProfile(80, 45, 60)
                    _maxPowerHp.value = active.maxPowerHp
                    _engineBrakingPct.value = active.engineBrakingPct
                    _regenPct.value = active.regenPct

                    _isCharging.value = (snapshot.battery.chargingState == ChargingState.CHARGING)
                    _uiState.value = TelemetryUiState.Success(snapshot)

                    if (_isCharging.value) {
                        startChargingSimulation()
                    }
                },
                onFailure = { _uiState.value = TelemetryUiState.Error(it.message ?: "Unknown error") },
            )
        }
    }

    fun refreshTelemetry() {
        loadTelemetry()
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
        viewModelScope.launch {
            userPreferencesRepository.saveSelectedMode(mode)
        }
    }

    fun updateMaxPower(hp: Int) {
        val mode = _selectedMode.value
        val current = _modeProfiles.value[mode] ?: PowerModeProfile(hp, 45, 60)
        val updated = current.copy(maxPowerHp = hp)
        _modeProfiles.value += (mode to updated)
        _maxPowerHp.value = hp
        viewModelScope.launch {
            userPreferencesRepository.savePowerModeProfile(
                mode = mode,
                profile = SavedPowerMode(updated.maxPowerHp, updated.engineBrakingPct, updated.regenPct),
            )
        }
    }

    fun updateEngineBraking(pct: Int) {
        val mode = _selectedMode.value
        val current = _modeProfiles.value[mode] ?: PowerModeProfile(80, pct, 60)
        val updated = current.copy(engineBrakingPct = pct)
        _modeProfiles.value += (mode to updated)
        _engineBrakingPct.value = pct
        viewModelScope.launch {
            userPreferencesRepository.savePowerModeProfile(
                mode = mode,
                profile = SavedPowerMode(updated.maxPowerHp, updated.engineBrakingPct, updated.regenPct),
            )
        }
    }

    fun updateRegen(pct: Int) {
        val mode = _selectedMode.value
        val current = _modeProfiles.value[mode] ?: PowerModeProfile(80, 45, pct)
        val updated = current.copy(regenPct = pct)
        _modeProfiles.value += (mode to updated)
        _regenPct.value = pct
        viewModelScope.launch {
            userPreferencesRepository.savePowerModeProfile(
                mode = mode,
                profile = SavedPowerMode(updated.maxPowerHp, updated.engineBrakingPct, updated.regenPct),
            )
        }
    }

    fun updateChargeTarget(target: Int) {
        _chargeTargetPct.value = target
        viewModelScope.launch {
            userPreferencesRepository.saveChargeTargetPct(target)
        }
        // If currently charging and already reached or exceeded target, gracefully stop charging
        val currentState = _uiState.value
        if (currentState is TelemetryUiState.Success && _isCharging.value) {
            if (currentState.snapshot.battery.stateOfChargePct >= target) {
                stopCharging()
            }
        }
    }

    fun toggleUnitSystem() {
        val next = if (_unitSystem.value == UnitSystem.METRIC) UnitSystem.IMPERIAL else UnitSystem.METRIC
        setUnitSystem(next)
    }

    fun setUnitSystem(system: UnitSystem) {
        _unitSystem.value = system
        viewModelScope.launch {
            userPreferencesRepository.saveUnitSystem(system)
        }
    }

    fun selectLanguage(lang: AppLanguage) {
        _language.value = lang
        viewModelScope.launch {
            userPreferencesRepository.saveLanguage(lang)
        }
    }

    fun restoreFactoryModes() {
        val defaults = UserPreferences.defaultModeProfiles()
        _modeProfiles.value = defaults.mapValues { (_, saved) ->
            PowerModeProfile(saved.maxPowerHp, saved.engineBrakingPct, saved.regenPct)
        }
        val current = defaults[_selectedMode.value] ?: SavedPowerMode(80, 45, 60)
        _maxPowerHp.value = current.maxPowerHp
        _engineBrakingPct.value = current.engineBrakingPct
        _regenPct.value = current.regenPct
        viewModelScope.launch {
            defaults.forEach { (mode, prof) ->
                userPreferencesRepository.savePowerModeProfile(mode, prof)
            }
        }
    }

    /**
     * Interactive Charging Process:
     * Connects/disconnects charger and manages charging cycle simulation.
     */
    fun toggleCharging() {
        if (_isCharging.value) {
            stopCharging()
        } else {
            startCharging()
        }
    }

    fun startCharging() {
        _isCharging.value = true
        val currentState = _uiState.value
        if (currentState is TelemetryUiState.Success) {
            val updatedBattery = currentState.snapshot.battery.copy(chargingState = ChargingState.CHARGING)
            _uiState.value = TelemetryUiState.Success(currentState.snapshot.copy(battery = updatedBattery))
        }
        startChargingSimulation()
    }

    fun stopCharging() {
        _isCharging.value = false
        chargingSimulationJob?.cancel()
        val currentState = _uiState.value
        if (currentState is TelemetryUiState.Success) {
            val updatedBattery = currentState.snapshot.battery.copy(chargingState = ChargingState.DISCHARGING)
            _uiState.value = TelemetryUiState.Success(currentState.snapshot.copy(battery = updatedBattery))
        }
    }

    private fun startChargingSimulation() {
        chargingSimulationJob?.cancel()
        chargingSimulationJob = viewModelScope.launch {
            while (_isCharging.value) {
                delay(2500L)
                val currentState = _uiState.value
                if (currentState is TelemetryUiState.Success) {
                    val currentBattery = currentState.snapshot.battery
                    val target = _chargeTargetPct.value

                    if (currentBattery.stateOfChargePct >= target) {
                        _isCharging.value = false
                        val fullBattery = currentBattery.copy(chargingState = ChargingState.UNKNOWN)
                        _uiState.value = TelemetryUiState.Success(currentState.snapshot.copy(battery = fullBattery))
                        break
                    } else {
                        val newPct = (currentBattery.stateOfChargePct + 1).coerceAtMost(100)
                        val newRange = (currentBattery.estimatedRangeKm + 1).coerceAtMost(100)
                        val chargingBattery = currentBattery.copy(
                            stateOfChargePct = newPct,
                            estimatedRangeKm = newRange,
                            chargingState = ChargingState.CHARGING,
                        )
                        _uiState.value = TelemetryUiState.Success(currentState.snapshot.copy(battery = chargingBattery))
                    }
                }
            }
        }
    }
}
