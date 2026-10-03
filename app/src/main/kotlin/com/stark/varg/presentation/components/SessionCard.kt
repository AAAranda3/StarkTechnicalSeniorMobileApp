package com.stark.varg.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.SessionData
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary

@Composable
fun SessionCard(session: SessionData, modifier: Modifier = Modifier) {
    StarkCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("session_card"),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "SESSION TELEMETRY",
                    color = VargTextSecondary,
                    style = StarkTypographyTokens.TechnicalHeader,
                )
                StarkPill(
                    text = "LIVE SESSION",
                    backgroundColor = VargPillDark,
                    borderColor = VargBorderSubtle,
                    textColor = VargTextSecondary,
                )
            }

            Spacer(Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                StatItem(label = "Duration", value = session.durationS.toFormattedDuration())
                StatItem(label = "Distance", value = "${session.distanceKm} km")
                StatItem(label = "Max Speed", value = "${session.maxSpeedKmh} km/h")
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = VargTextPrimary,
            style = StarkTypographyTokens.MetricSmall,
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = label.uppercase(),
            color = VargTextSecondary,
            style = StarkTypographyTokens.TechnicalLabel,
        )
    }
}

private fun Int.toFormattedDuration(): String {
    val h = this / 3600
    val m = (this % 3600) / 60
    val s = this % 60
    return "%d:%02d:%02d".format(h, m, s)
}
