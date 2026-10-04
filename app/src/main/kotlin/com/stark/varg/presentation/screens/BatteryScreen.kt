package com.stark.varg.presentation.screens

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.ChargingState
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.domain.util.UnitConverter
import com.stark.varg.presentation.components.VargIconPlug
import com.stark.varg.presentation.components.VargScreenTopAppBar
import com.stark.varg.presentation.util.VargStrings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargSurfaceDark
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

@Composable
fun BatteryScreen(
    battery: BatteryStatus,
    timestamp: String,
    selectedTarget: Int,
    onChargeTargetChange: (Int) -> Unit,
    onBack: () -> Unit,
    unitSystem: UnitSystem = UnitSystem.METRIC,
    language: AppLanguage = AppLanguage.SPANISH,
    onToggleCharging: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isCharging = battery.chargingState == ChargingState.CHARGING

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .padding(horizontal = 16.dp),
    ) {
        // Standard Navigation TopAppBar
        VargScreenTopAppBar(
            title = VargStrings.batteryTitle(language),
            subtitle = if (isCharging) VargStrings.batteryStateCharging(language) else VargStrings.batteryStateDischarging(language, battery.chargingState.name),
            onBack = onBack,
        )

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Left Column: Hero Battery, Metrics & Charging Process Card
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    BatteryHeroCard(battery = battery, unitSystem = unitSystem)
                    BatteryChargingProcessCard(
                        battery = battery,
                        selectedTarget = selectedTarget,
                        onToggleCharging = onToggleCharging,
                    )
                    BatteryMetricsRow(battery = battery, unitSystem = unitSystem)
                }

                // Right Column: Target Charge Limit
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                ) {
                    BatteryTargetCard(
                        selectedTarget = selectedTarget,
                        onChargeTargetChange = onChargeTargetChange,
                        timestamp = timestamp,
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

                BatteryHeroCard(battery = battery, unitSystem = unitSystem)

                Spacer(Modifier.height(14.dp))

                BatteryChargingProcessCard(
                    battery = battery,
                    selectedTarget = selectedTarget,
                    onToggleCharging = onToggleCharging,
                )

                Spacer(Modifier.height(14.dp))

                BatteryMetricsRow(battery = battery, unitSystem = unitSystem)

                Spacer(Modifier.height(14.dp))

                BatteryTargetCard(
                    selectedTarget = selectedTarget,
                    onChargeTargetChange = onChargeTargetChange,
                    timestamp = timestamp,
                )
            }
        }
    }
}

@Composable
private fun BatteryHeroCard(
    battery: BatteryStatus,
    unitSystem: UnitSystem = UnitSystem.METRIC,
) {
    val isCharging = battery.chargingState == ChargingState.CHARGING
    val (rangeVal, rangeUnit) = UnitConverter.formatRange(battery.estimatedRangeKm, unitSystem)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(
                width = 1.dp,
                color = if (isCharging) VargElectricGreen.copy(alpha = 0.45f) else VargBorderSubtle,
                shape = RoundedCornerShape(14.dp),
            )
            .semantics {
                contentDescription = "Battery charge ${battery.stateOfChargePct} percent, estimated autonomy $rangeVal $rangeUnit, status ${if (isCharging) "charging" else "discharging"}"
            }
            .padding(18.dp),
    ) {
        // Card Header: Title & Dynamic Status Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "HIGH-VOLTAGE TRACTION PACK",
                style = StarkTypographyTokens.TechnicalHeader,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isCharging) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(VargElectricGreen, CircleShape),
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = "⚡ CHARGING 3.3 kW",
                        color = VargElectricGreen,
                        style = StarkTypographyTokens.TechnicalLabel.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = VargElectricGreen,
                        ),
                    )
                } else {
                    Text(
                        text = "NOMINAL DISCHARGE",
                        color = VargTextSecondary,
                        style = StarkTypographyTokens.TechnicalLabel,
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Dual Hero Metrics: Battery % on Left, Range on Right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            // Left: SoC %
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${battery.stateOfChargePct}",
                        color = VargWhite,
                        style = StarkTypographyTokens.MetricLarge,
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = "%",
                        color = if (isCharging) VargElectricGreen else VargSecondary,
                        style = StarkTypographyTokens.MetricUnit,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "STATE OF CHARGE",
                    style = StarkTypographyTokens.TechnicalHeader.copy(fontSize = 10.sp),
                )
            }

            // Right: Autonomy / Range
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = rangeVal,
                        color = VargWhite,
                        style = StarkTypographyTokens.MetricMedium,
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = rangeUnit.uppercase(),
                        color = VargSecondary,
                        style = StarkTypographyTokens.MetricUnit,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "ESTIMATED RANGE",
                    style = StarkTypographyTokens.TechnicalHeader.copy(fontSize = 10.sp),
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Precision Gauge Bar (matching VargPrecisionMeter track)
        val fraction = (battery.stateOfChargePct / 100f).coerceIn(0f, 1f)
        val pulseTransition = rememberInfiniteTransition(label = "ChargingBeamPulse")
        val beamAlpha by pulseTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 750, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "BeamAlpha",
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFF1E1E24))
                .border(1.dp, Color(0xFF2C2C36), RoundedCornerShape(3.dp)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = if (isCharging) {
                                listOf(
                                    Color(0xFF007A33),
                                    VargElectricGreen.copy(alpha = beamAlpha),
                                    Color(0xFF8BFFB2),
                                )
                            } else {
                                listOf(
                                    Color(0xFF8C7020),
                                    VargSecondary,
                                    Color(0xFFE2C46A),
                                )
                            },
                        ),
                        shape = RoundedCornerShape(3.dp),
                    ),
            )
        }

        Spacer(Modifier.height(6.dp))

        // Scale Ticks at 0, 25, 50, 75, 100%
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            listOf(0, 25, 50, 75, 100).forEach { tick ->
                val isReached = battery.stateOfChargePct >= tick
                Text(
                    text = "$tick%",
                    color = if (isReached) VargWhite else VargTextSecondary.copy(alpha = 0.45f),
                    style = StarkTypographyTokens.ScaleTick.copy(fontSize = 10.sp),
                )
            }
        }
    }
}

/**
 * High-voltage charging process telemetry card with real-time powertrain feed and toggle.
 */
@Composable
private fun BatteryChargingProcessCard(
    battery: BatteryStatus,
    selectedTarget: Int,
    onToggleCharging: () -> Unit,
) {
    val isCharging = battery.chargingState == ChargingState.CHARGING
    val minutesLeft = ((selectedTarget - battery.stateOfChargePct).coerceAtLeast(0) * 1.1).toInt()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(
                width = 1.dp,
                color = if (isCharging) VargElectricGreen.copy(alpha = 0.45f) else VargBorderSubtle,
                shape = RoundedCornerShape(14.dp),
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "CHARGING PROCESS",
                style = StarkTypographyTokens.TechnicalHeader,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(
                            color = if (isCharging) VargElectricGreen else VargTextSecondary,
                            shape = CircleShape,
                        ),
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = if (isCharging) "ACTIVE (3.3 kW)" else "STANDBY",
                    color = if (isCharging) VargElectricGreen else VargTextSecondary,
                    style = StarkTypographyTokens.ScaleTick.copy(fontWeight = FontWeight.SemiBold),
                )
            }
        }

        // Technical Hardware & Feed Specs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(VargSurfaceDark, RoundedCornerShape(10.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Text(
                    text = "CHARGER FEED",
                    color = VargTextSecondary,
                    style = StarkTypographyTokens.TechnicalHeader.copy(fontSize = 10.sp),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (isCharging) "230V • 16A AC (3.3 kW)" else "Standby",
                    color = if (isCharging) VargElectricGreen else VargWhite,
                    style = StarkTypographyTokens.ValueMedium,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(VargSurfaceDark, RoundedCornerShape(10.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Text(
                    text = "TIME TO $selectedTarget%",
                    color = VargTextSecondary,
                    style = StarkTypographyTokens.TechnicalHeader.copy(fontSize = 10.sp),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (isCharging) "~$minutesLeft min" else "~$minutesLeft min est.",
                    color = if (isCharging) VargElectricGreen else VargWhite,
                    style = StarkTypographyTokens.ValueMedium,
                )
            }
        }

        // Action Button
        val haptic = LocalHapticFeedback.current
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (isCharging) Color(0xFF241214) else Color(0xFF1A1A20)
                )
                .border(
                    width = 1.dp,
                    color = if (isCharging) VargStarkRed.copy(alpha = 0.7f) else VargSecondary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp),
                )
                .semantics {
                    contentDescription = if (isCharging) {
                        "Charging active at 3.3 kilowatts. Tap to stop charging and disconnect."
                    } else {
                        "Connect 3.3 kilowatt stand charger and begin charging."
                    }
                    role = Role.Button
                }
                .clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggleCharging()
                }
                .padding(vertical = 13.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VargIconPlug(
                    color = if (isCharging) VargStarkRed else VargSecondary,
                    sizeDp = 16.dp,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isCharging) "DISCONNECT & STOP CHARGING" else "CONNECT CHARGER (3.3 kW)",
                    color = VargWhite,
                    style = StarkTypographyTokens.PillLabel.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        color = VargWhite,
                    ),
                )
            }
        }
    }
}

@Composable
private fun BatteryMetricsRow(battery: BatteryStatus, unitSystem: UnitSystem = UnitSystem.METRIC) {
    val (tempVal, tempUnit) = UnitConverter.formatTemperature(battery.temperatureC, unitSystem)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(ColorDarkTile, RoundedCornerShape(14.dp))
                .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
                .padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = tempVal,
                    color = VargWhite,
                    style = StarkTypographyTokens.MetricMedium,
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = tempUnit,
                    color = VargSecondary,
                    style = StarkTypographyTokens.MetricUnit,
                    modifier = Modifier.padding(bottom = 2.dp),
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "PACK TEMPERATURE",
                style = StarkTypographyTokens.TechnicalHeader,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .background(ColorDarkTile, RoundedCornerShape(14.dp))
                .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
                .padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = battery.chargingState.name.uppercase(),
                    color = if (battery.chargingState == ChargingState.CHARGING) VargElectricGreen else VargWhite,
                    style = StarkTypographyTokens.MetricMedium,
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "CHARGING STATE",
                style = StarkTypographyTokens.TechnicalHeader,
            )
        }
    }
}

@Composable
private fun BatteryTargetCard(
    selectedTarget: Int,
    onChargeTargetChange: (Int) -> Unit,
    timestamp: String,
) {
    val haptic = LocalHapticFeedback.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "TARGET CHARGE LIMIT",
            style = StarkTypographyTokens.TechnicalHeader,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            listOf(80, 90, 100).forEach { target ->
                val isSelected = target == selectedTarget
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .background(
                            color = if (isSelected) VargWhite else ColorDarkTile,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) VargWhite else VargBorderSubtle,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .semantics {
                            contentDescription = "Target charge limit $target percent. ${if (isSelected) "Selected" else "Not selected"}"
                            role = Role.RadioButton
                            selected = isSelected
                        }
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onChargeTargetChange(target)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "$target%",
                        color = if (isSelected) VargPitchBlack else VargTextSecondary,
                        style = StarkTypographyTokens.CardTitle.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                        ),
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Telemetry Timestamp", color = VargTextSecondary, style = StarkTypographyTokens.CardSubtitle)
            Text(timestamp, color = VargWhite, style = StarkTypographyTokens.ScaleTick)
        }
    }
}

private val ColorDarkTile = VargSurfaceCard
