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
import androidx.compose.foundation.layout.width
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
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.domain.util.UnitConverter
import com.stark.varg.presentation.components.VargPrecisionMeter
import com.stark.varg.presentation.components.VargScreenTopAppBar
import com.stark.varg.presentation.util.VargStrings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber
import kotlin.math.roundToInt
import com.stark.varg.ui.theme.VargWhite

@Composable
fun MotorScreen(
    motor: MotorStatus,
    settings: RideSettings,
    selectedMode: String = "M2",
    activeMaxPowerHp: Int = settings.maxPowerHp,
    timestamp: String,
    onBack: () -> Unit,
    unitSystem: UnitSystem = UnitSystem.METRIC,
    language: AppLanguage = AppLanguage.SPANISH,
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
        // Standard Navigation TopAppBar
        val hpUnit = if (language == AppLanguage.SPANISH) "CV" else "HP"
        val modeDisplayName = com.stark.varg.presentation.util.VargModeUtil.getDisplayName(selectedMode, settings.powerMap)
        VargScreenTopAppBar(
            title = VargStrings.motorTitle(language),
            subtitle = VargStrings.motorSubtitle(language, modeDisplayName, activeMaxPowerHp, hpUnit),
            onBack = onBack,
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
                    MotorPowerMeterCard(
                        motor = motor,
                        settings = settings,
                        selectedMode = selectedMode,
                        activeMaxPowerHp = activeMaxPowerHp,
                        hpUnit = hpUnit,
                        language = language,
                    )
                    MotorTempRow(
                        motor = motor,
                        selectedMode = selectedMode,
                        activeMaxPowerHp = activeMaxPowerHp,
                        tempColor = tempColor,
                        unitSystem = unitSystem,
                        hpUnit = hpUnit,
                        language = language,
                    )
                }

                // Right Column: Configured Parameters
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                ) {
                    MotorConfigCard(
                        settings = settings,
                        selectedMode = selectedMode,
                        activeMaxPowerHp = activeMaxPowerHp,
                        timestamp = timestamp,
                        language = language,
                        hpUnit = hpUnit,
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

                MotorPowerMeterCard(
                    motor = motor,
                    settings = settings,
                    selectedMode = selectedMode,
                    activeMaxPowerHp = activeMaxPowerHp,
                    hpUnit = hpUnit,
                    language = language,
                )

                Spacer(Modifier.height(14.dp))

                MotorTempRow(
                    motor = motor,
                    selectedMode = selectedMode,
                    activeMaxPowerHp = activeMaxPowerHp,
                    tempColor = tempColor,
                    unitSystem = unitSystem,
                    hpUnit = hpUnit,
                    language = language,
                )

                Spacer(Modifier.height(14.dp))

                MotorConfigCard(
                    settings = settings,
                    selectedMode = selectedMode,
                    activeMaxPowerHp = activeMaxPowerHp,
                    timestamp = timestamp,
                    language = language,
                    hpUnit = hpUnit,
                )
            }
        }
    }
}

@Composable
private fun MotorPowerMeterCard(
    motor: MotorStatus,
    settings: RideSettings,
    selectedMode: String,
    activeMaxPowerHp: Int,
    hpUnit: String,
    language: AppLanguage,
) {
    val maxVal = if (activeMaxPowerHp > 0) activeMaxPowerHp else settings.maxPowerHp
    val quarter = (maxVal * 0.25).roundToInt()
    val half = (maxVal * 0.5).roundToInt()
    val threeQuarter = (maxVal * 0.75).roundToInt()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(18.dp),
    ) {
        VargPrecisionMeter(
            title = if (language == AppLanguage.SPANISH) "⚡ Potencia Instantánea de Salida" else "⚡ Instantaneous Power Output",
            unit = hpUnit.lowercase(),
            value = motor.powerHp.toFloat(),
            maxValue = maxVal.toFloat(),
            displayValue = "${motor.powerHp}",
            accentColor = VargSecondary,
            tickSteps = listOf(0, quarter, half, threeQuarter, maxVal),
            sideLabel = "$selectedMode • ${com.stark.varg.presentation.util.VargModeUtil.getMapName(selectedMode, settings.powerMap)}",
        )
    }
}

@Composable
private fun MotorTempRow(
    motor: MotorStatus,
    selectedMode: String,
    activeMaxPowerHp: Int,
    tempColor: Color,
    unitSystem: UnitSystem = UnitSystem.METRIC,
    hpUnit: String,
    language: AppLanguage,
) {
    val (tempVal, tempUnit) = UnitConverter.formatTemperature(motor.temperatureC, unitSystem)
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
                    color = tempColor,
                    style = StarkTypographyTokens.MetricMedium,
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = tempUnit,
                    color = tempColor,
                    style = StarkTypographyTokens.MetricUnit,
                    modifier = Modifier.padding(bottom = 2.dp),
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (language == AppLanguage.SPANISH) "TEMPERATURA NÚCLEO" else "CORE TEMPERATURE",
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
                    text = "$activeMaxPowerHp",
                    color = VargWhite,
                    style = StarkTypographyTokens.MetricMedium,
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = hpUnit,
                    color = VargSecondary,
                    style = StarkTypographyTokens.MetricUnit,
                    modifier = Modifier.padding(bottom = 2.dp),
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (language == AppLanguage.SPANISH) "POTENCIA MÁX ($selectedMode)" else "MAX POWER ($selectedMode)",
                style = StarkTypographyTokens.TechnicalHeader,
            )
        }
    }
}

@Composable
private fun MotorConfigCard(
    settings: RideSettings,
    selectedMode: String,
    activeMaxPowerHp: Int,
    timestamp: String,
    language: AppLanguage,
    hpUnit: String,
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
            text = if (language == AppLanguage.SPANISH) "PARÁMETROS DEL MOTOR CONFIGURADOS" else "CONFIGURED MOTOR PARAMETERS",
            style = StarkTypographyTokens.TechnicalHeader,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                if (language == AppLanguage.SPANISH) "Modo de Potencia Activo" else "Active Power Mode",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text(
                selectedMode,
                color = VargSecondary,
                style = StarkTypographyTokens.ValueMedium.copy(fontWeight = FontWeight.Bold),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                if (language == AppLanguage.SPANISH) "Mapa de Potencia" else "Power Map",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text(com.stark.varg.presentation.util.VargModeUtil.getMapName(selectedMode, settings.powerMap), color = VargWhite, style = StarkTypographyTokens.ValueMedium)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                if (language == AppLanguage.SPANISH) "Potencia Máxima Calibrada" else "Calibrated Max Power",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text("$activeMaxPowerHp $hpUnit", color = VargWhite, style = StarkTypographyTokens.ValueMedium)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                if (language == AppLanguage.SPANISH) "Freno Motor (Engine Braking)" else "Engine Braking Setting",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text("${settings.engineBrakingPct}%", color = VargWhite, style = StarkTypographyTokens.ValueMedium)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                if (language == AppLanguage.SPANISH) "Regeneración" else "Regeneration Setting",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text("${settings.regenPct}%", color = VargWhite, style = StarkTypographyTokens.ValueMedium)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                if (language == AppLanguage.SPANISH) "Timestamp de Telemetría" else "Telemetry Timestamp",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text(timestamp, color = VargTextSecondary, style = StarkTypographyTokens.ScaleTick)
        }
    }
}

private val ColorDarkTile = VargSurfaceCard
