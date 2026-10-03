package com.stark.varg.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.stark.varg.presentation.components.VargPrecisionMeter
import com.stark.varg.presentation.components.VargScreenTopAppBar
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber
import com.stark.varg.ui.theme.VargWhite

@Composable
fun MotorScreen(
    motor: MotorStatus,
    settings: RideSettings,
    timestamp: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isTempElevated = motor.temperatureC >= 60.0
    val tempColor = if (isTempElevated) VargWarningAmber else VargElectricGreen

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
            title = "MOTOR & POWERTRAIN",
            subtitle = "Active Map: ${settings.powerMap.uppercase()}",
            onBack = onBack,
            statusText = "${motor.powerHp} HP",
            statusColor = VargSecondary,
        )

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Left Column: Power Meter & Temp HUD
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MotorPowerMeterCard(motor = motor, settings = settings)
                    MotorTempRow(motor = motor, settings = settings, tempColor = tempColor)
                }

                // Right Column: Configured Parameters
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                ) {
                    MotorConfigCard(settings = settings, timestamp = timestamp)
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

                MotorPowerMeterCard(motor = motor, settings = settings)

                Spacer(Modifier.height(14.dp))

                MotorTempRow(motor = motor, settings = settings, tempColor = tempColor)

                Spacer(Modifier.height(14.dp))

                MotorConfigCard(settings = settings, timestamp = timestamp)
            }
        }
    }
}

@Composable
private fun MotorPowerMeterCard(
    motor: MotorStatus,
    settings: RideSettings,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(18.dp),
    ) {
        VargPrecisionMeter(
            title = "⚡ Instantaneous Power Output",
            unit = "hp",
            value = motor.powerHp.toFloat(),
            maxValue = settings.maxPowerHp.toFloat(),
            displayValue = "${motor.powerHp}",
            accentColor = VargSecondary,
            tickSteps = listOf(0, 20, 40, 60, 80),
            sideLabel = settings.powerMap.uppercase(),
        )
    }
}

@Composable
private fun MotorTempRow(
    motor: MotorStatus,
    settings: RideSettings,
    tempColor: Color,
) {
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
                text = "${motor.temperatureC}°C",
                color = tempColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "CORE TEMPERATURE",
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
                text = "${settings.maxPowerHp} HP",
                color = VargWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "CALIBRATED MAX POWER",
                color = VargTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
        }
    }
}

@Composable
private fun MotorConfigCard(
    settings: RideSettings,
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
            text = "CONFIGURED MOTOR PARAMETERS",
            color = VargWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Power Map", color = VargTextSecondary, fontSize = 13.sp)
            Text(settings.powerMap.uppercase(), color = VargWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Configured Max Power", color = VargTextSecondary, fontSize = 13.sp)
            Text("${settings.maxPowerHp} HP", color = VargWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Engine Braking Setting", color = VargTextSecondary, fontSize = 13.sp)
            Text("${settings.engineBrakingPct}%", color = VargWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Regeneration Setting", color = VargTextSecondary, fontSize = 13.sp)
            Text("${settings.regenPct}%", color = VargWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Telemetry Timestamp", color = VargTextSecondary, fontSize = 13.sp)
            Text(timestamp, color = VargTextSecondary, fontSize = 11.sp)
        }
    }
}

private val ColorDarkTile = VargSurfaceCard
