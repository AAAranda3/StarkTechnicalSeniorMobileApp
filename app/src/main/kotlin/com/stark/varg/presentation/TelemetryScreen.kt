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
import com.stark.varg.presentation.screens.BatteryScreen
import com.stark.varg.presentation.screens.DiagnosticsScreen
import com.stark.varg.presentation.screens.MotorScreen
import com.stark.varg.presentation.screens.PowerModesScreen
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

    // Intercept hardware / gesture back navigation when in sub-screens
    BackHandler(enabled = currentDestination != TelemetryDestination.Dashboard) {
        viewModel.navigateBack()
    }

    Scaffold(
        containerColor = VargPitchBlack,
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
                                // Landscape Mode: Adaptive 2-Column Dashboard
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(VargPitchBlack)
                                        .padding(horizontal = 16.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                ) {
                                    // Left Column: Motorcycle Hero Showcase
                                    Column(
                                        modifier = Modifier
                                            .weight(0.44f)
                                            .fillMaxHeight()
                                            .verticalScroll(rememberScrollState()),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        BikeShowcase(
                                            bike = snap.bike,
                                            battery = snap.battery,
                                            diagnostics = snap.diagnostics,
                                            onOpenBatteryOptions = { viewModel.navigateTo(TelemetryDestination.Battery) },
                                            onOpenDiagnostics = { viewModel.navigateTo(TelemetryDestination.Diagnostics) },
                                        )
                                    }

                                    // Right Column: Controls, Precision Meter & Service Cards
                                    Column(
                                        modifier = Modifier
                                            .weight(0.56f)
                                            .fillMaxHeight()
                                            .verticalScroll(rememberScrollState()),
                                    ) {
                                        PortraitDashboardCard(
                                            battery = snap.battery,
                                            settings = snap.rideSettings,
                                            motor = snap.motor,
                                            session = snap.session,
                                            diagnostics = snap.diagnostics,
                                            selectedMode = selectedMode,
                                            activeMaxPowerHp = maxPowerHp,
                                            onOpenBatteryOptions = { viewModel.navigateTo(TelemetryDestination.Battery) },
                                            onOpenStats = { viewModel.navigateTo(TelemetryDestination.Stats) },
                                            onOpenModes = { viewModel.navigateTo(TelemetryDestination.Modes) },
                                            onOpenMotor = { viewModel.navigateTo(TelemetryDestination.Motor) },
                                            onOpenDiagnostics = { viewModel.navigateTo(TelemetryDestination.Diagnostics) },
                                        )
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState()),
                                ) {
                                    // Section 1: Hero Motorcycle Showcase
                                    BikeShowcase(
                                        bike = snap.bike,
                                        battery = snap.battery,
                                        diagnostics = snap.diagnostics,
                                        onOpenBatteryOptions = { viewModel.navigateTo(TelemetryDestination.Battery) },
                                        onOpenDiagnostics = { viewModel.navigateTo(TelemetryDestination.Diagnostics) },
                                    )

                                    // Section 2: Stealth Black Section with Centered Battery/Autonomy & 4 Dark Grey Tiles
                                    PortraitDashboardCard(
                                        battery = snap.battery,
                                        settings = snap.rideSettings,
                                        motor = snap.motor,
                                        session = snap.session,
                                        diagnostics = snap.diagnostics,
                                        selectedMode = selectedMode,
                                        activeMaxPowerHp = maxPowerHp,
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
                            )
                        }
                        TelemetryDestination.Motor -> {
                            MotorScreen(
                                motor = snap.motor,
                                settings = snap.rideSettings,
                                timestamp = snap.timestamp,
                                onBack = { viewModel.navigateBack() },
                            )
                        }
                        TelemetryDestination.Stats -> {
                            StatsScreen(
                                session = snap.session,
                                timestamp = snap.timestamp,
                                onBack = { viewModel.navigateBack() },
                            )
                        }
                        TelemetryDestination.Diagnostics -> {
                            DiagnosticsScreen(
                                diagnostics = snap.diagnostics,
                                timestamp = snap.timestamp,
                                onBack = { viewModel.navigateBack() },
                            )
                        }
                        TelemetryDestination.Battery -> {
                            BatteryScreen(
                                battery = snap.battery,
                                timestamp = snap.timestamp,
                                selectedTarget = chargeTargetPct,
                                onChargeTargetChange = { viewModel.updateChargeTarget(it) },
                                onBack = { viewModel.navigateBack() },
                            )
                        }
                    }
                }
            }
        }
    }
}
