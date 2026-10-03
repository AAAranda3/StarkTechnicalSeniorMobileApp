package com.stark.varg.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.BikeInfo
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.SessionData
import com.stark.varg.domain.model.WarningSeverity
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargGreenDot
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceDark
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber

@Composable
fun VargTopBar(
    bike: BikeInfo,
    battery: BatteryStatus,
    session: SessionData,
    diagnostics: DiagnosticsData,
    modifier: Modifier = Modifier,
    onAlertsClick: () -> Unit = {},
    onBatteryClick: () -> Unit = {},
) {
    val durationFormatted = formatDuration(session.durationS)
    val hasCritical = diagnostics.warnings.any { it.severity == WarningSeverity.CRITICAL } || diagnostics.faultCodes.isNotEmpty()
    val alertColor = if (hasCritical) VargStarkRed else VargWarningAmber

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(VargSurfaceDark)
            .padding(horizontal = 16.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left: VARG Brand + Model
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "VARG",
                color = VargTextPrimary,
                style = StarkTypographyTokens.BrandTitle,
            )
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(13.dp)
                    .background(VargStarkRed, RoundedCornerShape(1.dp)),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = bike.variant.uppercase(),
                color = VargTextSecondary,
                style = StarkTypographyTokens.TechnicalHeader,
            )
        }

        // Center: Session Odometer + Connected Indicator
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "$durationFormatted   ${session.distanceKm} km",
                color = VargTextPrimary,
                style = StarkTypographyTokens.ValueMedium,
            )
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(VargGreenDot, CircleShape),
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "Connected",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
        }

        // Right: Clickable Active Alerts Button & Green Battery Capsule
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (diagnostics.warnings.isNotEmpty() || diagnostics.faultCodes.isNotEmpty()) {
                val totalAlerts = diagnostics.warnings.size + diagnostics.faultCodes.size
                Box(
                    modifier = Modifier
                        .background(alertColor.copy(alpha = 0.18f), RoundedCornerShape(50))
                        .border(1.dp, alertColor.copy(alpha = 0.6f), RoundedCornerShape(50))
                        .clickable { onAlertsClick() }
                        .padding(horizontal = 9.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = "⚠ $totalAlerts ALERT",
                        color = alertColor,
                        style = StarkTypographyTokens.TechnicalLabel,
                    )
                }
                Spacer(Modifier.width(8.dp))
            }

            // Battery Capsule Indicator (Electric Green with Black Text)
            Box(
                modifier = Modifier
                    .background(VargElectricGreen, RoundedCornerShape(50))
                    .clickable { onBatteryClick() }
                    .padding(horizontal = 10.dp, vertical = 3.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⚡",
                        color = VargPitchBlack,
                        fontSize = 11.sp,
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = "${battery.stateOfChargePct}",
                        color = VargPitchBlack,
                        style = StarkTypographyTokens.MetricMini.copy(
                            color = VargPitchBlack,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }
            }
        }
    }
}

private fun formatDuration(durationS: Int): String {
    val h = durationS / 3600
    val m = (durationS % 3600) / 60
    return "${h}h ${m}m"
}
