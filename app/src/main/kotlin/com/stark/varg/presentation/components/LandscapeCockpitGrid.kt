package com.stark.varg.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.BikeInfo
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.domain.model.SessionData
import com.stark.varg.domain.model.WarningSeverity
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargCyan
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargSurfaceDark
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber
import com.stark.varg.ui.theme.VargWhite

/**
 * Handlebar Cockpit Grid in Landscape mode matching "THE NEW VARG APP 3.0" photo.
 * Strictly uses data from JSON with unified monochrome Gris / Blanco icons.
 */
@Composable
fun LandscapeCockpitGrid(
    bike: BikeInfo,
    battery: BatteryStatus,
    settings: RideSettings,
    motor: MotorStatus,
    session: SessionData,
    diagnostics: DiagnosticsData,
    selectedMode: String,
    activeMaxPowerHp: Int = settings.maxPowerHp,
    modifier: Modifier = Modifier,
    onOpenModes: () -> Unit = {},
    onOpenMotor: () -> Unit = {},
    onOpenStats: () -> Unit = {},
    onOpenDiagnostics: () -> Unit = {},
    onOpenBatteryOptions: () -> Unit = {},
) {
    val durationFormatted = formatDuration(session.durationS)
    val hasAlert = diagnostics.warnings.isNotEmpty() || diagnostics.faultCodes.isNotEmpty()
    val hasCritical = diagnostics.warnings.any { it.severity == WarningSeverity.CRITICAL } || diagnostics.faultCodes.isNotEmpty()
    val alertAccent = if (hasCritical) VargStarkRed else VargWarningAmber

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Cockpit Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left: Signature VARG brush script + Trip Summary
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "VARG",
                    color = VargWhite,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp,
                    fontFamily = FontFamily.Cursive,
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "$durationFormatted  ${session.distanceKm} km",
                    color = VargWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(VargElectricGreen, CircleShape),
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = "Connected",
                    color = VargTextSecondary,
                    fontSize = 11.sp,
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = bike.model,
                    color = VargTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                )
            }

            // Right: Electric Green Battery Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(VargElectricGreen)
                        .clickable { onOpenBatteryOptions() }
                        .padding(horizontal = 10.dp, vertical = 3.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⚡ ${battery.stateOfChargePct}%",
                            color = VargPitchBlack,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                        )
                    }
                }
            }
        }

        // Row 1: 4 Primary Cockpit Tiles in a Grid with Gris / Blanco Icons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CockpitTile(
                modifier = Modifier.weight(1f),
                iconContent = { VargIconModes(color = VargWhite, sizeDp = 16.dp) },
                title = "Modes",
                subtitle = "$selectedMode • $activeMaxPowerHp HP",
                onClick = onOpenModes,
            )

            CockpitTile(
                modifier = Modifier.weight(1f),
                iconContent = { VargIconMotor(color = VargWhite, sizeDp = 16.dp) },
                title = "Motor",
                subtitle = "${motor.powerHp} HP • ${motor.temperatureC}°C",
                onClick = onOpenMotor,
            )

            CockpitTile(
                modifier = Modifier.weight(1f),
                iconContent = { VargIconStats(color = VargWhite, sizeDp = 16.dp) },
                title = "Stats",
                subtitle = "${session.distanceKm} km • Max ${session.maxSpeedKmh}",
                onClick = onOpenStats,
            )

            CockpitTile(
                modifier = Modifier.weight(1f),
                iconContent = {
                    VargIconDiagnostics(
                        color = if (hasAlert) alertAccent else VargWhite,
                        sizeDp = 16.dp,
                    )
                },
                title = "Diagnostics",
                subtitle = if (hasAlert) "${diagnostics.warnings.size} Advisory" else "Nominal",
                badge = if (hasAlert) "ALERT" else null,
                badgeColor = if (hasAlert) alertAccent else null,
                onClick = onOpenDiagnostics,
            )
        }

        // Row 2: 2 Wider Grid Cards (Trip History & Autonomy Status)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Card 1: Session Telemetry
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorDarkTile)
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(12.dp))
                    .clickable { onOpenStats() }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            VargIconStats(color = VargTextSecondary, sizeDp = 14.dp)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "TRIP TELEMETRY",
                                color = VargTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${session.distanceKm} km  •  $durationFormatted",
                            color = VargWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Text(
                        text = "MAX ${session.maxSpeedKmh} KM/H",
                        color = VargElectricGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            // Card 2: Autonomy & Battery Pack Status
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorDarkTile)
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(12.dp))
                    .clickable { onOpenBatteryOptions() }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = "AUTONOMY & BATTERY",
                            color = VargTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${battery.estimatedRangeKm} km  •  ${battery.stateOfChargePct}%",
                            color = VargWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Text(
                        text = "${battery.temperatureC}°C",
                        color = VargCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        // Active Advisory Bar in Cockpit (if any)
        if (hasAlert) {
            val alertMessage = diagnostics.warnings.firstOrNull()?.message ?: "System Advisory Active"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(alertAccent.copy(alpha = 0.15f))
                    .border(1.dp, alertAccent.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .clickable { onOpenDiagnostics() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VargIconDiagnostics(color = alertAccent, sizeDp = 13.dp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = alertMessage,
                        color = VargWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Text(
                    text = "DETAILS ➔",
                    color = alertAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun CockpitTile(
    iconContent: @Composable () -> Unit,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    badge: String? = null,
    badgeColor: Color? = null,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(ColorDarkTile)
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(10.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF363844), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                iconContent()
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = title,
                color = VargWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = VargTextSecondary,
                fontSize = 9.sp,
                maxLines = 1,
            )
        }
    }
}

private fun formatDuration(durationS: Int): String {
    val h = durationS / 3600
    val m = (durationS % 3600) / 60
    return "${h}h ${m}m"
}

private val ColorDarkTile = VargSurfaceCard
