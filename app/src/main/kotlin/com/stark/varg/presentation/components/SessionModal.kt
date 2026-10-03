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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.SessionData
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorder
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargSurfaceDark
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionModal(
    session: SessionData,
    onDismiss: () -> Unit,
) {
    val avgSpeed = if (session.durationS > 0) {
        val hours = session.durationS / 3600.0
        "%.1f km/h".format(session.distanceKm / hours)
    } else {
        "-- km/h"
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .background(VargSurfaceDark, RoundedCornerShape(20.dp))
            .border(1.dp, VargBorder, RoundedCornerShape(20.dp)),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "SESSION STATISTICS",
                    color = VargWhite,
                    style = StarkTypographyTokens.ModalTitle,
                )
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
                        style = StarkTypographyTokens.CardTitle,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MetricBox(label = "Total Distance", value = "${session.distanceKm} km", modifier = Modifier.weight(1f))
                MetricBox(label = "Max Speed", value = "${session.maxSpeedKmh} km/h", modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MetricBox(label = "Session Duration", value = formatFullDuration(session.durationS), modifier = Modifier.weight(1f))
                MetricBox(label = "Avg Speed", value = avgSpeed, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(18.dp))

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
                    style = StarkTypographyTokens.ButtonLabel,
                )
            }
        }
    }
}

@Composable
private fun MetricBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(VargSurfaceCard, RoundedCornerShape(12.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(12.dp))
            .padding(14.dp),
    ) {
        Text(
            text = value,
            color = VargWhite,
            style = StarkTypographyTokens.MetricSmall,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label.uppercase(),
            color = VargTextSecondary,
            style = StarkTypographyTokens.TechnicalLabel,
        )
    }
}

private fun formatFullDuration(durationS: Int): String {
    val h = durationS / 3600
    val m = (durationS % 3600) / 60
    val s = durationS % 60
    return "%dh %02dm %02ds".format(h, m, s)
}
