package com.stark.varg.presentation.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.domain.model.SessionData
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.domain.model.WarningSeverity
import com.stark.varg.domain.util.UnitConverter
import com.stark.varg.presentation.util.VargStrings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber
import com.stark.varg.ui.theme.VargWhite

/**
 * Dashboard services:
 * - Active Alert banner with direct diagnostic link
 * - Services section with full-width horizontal rounded capsule cards (Power Modes, Motor, Telemetry, Diagnostics)
 * - Strictly shows data from JSON snapshot (no invented telemetry)
 */
@Composable
fun PortraitDashboardCard(
    battery: BatteryStatus,
    settings: RideSettings,
    motor: MotorStatus,
    session: SessionData,
    diagnostics: DiagnosticsData,
    selectedMode: String,
    activeMaxPowerHp: Int = settings.maxPowerHp,
    unitSystem: UnitSystem = UnitSystem.METRIC,
    language: AppLanguage = AppLanguage.SPANISH,
    modifier: Modifier = Modifier,
    onOpenBatteryOptions: () -> Unit = {},
    onOpenStats: () -> Unit = {},
    onOpenModes: () -> Unit = {},
    onOpenMotor: () -> Unit = {},
    onOpenDiagnostics: () -> Unit = {},
) {
    val hasAlert = diagnostics.warnings.isNotEmpty() || diagnostics.faultCodes.isNotEmpty()
    val hasCritical = diagnostics.warnings.any { it.severity == WarningSeverity.CRITICAL } || diagnostics.faultCodes.isNotEmpty()
    val alertAccent = if (hasCritical) VargStarkRed else VargWarningAmber
    val primaryWarning = diagnostics.warnings.firstOrNull()?.message ?: "Check diagnostics status"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(VargPitchBlack)
            .padding(horizontal = 18.dp, vertical = 6.dp),
    ) {
        // Systems Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = VargStrings.systemsHeader(language),
                style = StarkTypographyTokens.TechnicalHeader,
                letterSpacing = 1.5.sp,
            )
        }

        Spacer(Modifier.height(10.dp))

        // Wide Horizontal Capsule Cards (Full Screen Navigation in Dark & Bronze)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Power Modes Card (Bronze Accent)
            val hpUnit = if (language == AppLanguage.SPANISH) "CV" else "HP"
            val modeDisplayName = com.stark.varg.presentation.util.VargModeUtil.getDisplayName(selectedMode, settings.powerMap)
            DashboardServiceCard(
                iconContent = { VargIconModes(color = VargSecondary, sizeDp = 18.dp) },
                title = VargStrings.servicePowerModes(language),
                subtitle = "$modeDisplayName • $activeMaxPowerHp $hpUnit",
                onClick = onOpenModes,
            )

            // Motor & Inverter Powertrain Card (Bronze Accent)
            val (motorTempVal, motorTempUnit) = UnitConverter.formatTemperature(motor.temperatureC, unitSystem)
            DashboardServiceCard(
                iconContent = { VargIconMotor(color = VargSecondary, sizeDp = 18.dp) },
                title = VargStrings.serviceMotor(language),
                subtitle = "$selectedMode • ${motor.powerHp} $hpUnit • $motorTempVal $motorTempUnit",
                onClick = onOpenMotor,
            )

            // Session & Circuit Telemetry Card (Bronze Accent)
            val (distVal, distUnit) = UnitConverter.formatDistance(session.distanceKm, unitSystem)
            val (speedVal, speedUnit) = UnitConverter.formatSpeed(session.maxSpeedKmh, unitSystem)
            DashboardServiceCard(
                iconContent = { VargIconStats(color = VargSecondary, sizeDp = 18.dp) },
                title = VargStrings.serviceTelemetry(language),
                subtitle = "$distVal $distUnit • Peak $speedVal $speedUnit",
                onClick = onOpenStats,
            )

            // System Diagnostics Card (Highlighted with active advisory when alert present)
            DashboardServiceCard(
                iconContent = {
                    VargIconDiagnostics(
                        color = if (hasAlert) alertAccent else VargSecondary,
                        sizeDp = 18.dp,
                    )
                },
                title = VargStrings.serviceDiagnostics(language),
                subtitle = if (hasAlert) {
                    primaryWarning
                } else {
                    if (language == AppLanguage.SPANISH) "Sistemas Operativos" else "All Systems Operational"
                },
                borderColor = if (hasAlert) alertAccent.copy(alpha = 0.55f) else Color(0xFF26262C),
                onClick = onOpenDiagnostics,
            )
        }

        Spacer(Modifier.height(32.dp))
    }
}

/**
 * Wide Capsule Feature Card:
 * [ Icon ]  Title / Subtitle                >
 */
@Composable
private fun DashboardServiceCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = Color(0xFF26262C),
    iconContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF161619),
                        Color(0xFF111113),
                    ),
                ),
            )
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .semantics {
                contentDescription = "$title, $subtitle. Tap to configure."
                role = Role.Button
            }
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 15.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Left: Icon in slightly rounded rectangular container with elegant dark obsidian background
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1C1C22))
                        .border(1.dp, VargSecondary.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    iconContent()
                }

                Spacer(Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        style = StarkTypographyTokens.CardTitle,
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = subtitle,
                        style = StarkTypographyTokens.CardSubtitle,
                    )
                }
            }

            // Right: Refined bronze chevron
            VargIconChevronRight(color = VargSecondary.copy(alpha = 0.75f), sizeDp = 14.dp)
        }
    }
}

