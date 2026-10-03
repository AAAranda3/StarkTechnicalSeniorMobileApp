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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.Warning
import com.stark.varg.domain.model.WarningSeverity
import com.stark.varg.ui.theme.VargBorder
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargSurfaceDark
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber
import com.stark.varg.ui.theme.VargWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticsModal(
    diagnostics: DiagnosticsData,
    onDismiss: () -> Unit,
) {
    val hasCritical = diagnostics.warnings.any { it.severity == WarningSeverity.CRITICAL } || diagnostics.faultCodes.isNotEmpty()
    val headerColor = if (hasCritical) VargStarkRed else VargWarningAmber

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .background(VargSurfaceDark, RoundedCornerShape(20.dp))
            .border(1.dp, VargBorder, RoundedCornerShape(20.dp)),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            // Header with Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⚠",
                        color = headerColor,
                        fontSize = 18.sp,
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = "VEHICLE ALERTS & ADVISORIES",
                        color = VargTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                    )
                }

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(VargPillDark, CircleShape)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "✕",
                        color = VargTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            if (diagnostics.warnings.isEmpty() && diagnostics.faultCodes.isEmpty()) {
                Text(
                    text = "No active warnings. All vehicle sensors reporting nominal.",
                    color = VargTextSecondary,
                    fontSize = 13.sp,
                )
            } else {
                // List of Warnings (Friendly names, NO raw codes like W_MOT_TEMP_HIGH)
                diagnostics.warnings.forEach { warning ->
                    FriendlyWarningItem(warning)
                    Spacer(Modifier.height(8.dp))
                }

                // Any fault advisory
                diagnostics.faultCodes.forEach { _ ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(VargSurfaceCard, RoundedCornerShape(12.dp))
                            .border(1.dp, VargStarkRed.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Diagnostic Sensor Advisory",
                            color = VargStarkRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        StarkPill(
                            text = "ACTIVE",
                            backgroundColor = VargStarkRed.copy(alpha = 0.2f),
                            borderColor = VargStarkRed,
                            textColor = VargStarkRed,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            // Dismiss Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VargSurfaceCard, RoundedCornerShape(12.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(12.dp))
                    .clickable { onDismiss() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "CLOSE",
                    color = VargTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
            }
        }
    }
}

@Composable
private fun FriendlyWarningItem(warning: Warning) {
    val severityColor = when (warning.severity) {
        WarningSeverity.CRITICAL -> VargStarkRed
        WarningSeverity.WARNING -> VargWarningAmber
        WarningSeverity.INFO -> VargWhite
        WarningSeverity.UNKNOWN -> VargTextSecondary
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(VargSurfaceCard, RoundedCornerShape(12.dp))
            .border(1.dp, severityColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Thermal Sensor Advisory",
                color = VargTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
            StarkPill(
                text = warning.severity.name,
                backgroundColor = severityColor.copy(alpha = 0.15f),
                borderColor = severityColor.copy(alpha = 0.4f),
                textColor = severityColor,
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = warning.message,
            color = VargWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Cooling system active. Performance is being regulated.",
            color = VargTextSecondary,
            fontSize = 11.sp,
        )
    }
}
