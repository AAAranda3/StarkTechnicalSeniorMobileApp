package com.stark.varg.presentation.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.ChargingState
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber
import com.stark.varg.ui.theme.VargWhite

/**
 * Priority Hero Card: High-visibility Range & Battery level with quick-access options.
 */
@Composable
fun BatteryCard(
    battery: BatteryStatus,
    modifier: Modifier = Modifier,
    onOpenOptions: () -> Unit = {},
    onToggleCharging: () -> Unit = {},
) {
    val isCharging = battery.chargingState == ChargingState.CHARGING
    val batteryColor = when {
        isCharging -> VargElectricGreen
        battery.stateOfChargePct <= 20 -> VargStarkRed
        battery.stateOfChargePct <= 45 -> VargWarningAmber
        else -> VargElectricGreen
    }

    val chargingLabel = when (battery.chargingState) {
        ChargingState.CHARGING -> "⚡ CHARGING • 3.3 kW"
        ChargingState.DISCHARGING -> "DISCHARGING"
        ChargingState.UNKNOWN -> "STANDBY"
    }

    var selectedThreshold by remember { mutableStateOf("100%") }

    StarkCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenOptions() }
            .testTag("battery_card"),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
        ) {
            // Card Subtitle & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "ESTIMATED AUTONOMY & BATTERY",
                    color = VargTextSecondary,
                    style = StarkTypographyTokens.TechnicalHeader,
                )
                StarkPill(
                    text = chargingLabel,
                    backgroundColor = if (isCharging) VargElectricGreen.copy(alpha = 0.18f) else VargPillDark,
                    borderColor = if (isCharging) VargElectricGreen.copy(alpha = 0.8f) else VargBorderSubtle,
                    textColor = if (isCharging) VargElectricGreen else VargTextSecondary,
                    modifier = Modifier.clickable { onToggleCharging() },
                )
            }

            Spacer(Modifier.height(14.dp))

            // Main Hero Row: Huge Range & Battery Percentage Side-by-Side (Centered)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Range Hero
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${battery.estimatedRangeKm} km",
                        color = VargWhite,
                        style = StarkTypographyTokens.MetricLarge,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "RANGE LEFT",
                        color = VargTextSecondary,
                        style = StarkTypographyTokens.TechnicalLabel,
                    )
                }

                Box(
                    modifier = Modifier
                        .size(width = 1.dp, height = 45.dp)
                        .background(VargBorderSubtle),
                )

                // Battery Hero
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⚡",
                            color = batteryColor,
                            fontSize = 20.sp,
                        )
                        Spacer(Modifier.size(4.dp))
                        Text(
                            text = "${battery.stateOfChargePct}%",
                            color = VargWhite,
                            style = StarkTypographyTokens.MetricLarge,
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "BATTERY CHARGE",
                        color = VargTextSecondary,
                        style = StarkTypographyTokens.TechnicalLabel,
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Gauge Bar: Animated Charging Pulse Beam or Standard Meter
            if (isCharging) {
                val infiniteTransition = rememberInfiniteTransition(label = "BatteryCardChargingPulse")
                val pulseAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.55f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 750, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse,
                    ),
                    label = "ChargingPulseAlpha",
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(VargBorderSubtle),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = (battery.stateOfChargePct / 100f).coerceIn(0f, 1f))
                            .height(8.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF0F5A28),
                                        VargElectricGreen.copy(alpha = pulseAlpha),
                                        Color(0xFF88FFB0),
                                    ),
                                ),
                            ),
                    )
                }
            } else {
                LinearProgressIndicator(
                    progress = { (battery.stateOfChargePct / 100f).coerceIn(0f, 1f) },
                    color = batteryColor,
                    trackColor = VargBorderSubtle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                )
            }

            Spacer(Modifier.height(14.dp))

            // Battery Options Section: Charging Limit Target Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "CHARGE TARGET",
                    color = VargTextSecondary,
                    style = StarkTypographyTokens.TechnicalLabel,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("80%", "90%", "100%").forEach { limit ->
                        val isSelected = limit == selectedThreshold
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) VargElectricGreen else VargPillDark,
                                    shape = RoundedCornerShape(6.dp),
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) VargElectricGreen else VargBorderSubtle,
                                    shape = RoundedCornerShape(6.dp),
                                )
                                .clickable { selectedThreshold = limit }
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = limit,
                                color = if (isSelected) VargPitchBlack else VargWhite,
                                style = StarkTypographyTokens.PillLabel.copy(
                                    color = if (isSelected) VargPitchBlack else VargWhite,
                                ),
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Battery Options Metrics (Cell Temp, Health & Power State)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Cell Temp Chip
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(VargPillDark, RoundedCornerShape(8.dp))
                        .border(1.dp, VargBorderSubtle, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Column {
                        Text(text = "TEMP", color = VargTextSecondary, style = StarkTypographyTokens.ScaleTick)
                        Text(
                            text = "${battery.temperatureC}°C",
                            color = VargWhite,
                            style = StarkTypographyTokens.ValueMedium,
                        )
                    }
                }

                // Health Chip
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(VargPillDark, RoundedCornerShape(8.dp))
                        .border(1.dp, VargBorderSubtle, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Column {
                        Text(text = "HEALTH", color = VargTextSecondary, style = StarkTypographyTokens.ScaleTick)
                        Text(
                            text = "99% Optimal",
                            color = VargElectricGreen,
                            style = StarkTypographyTokens.ValueMedium.copy(color = VargElectricGreen),
                        )
                    }
                }

                // Status Chip
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(VargPillDark, RoundedCornerShape(8.dp))
                        .border(1.dp, VargBorderSubtle, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Column {
                        Text(text = "POWER", color = VargTextSecondary, style = StarkTypographyTokens.ScaleTick)
                        Text(
                            text = battery.chargingState.name.lowercase().replaceFirstChar { it.uppercase() },
                            color = VargWhite,
                            style = StarkTypographyTokens.ValueMedium,
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Battery Options / Health Modal Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VargPillDark, RoundedCornerShape(10.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(10.dp))
                    .clickable { onOpenOptions() }
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🔋",
                        fontSize = 13.sp,
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = "Advanced Battery Management & Diagnostics",
                        color = VargTextPrimary,
                        style = StarkTypographyTokens.CardSubtitle,
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "OPTIONS ›",
                        color = VargElectricGreen,
                        style = StarkTypographyTokens.TechnicalLabel.copy(color = VargElectricGreen),
                    )
                }
            }
        }
    }
}
