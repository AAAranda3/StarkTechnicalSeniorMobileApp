package com.stark.varg.presentation.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.presentation.components.VargScreenTopAppBar
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargCyan
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
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
    modifier: Modifier = Modifier,
) {
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .padding(horizontal = 16.dp),
    ) {
        // Standard Navigation TopAppBar (No cutre buttons)
        VargScreenTopAppBar(
            title = "BATTERY & CHARGING",
            subtitle = "State: ${battery.chargingState.name.lowercase()}",
            onBack = onBack,
            statusText = "${battery.stateOfChargePct}%",
            statusColor = VargElectricGreen,
        )

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Left Column: Hero Battery & Temperature
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    BatteryHeroCard(battery = battery)
                    BatteryMetricsRow(battery = battery)
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

                BatteryHeroCard(battery = battery)

                Spacer(Modifier.height(14.dp))

                BatteryMetricsRow(battery = battery)

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
private fun BatteryHeroCard(battery: BatteryStatus) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(16.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(16.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            // Battery capsule
            Box(
                modifier = Modifier
                    .size(width = 30.dp, height = 16.dp)
                    .border(2.dp, VargElectricGreen, RoundedCornerShape(4.dp))
                    .padding(2.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(battery.stateOfChargePct / 100f)
                        .background(VargElectricGreen, RoundedCornerShape(1.dp)),
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = "${battery.stateOfChargePct}%",
                color = VargWhite,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1).sp,
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = "${battery.estimatedRangeKm} KM ESTIMATED AUTONOMY",
            color = VargElectricGreen,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )

        Spacer(Modifier.height(16.dp))

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(VargSurfaceDark, RoundedCornerShape(4.dp)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(battery.stateOfChargePct / 100f)
                    .height(8.dp)
                    .background(VargElectricGreen, RoundedCornerShape(4.dp)),
            )
        }
    }
}

@Composable
private fun BatteryMetricsRow(battery: BatteryStatus) {
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
            Text(
                text = "${battery.temperatureC}°C",
                color = VargSecondary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "PACK TEMPERATURE",
                color = VargTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .background(ColorDarkTile, RoundedCornerShape(14.dp))
                .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
                .padding(16.dp),
        ) {
            Text(
                text = battery.chargingState.name,
                color = VargWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "CHARGING STATE",
                color = VargTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
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
            color = VargWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
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
                        .background(
                            color = if (isSelected) VargSecondary else VargSurfaceDark,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) VargSecondary else VargBorderSubtle,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clickable { onChargeTargetChange(target) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "$target%",
                        color = if (isSelected) VargPitchBlack else VargTextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Telemetry Timestamp", color = VargTextSecondary, fontSize = 12.sp)
            Text(timestamp, color = VargTextSecondary, fontSize = 11.sp)
        }
    }
}

private val ColorDarkTile = VargSurfaceCard
