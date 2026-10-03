package com.stark.varg.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.presentation.components.VargScreenTopAppBar
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

@Composable
fun PowerModesScreen(
    settings: RideSettings,
    motor: MotorStatus,
    selectedMode: String,
    maxPowerHp: Int,
    engineBrakingPct: Int,
    regenPct: Int,
    onModeSelect: (String) -> Unit,
    onPowerChange: (Int) -> Unit,
    onEngineBrakingChange: (Int) -> Unit,
    onRegenChange: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val modes = listOf("M1", "M2", "M3", "M4", "M5")

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .padding(horizontal = 16.dp),
    ) {
        // Standard Navigation TopAppBar (No cutre buttons)
        VargScreenTopAppBar(
            title = "POWER MODES",
            subtitle = "Active Map: ${settings.powerMap.uppercase()}",
            onBack = onBack,
            statusText = "$maxPowerHp HP",
            statusColor = VargElectricGreen,
        )

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Left Column: Modes & Max Power
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Selectable Modes Pills Row
                    ModesPillsRow(
                        modes = modes,
                        selectedMode = selectedMode,
                        onModeSelect = onModeSelect,
                    )

                    // Header for Selected Mode & Map
                    ModeProfileHeader(
                        selectedMode = selectedMode,
                        settings = settings,
                        motor = motor,
                    )

                    // Interactive Slider 1: Max Power Limit
                    InteractiveSliderCard(
                        title = "MAX POWER LIMIT",
                        unit = "HP",
                        currentValue = maxPowerHp.toFloat(),
                        range = 10f..80f,
                        accentColor = VargSecondary,
                        onValueChange = { onPowerChange(it.toInt()) },
                    )
                }

                // Right Column: Braking Controls
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Interactive Slider 2: Regenerative Braking
                    InteractiveSliderCard(
                        title = "REGENERATIVE BRAKING",
                        unit = "%",
                        currentValue = regenPct.toFloat(),
                        range = 0f..100f,
                        accentColor = VargSecondary,
                        onValueChange = { onRegenChange(it.toInt()) },
                    )

                    // Interactive Slider 3: Engine Braking
                    InteractiveSliderCard(
                        title = "ENGINE BRAKING",
                        unit = "%",
                        currentValue = engineBrakingPct.toFloat(),
                        range = 0f..100f,
                        accentColor = VargSecondary,
                        onValueChange = { onEngineBrakingChange(it.toInt()) },
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp),
            ) {
                Spacer(Modifier.height(8.dp))

                // Selectable Modes Pills Row (Maintains state upon click)
                ModesPillsRow(
                    modes = modes,
                    selectedMode = selectedMode,
                    onModeSelect = onModeSelect,
                )

                Spacer(Modifier.height(20.dp))

                // Header for Selected Mode & Map
                ModeProfileHeader(
                    selectedMode = selectedMode,
                    settings = settings,
                    motor = motor,
                )

                Spacer(Modifier.height(16.dp))

                // Interactive Slider 1: Max Power Limit
                InteractiveSliderCard(
                    title = "MAX POWER LIMIT",
                    unit = "HP",
                    currentValue = maxPowerHp.toFloat(),
                    range = 10f..80f,
                    accentColor = VargSecondary,
                    onValueChange = { onPowerChange(it.toInt()) },
                )

                Spacer(Modifier.height(14.dp))

                // Interactive Slider 2: Regenerative Braking
                InteractiveSliderCard(
                    title = "REGENERATIVE BRAKING",
                    unit = "%",
                    currentValue = regenPct.toFloat(),
                    range = 0f..100f,
                    accentColor = VargSecondary,
                    onValueChange = { onRegenChange(it.toInt()) },
                )

                Spacer(Modifier.height(14.dp))

                // Interactive Slider 3: Engine Braking
                InteractiveSliderCard(
                    title = "ENGINE BRAKING",
                    unit = "%",
                    currentValue = engineBrakingPct.toFloat(),
                    range = 0f..100f,
                    accentColor = VargSecondary,
                    onValueChange = { onEngineBrakingChange(it.toInt()) },
                )
            }
        }
    }
}

@Composable
private fun ModesPillsRow(
    modes: List<String>,
    selectedMode: String,
    onModeSelect: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        modes.forEach { mode ->
            val isSelected = mode == selectedMode
            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) VargSecondary else ColorDarkTile,
                        shape = RoundedCornerShape(6.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) VargSecondary else VargBorderSubtle,
                        shape = RoundedCornerShape(6.dp),
                    )
                    .clickable { onModeSelect(mode) }
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = mode,
                    color = if (isSelected) VargPitchBlack else VargTextSecondary,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun ModeProfileHeader(
    selectedMode: String,
    settings: RideSettings,
    motor: MotorStatus,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$selectedMode PROFILE",
                color = VargWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "MAP: ${settings.powerMap.uppercase()}",
                color = VargElectricGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Current motor delivery: ${motor.powerHp} HP • Temp: ${motor.temperatureC}°C",
            color = VargTextSecondary,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun InteractiveSliderCard(
    title: String,
    unit: String,
    currentValue: Float,
    range: ClosedFloatingPointRange<Float>,
    accentColor: Color,
    onValueChange: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                color = VargWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
            Text(
                text = "${currentValue.toInt()} $unit",
                color = VargWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.height(8.dp))

        Slider(
            value = currentValue,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = Color(0xFF333333),
            ),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${range.start.toInt()}",
                color = VargTextSecondary,
                fontSize = 10.sp,
            )
            Text(
                text = "${range.endInclusive.toInt()} $unit",
                color = VargTextSecondary,
                fontSize = 10.sp,
            )
        }
    }
}

private val ColorDarkTile = VargSurfaceCard
