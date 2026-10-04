package com.stark.varg.presentation

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stark.varg.presentation.components.BikeShowcase
import com.stark.varg.presentation.components.ErrorContent
import com.stark.varg.presentation.components.LandscapeCockpitGrid
import com.stark.varg.presentation.components.LoadingContent
import com.stark.varg.presentation.components.PortraitDashboardCard
import com.stark.varg.presentation.components.VargBottomNavigation
import com.stark.varg.presentation.screens.BatteryScreen
import com.stark.varg.presentation.screens.DiagnosticsScreen
import com.stark.varg.presentation.screens.MotorScreen
import com.stark.varg.presentation.screens.PowerModesScreen
import com.stark.varg.presentation.screens.SettingsScreen
import com.stark.varg.presentation.screens.StatsScreen
import com.stark.varg.ui.theme.VargPitchBlack

/**
 * Destinations for full-screen navigation.
 */
sealed interface TelemetryDestination {
    data object Dashboard : TelemetryDestination
    data object Modes : TelemetryDestination
    data object Motor : TelemetryDestination
    data object Stats : TelemetryDestination
    data object Diagnostics : TelemetryDestination
    data object Battery : TelemetryDestination
    data object Settings : TelemetryDestination
}

@Composable
fun TelemetryScreen(
    viewModel: TelemetryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val selectedMode by viewModel.selectedMode.collectAsStateWithLifecycle()
    val maxPowerHp by viewModel.maxPowerHp.collectAsStateWithLifecycle()
    val engineBrakingPct by viewModel.engineBrakingPct.collectAsStateWithLifecycle()
    val regenPct by viewModel.regenPct.collectAsStateWithLifecycle()
    val chargeTargetPct by viewModel.chargeTargetPct.collectAsStateWithLifecycle()
    val unitSystem by viewModel.unitSystem.collectAsStateWithLifecycle()
    val isLiveStreamActive by viewModel.isLiveStreamActive.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()

    // Intercept hardware / gesture back navigation when in sub-screens
    BackHandler(enabled = currentDestination != TelemetryDestination.Dashboard) {
        viewModel.navigateBack()
    }

    Scaffold(
        containerColor = VargPitchBlack,
        bottomBar = {
            if (!isLandscape && uiState is TelemetryUiState.Success) {
                VargBottomNavigation(
                    currentDestination = currentDestination,
                    language = language,
                    onTabSelected = { viewModel.navigateTo(it) },
                )
            }
        },
    ) { paddingValues ->
        when (val state = uiState) {
            is TelemetryUiState.Loading -> LoadingContent(
                modifier = Modifier.padding(paddingValues),
            )
            is TelemetryUiState.Error -> ErrorContent(
                message = state.message,
                onRetry = viewModel::loadTelemetry,
                modifier = Modifier.padding(paddingValues),
            )
            is TelemetryUiState.Success -> {
                val snap = state.snapshot

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(VargPitchBlack),
                ) {
                    when (currentDestination) {
                        TelemetryDestination.Dashboard -> {
                            if (isLandscape) {
                                // Landscape Mode: Dedicated Handlebar Cockpit Grid
                                LandscapeCockpitGrid(
                                    bike = snap.bike,
                                    battery = snap.battery,
                                    settings = snap.rideSettings,
                                    motor = snap.motor,
                                    session = snap.session,
                                    diagnostics = snap.diagnostics,
                                    selectedMode = selectedMode,
                                    activeMaxPowerHp = maxPowerHp,
                                    unitSystem = unitSystem,
                                    language = language,
                                    isLiveStreamActive = isLiveStreamActive,
                                    onToggleUnitSystem = { viewModel.toggleUnitSystem() },
                                    onToggleLiveStream = { viewModel.toggleLiveStream() },
                                    onOpenModes = { viewModel.navigateTo(TelemetryDestination.Modes) },
                                    onOpenMotor = { viewModel.navigateTo(TelemetryDestination.Motor) },
                                    onOpenStats = { viewModel.navigateTo(TelemetryDestination.Stats) },
                                    onOpenDiagnostics = { viewModel.navigateTo(TelemetryDestination.Diagnostics) },
                                    onOpenBatteryOptions = { viewModel.navigateTo(TelemetryDestination.Battery) },
                                    onOpenSettings = { viewModel.navigateTo(TelemetryDestination.Settings) },
                                    onRefresh = { viewModel.loadTelemetry() },
                                )
                            } else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(VargPitchBlack)
                                        .verticalScroll(rememberScrollState()),
                                ) {
                                    // Hero Motorcycle Showcase
                                    BikeShowcase(
                                        bike = snap.bike,
                                        battery = snap.battery,
                                        diagnostics = snap.diagnostics,
                                        unitSystem = unitSystem,
                                        language = language,
                                        isLiveStreamActive = isLiveStreamActive,
                                        onToggleUnitSystem = { viewModel.toggleUnitSystem() },
                                        onToggleLiveStream = { viewModel.toggleLiveStream() },
                                        onOpenBatteryOptions = { viewModel.navigateTo(TelemetryDestination.Battery) },
                                        onOpenDiagnostics = { viewModel.navigateTo(TelemetryDestination.Diagnostics) },
                                        onRefresh = { viewModel.loadTelemetry() },
                                    )

                                    // Stealth Black Section with Centered Battery/Autonomy & Dark Grey Tiles
                                    PortraitDashboardCard(
                                        battery = snap.battery,
                                        settings = snap.rideSettings,
                                        motor = snap.motor,
                                        session = snap.session,
                                        diagnostics = snap.diagnostics,
                                        selectedMode = selectedMode,
                                        activeMaxPowerHp = maxPowerHp,
                                        unitSystem = unitSystem,
                                        language = language,
                                        onOpenBatteryOptions = { viewModel.navigateTo(TelemetryDestination.Battery) },
                                        onOpenStats = { viewModel.navigateTo(TelemetryDestination.Stats) },
                                        onOpenModes = { viewModel.navigateTo(TelemetryDestination.Modes) },
                                        onOpenMotor = { viewModel.navigateTo(TelemetryDestination.Motor) },
                                        onOpenDiagnostics = { viewModel.navigateTo(TelemetryDestination.Diagnostics) },
                                    )
                                }
                            }
                        }
                        TelemetryDestination.Modes -> {
                            PowerModesScreen(
                                settings = snap.rideSettings,
                                motor = snap.motor,
                                selectedMode = selectedMode,
                                maxPowerHp = maxPowerHp,
                                engineBrakingPct = engineBrakingPct,
                                regenPct = regenPct,
                                onModeSelect = { viewModel.selectMode(it) },
                                onPowerChange = { viewModel.updateMaxPower(it) },
                                onEngineBrakingChange = { viewModel.updateEngineBraking(it) },
                                onRegenChange = { viewModel.updateRegen(it) },
                                onBack = { viewModel.navigateBack() },
                                language = language,
                            )
                        }
                        TelemetryDestination.Motor -> {
                            MotorScreen(
                                motor = snap.motor,
                                settings = snap.rideSettings,
                                timestamp = snap.timestamp,
                                unitSystem = unitSystem,
                                language = language,
                                onBack = { viewModel.navigateBack() },
                            )
                        }
                        TelemetryDestination.Stats -> {
                            StatsScreen(
                                session = snap.session,
                                timestamp = snap.timestamp,
                                unitSystem = unitSystem,
                                language = language,
                                onBack = { viewModel.navigateBack() },
                            )
                        }
                        TelemetryDestination.Diagnostics -> {
                            DiagnosticsScreen(
                                diagnostics = snap.diagnostics,
                                timestamp = snap.timestamp,
                                language = language,
                                onBack = { viewModel.navigateBack() },
                            )
                        }
                        TelemetryDestination.Battery -> {
                            BatteryScreen(
                                battery = snap.battery,
                                timestamp = snap.timestamp,
                                selectedTarget = chargeTargetPct,
                                unitSystem = unitSystem,
                                language = language,
                                onChargeTargetChange = { viewModel.updateChargeTarget(it) },
                                onBack = { viewModel.navigateBack() },
                                onToggleCharging = { viewModel.toggleCharging() },
                            )
                        }
                        TelemetryDestination.Settings -> {
                            SettingsScreen(
                                language = language,
                                unitSystem = unitSystem,
                                isLiveStreamActive = isLiveStreamActive,
                                onLanguageChange = { viewModel.selectLanguage(it) },
                                onUnitSystemChange = { viewModel.setUnitSystem(it) },
                                onToggleLiveStream = { viewModel.toggleLiveStream() },
                                onRestoreFactoryModes = { viewModel.restoreFactoryModes() },
                                onBack = { viewModel.navigateBack() },
                            )
                        }
                    }
                }
            }
        }
    }
}
