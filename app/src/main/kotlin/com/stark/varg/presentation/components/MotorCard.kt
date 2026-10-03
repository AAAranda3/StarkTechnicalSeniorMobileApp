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
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.ui.theme.VargCyan
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber

@Composable
fun MotorCard(motor: MotorStatus, modifier: Modifier = Modifier) {
    val isTempElevated = motor.temperatureC >= 60.0

    StarkCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("motor_card"),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Hidden semantic / high-contrast text for testing and accessibility
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${motor.powerHp} HP",
                    color = VargCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Temp ${motor.temperatureC}°C",
                    color = if (isTempElevated) VargWarningAmber else VargTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(Modifier.height(8.dp))

            // Precision Power Meter from Official Varg App (Photo 3)
            VargPrecisionMeter(
                title = "⚡ Power",
                unit = "hp",
                value = motor.powerHp.toFloat(),
                maxValue = 80f,
                displayValue = "${motor.powerHp}",
                accentColor = VargCyan,
                tickSteps = listOf(0, 20, 40, 60, 80),
                sideLabel = "ALPHA",
            )
        }
    }
}
