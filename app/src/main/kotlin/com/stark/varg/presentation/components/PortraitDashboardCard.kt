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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.domain.model.SessionData
import com.stark.varg.domain.model.WarningSeverity
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
        // Active Alert Warning Banner (Orange / Red based on criticality)
        if (hasAlert) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF2C2825),
                                Color(0xFF262320),
                            ),
                        ),
                    )
                    .border(1.dp, alertAccent.copy(alpha = 0.55f), RoundedCornerShape(10.dp))
                    .clickable { onOpenDiagnostics() }
                    .padding(horizontal = 16.dp, vertical = 13.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        VargIconDiagnostics(color = alertAccent, sizeDp = 16.dp)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = primaryWarning,
                            color = VargWhite,
                            style = StarkTypographyTokens.CardSubtitle.copy(color = VargWhite),
                        )
                    }
                    Text(
                        text = "VIEW →",
                        color = alertAccent,
                        style = StarkTypographyTokens.TechnicalLabel.copy(color = alertAccent),
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
        }

        // "SERVICES" Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "SERVICES",
                style = StarkTypographyTokens.TechnicalHeader,
                letterSpacing = 1.5.sp,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("<", style = StarkTypographyTokens.TechnicalLabel.copy(color = VargSecondary.copy(alpha = 0.6f)))
                Text(">", style = StarkTypographyTokens.TechnicalLabel.copy(color = VargSecondary.copy(alpha = 0.6f)))
            }
        }

        Spacer(Modifier.height(10.dp))

        // Wide Horizontal Capsule Cards (Full Screen Navigation in Dark & Bronze)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Power Modes Card (Bronze Accent)
            DashboardServiceCard(
                iconContent = { VargIconModes(color = VargSecondary, sizeDp = 18.dp) },
                title = "Power Modes",
                subtitle = "$selectedMode Profile • $activeMaxPowerHp HP",
                onClick = onOpenModes,
            )

            // Motor & Inverter Powertrain Card (Bronze Accent)
            DashboardServiceCard(
                iconContent = { VargIconMotor(color = VargSecondary, sizeDp = 18.dp) },
                title = "Motor & Powertrain",
                subtitle = "${motor.powerHp} HP • ${motor.temperatureC}°C",
                onClick = onOpenMotor,
            )

            // Session & Circuit Telemetry Card (Bronze Accent)
            DashboardServiceCard(
                iconContent = { VargIconStats(color = VargSecondary, sizeDp = 18.dp) },
                title = "Trip & Circuit Telemetry",
                subtitle = "${session.distanceKm} km • Peak ${session.maxSpeedKmh} km/h",
                onClick = onOpenStats,
            )

            // System Diagnostics Card (Orange / Red alert indicator according to severity)
            DashboardServiceCard(
                iconContent = {
                    VargIconDiagnostics(
                        color = if (hasAlert) alertAccent else VargSecondary,
                        sizeDp = 18.dp,
                    )
                },
                title = "System Diagnostics",
                subtitle = if (hasAlert) "${diagnostics.warnings.size} Active Advisory" else "All Systems Operational",
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
            .border(1.dp, Color(0xFF26262C), RoundedCornerShape(14.dp))
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

