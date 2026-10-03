package com.stark.varg.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

@Composable
fun RideSettingsCard(settings: RideSettings, modifier: Modifier = Modifier) {
    val modes = listOf("M1", "M2", "M3 (ENDURO)", "M4", "M5")

    StarkCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ride_settings_card"),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Mode Pills Selector (from Photo 3)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                modes.forEach { mode ->
                    val isSelected = mode.contains(settings.powerMap, ignoreCase = true) || mode.startsWith("M3")
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) VargWhite else VargPillDark,
                                shape = RoundedCornerShape(50),
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) VargWhite else VargBorderSubtle,
                                shape = RoundedCornerShape(50),
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = if (mode.contains("(")) mode.substringBefore(" (") else mode,
                            color = if (isSelected) com.stark.varg.ui.theme.VargPitchBlack else VargTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Semantic Title & Subtitle for tests and clarity
            Text(
                text = "Map: ${settings.powerMap.replaceFirstChar { it.uppercase() }} • Max ${settings.maxPowerHp} HP",
                color = VargTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Engine braking ${settings.engineBrakingPct}% • Regen ${settings.regenPct}%",
                color = VargTextSecondary,
                fontSize = 12.sp,
            )

            Spacer(Modifier.height(16.dp))

            // Regenerative Braking Precision Meter (Photo 3)
            VargPrecisionMeter(
                title = "◎ Regenerative braking",
                unit = "%",
                value = settings.regenPct.toFloat(),
                maxValue = 100f,
                displayValue = "${settings.regenPct}",
                accentColor = VargStarkRed,
                tickSteps = listOf(0, 20, 40, 60, 80, 100),
            )

            Spacer(Modifier.height(16.dp))

            // Engine Braking Precision Meter
            VargPrecisionMeter(
                title = "⚙ Engine braking",
                unit = "%",
                value = settings.engineBrakingPct.toFloat(),
                maxValue = 100f,
                displayValue = "${settings.engineBrakingPct}",
                accentColor = com.stark.varg.ui.theme.VargWarningAmber,
                tickSteps = listOf(0, 20, 40, 60, 80, 100),
            )
        }
    }
}
