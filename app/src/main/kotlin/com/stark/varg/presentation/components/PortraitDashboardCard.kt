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
 * 1. Active Alert banner with direct diagnostic link
 * 2. Services section with full-width horizontal rounded capsule cards (Power Modes, Motor, Telemetry, Diagnostics)
 * 3. Strictly shows data from JSON snapshot (no invented telemetry)
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
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                    Text(
                        text = "VIEW →",
                        color = alertAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
        }

        // 3. "SERVICES" Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "SERVICES",
                color = VargTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("<", color = VargSecondary.copy(alpha = 0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(">", color = VargSecondary.copy(alpha = 0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(10.dp))

        // 4. Wide Horizontal Capsule Cards (Full Screen Navigation in Dark & Bronze)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Card 1: Power Modes (Bronze Accent)
            DashboardServiceCard(
                iconContent = { VargIconModes(color = VargSecondary, sizeDp = 18.dp) },
                title = "Power Modes",
                subtitle = "$selectedMode Profile • $activeMaxPowerHp HP",
                badgeText = settings.powerMap.uppercase(),
                badgeColor = Color(0xFFD4D4DC),
                onClick = onOpenModes,
            )

            // Card 2: Motor & Inverter Powertrain (Bronze Accent)
            DashboardServiceCard(
                iconContent = { VargIconMotor(color = VargSecondary, sizeDp = 18.dp) },
                title = "Motor & Powertrain",
                subtitle = "${motor.powerHp} HP • ${motor.temperatureC}°C",
                badgeText = "OK",
                badgeColor = Color(0xFFD4D4DC),
                onClick = onOpenMotor,
            )

            // Card 3: Session & Circuit Telemetry (Bronze Accent)
            DashboardServiceCard(
                iconContent = { VargIconStats(color = VargSecondary, sizeDp = 18.dp) },
                title = "Trip & Circuit Telemetry",
                subtitle = "${session.distanceKm} km • Peak ${session.maxSpeedKmh} km/h",
                badgeText = "TRIP",
                badgeColor = Color(0xFFD4D4DC),
                onClick = onOpenStats,
            )

            // Card 4: System Diagnostics (Orange / Red alert indicator according to severity)
            DashboardServiceCard(
                iconContent = {
                    VargIconDiagnostics(
                        color = if (hasAlert) alertAccent else VargSecondary,
                        sizeDp = 18.dp,
                    )
                },
                title = "System Diagnostics",
                subtitle = if (hasAlert) "${diagnostics.warnings.size} Active Advisory" else "All Systems Operational",
                badgeText = if (hasAlert) "ALERT" else "SAFE",
                badgeColor = if (hasAlert) alertAccent else Color(0xFFD4D4DC),
                onClick = onOpenDiagnostics,
            )
        }

        Spacer(Modifier.height(32.dp))
    }
}

/**
 * Wide Capsule Feature Card:
 * [ Icon ]  Title / Subtitle                [ Badge ]  >
 */
@Composable
private fun DashboardServiceCard(
    title: String,
    subtitle: String,
    badgeText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeColor: Color = Color(0xFFD4D4DC),
    iconContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF2D2E36),
                        Color(0xFF26272F),
                    ),
                ),
            )
            .border(1.dp, Color(0xFF3F414F), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 15.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Left: Icon in slightly rounded rectangular container with grey background for gold contrast
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF363844))
                        .border(1.dp, VargSecondary.copy(alpha = 0.35f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    iconContent()
                }

                Spacer(Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        color = VargWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = subtitle,
                        color = Color(0xFFA0A0AA),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }

            // Right: Status Badge - Rectangular in Technical Grey
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF363844), RoundedCornerShape(4.dp))
                        .border(1.dp, Color(0xFF4C4E5E), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = badgeText,
                        color = Color(0xFFE4E4EE),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp,
                    )
                }

                VargIconChevronRight(color = VargSecondary, sizeDp = 14.dp)
            }
        }
    }
}

