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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.domain.model.SessionData
import com.stark.varg.domain.model.WarningSeverity
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargCyan
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargTextMuted
import androidx.compose.ui.graphics.Color
import com.stark.varg.ui.theme.VargWhite

/**
 * Quick Action Grid for ride parameters and telemetry management.
 * Features large touch targets for glove-friendly interaction while riding.
 */
@Composable
fun VargActionGrid(
    settings: RideSettings,
    motor: MotorStatus,
    session: SessionData,
    diagnostics: DiagnosticsData,
    modifier: Modifier = Modifier,
    isLandscape: Boolean = false,
    onOpenPowerModes: () -> Unit = {},
    onOpenSession: () -> Unit = {},
    onOpenDiagnostics: () -> Unit = {},
    onOpenBatteryOptions: () -> Unit = {},
) {
    val hasAlert = diagnostics.warnings.isNotEmpty() || diagnostics.faultCodes.isNotEmpty()
    val hasCritical = diagnostics.warnings.any { it.severity == WarningSeverity.CRITICAL } || diagnostics.faultCodes.isNotEmpty()
    val alertAccent = if (hasCritical) VargStarkRed else VargWarningAmber

    // Determine colors based on layout orientation
    val gridBg = if (isLandscape) androidx.compose.ui.graphics.Color.Transparent else VargWhite
    val tileBg = if (isLandscape) VargSurfaceCard else Color(0xFFF5F5F7) // light gray for portrait tile
    val tileBorder = if (isLandscape) VargBorderSubtle else Color(0xFFE5E5EA)
    val tileTextColor = if (isLandscape) VargWhite else VargPitchBlack
    val tileSubtitleColor = if (isLandscape) VargTextSecondary else VargTextMuted

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(gridBg)
            .padding(if (isLandscape) 0.dp else 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // MODES & MOTOR Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Tile: MODES (Opens Power Modes Modal with M1-M5 & Sliders)
            ActionTile(
                modifier = Modifier.weight(1f),
                icon = "⚡",
                iconColor = if (isLandscape) VargCyan else VargPitchBlack,
                title = "MODES",
                subtitle = "${settings.powerMap.uppercase()} • ${settings.maxPowerHp} HP",
                badge = "PRO",
                bg = tileBg,
                border = tileBorder,
                textColor = tileTextColor,
                subtitleColor = tileSubtitleColor,
                onClick = onOpenPowerModes,
            )

            // Tile: MOTOR (Displays current motor telemetry)
            ActionTile(
                modifier = Modifier.weight(1f),
                icon = "⚙",
                iconColor = if (isLandscape) VargElectricGreen else VargPitchBlack,
                title = "MOTOR",
                subtitle = "${motor.powerHp} HP • ${motor.temperatureC}°C",
                badge = "NOMINAL",
                bg = tileBg,
                border = tileBorder,
                textColor = tileTextColor,
                subtitleColor = tileSubtitleColor,
                onClick = onOpenPowerModes, // Could have its own modal if requested
            )
        }

        // STATS & DIAGNOSTICS Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Tile: STATS (Opens Trip Summary Modal)
            ActionTile(
                modifier = Modifier.weight(1f),
                icon = "📊",
                iconColor = if (isLandscape) VargWhite else VargPitchBlack,
                title = "STATS",
                subtitle = "${session.distanceKm} km • Max ${session.maxSpeedKmh}",
                badge = "TRIP",
                bg = tileBg,
                border = tileBorder,
                textColor = tileTextColor,
                subtitleColor = tileSubtitleColor,
                onClick = onOpenSession,
            )

            // Tile: DIAGNOSTICS (Opens Diagnostics Modal)
            ActionTile(
                modifier = Modifier.weight(1f),
                icon = if (hasAlert) "⚠" else "✓",
                iconColor = if (hasAlert) alertAccent else (if (isLandscape) VargElectricGreen else VargPitchBlack),
                title = "DIAGNOSTICS",
                subtitle = if (hasAlert) "${diagnostics.warnings.size} Advisory" else "System Nominal",
                badge = if (hasAlert) "ACTIVE" else "OK",
                bg = tileBg,
                border = tileBorder,
                textColor = tileTextColor,
                subtitleColor = tileSubtitleColor,
                onClick = onOpenDiagnostics,
            )
        }
    }
}

@Composable
private fun ActionTile(
    icon: String,
    iconColor: androidx.compose.ui.graphics.Color,
    title: String,
    subtitle: String,
    badge: String,
    bg: androidx.compose.ui.graphics.Color,
    border: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color,
    subtitleColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(14.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(if (bg == VargSurfaceCard) VargPillDark else Color(0xFFE5E5EA), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = icon,
                        color = iconColor,
                        fontSize = 16.sp,
                    )
                }

                StarkPill(
                    text = badge,
                    backgroundColor = if (bg == VargSurfaceCard) VargPillDark else Color(0xFFE5E5EA),
                    borderColor = border,
                    textColor = subtitleColor,
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = title,
                color = textColor,
                style = StarkTypographyTokens.CardTitle,
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = subtitleColor,
                style = StarkTypographyTokens.CardSubtitle,
                maxLines = 1,
            )
        }
    }
}
