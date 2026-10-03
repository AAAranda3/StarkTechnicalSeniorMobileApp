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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
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
fun PowerModesModal(
    settings: RideSettings,
    motor: MotorStatus,
    onDismiss: () -> Unit,
) {
    val modes = listOf("M1", "M2", "M3", "M4", "M5")
    var selectedMode by remember { mutableStateOf("M3") }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth(0.94f)
            .background(VargSurfaceDark, RoundedCornerShape(20.dp))
            .border(1.dp, VargBorder, RoundedCornerShape(20.dp)),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            // Modal Top Bar: Title & Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "POWER MODES",
                    color = VargWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
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
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Mode Pills Row (Photo 3)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                modes.forEach { mode ->
                    val isSelected = mode == selectedMode
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
                            .clickable { selectedMode = mode }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = mode,
                            color = if (isSelected) com.stark.varg.ui.theme.VargPitchBlack else VargTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "MODE 3 • ${settings.powerMap.uppercase()}",
                color = VargWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(16.dp))

            // Power Gauge Slider Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VargSurfaceCard, RoundedCornerShape(14.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
                    .padding(16.dp),
            ) {
                VargPrecisionMeter(
                    title = "⚡ Power",
                    unit = "hp",
                    value = motor.powerHp.toFloat(),
                    maxValue = settings.maxPowerHp.toFloat(),
                    displayValue = "${motor.powerHp}",
                    accentColor = VargWhite,
                    tickSteps = listOf(0, 20, 40, 60, 80),
                    sideLabel = "ALPHA",
                )
            }

            Spacer(Modifier.height(14.dp))

            // Regenerative Braking Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VargSurfaceCard, RoundedCornerShape(14.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
                    .padding(16.dp),
            ) {
                VargPrecisionMeter(
                    title = "◎ Regenerative braking",
                    unit = "%",
                    value = settings.regenPct.toFloat(),
                    maxValue = 100f,
                    displayValue = "${settings.regenPct}",
                    accentColor = VargStarkRed,
                    tickSteps = listOf(0, 20, 40, 60, 80, 100),
                )
            }

            Spacer(Modifier.height(14.dp))

            // Engine Braking Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VargSurfaceCard, RoundedCornerShape(14.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
                    .padding(16.dp),
            ) {
                VargPrecisionMeter(
                    title = "⚙ Engine braking",
                    unit = "%",
                    value = settings.engineBrakingPct.toFloat(),
                    maxValue = 100f,
                    displayValue = "${settings.engineBrakingPct}",
                    accentColor = VargWhite,
                    tickSteps = listOf(0, 20, 40, 60, 80, 100),
                )
            }

            Spacer(Modifier.height(18.dp))

            // Done / Close Button
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
                    text = "CLOSE MODES",
                    color = VargTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
            }
        }
    }
}
