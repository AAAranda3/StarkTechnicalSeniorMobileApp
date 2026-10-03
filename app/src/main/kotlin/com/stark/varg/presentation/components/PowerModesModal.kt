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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorder
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargPitchBlack
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

            // Mode Pills Row (Square tiles matching card hegemony)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                modes.forEach { mode ->
                    val isSelected = mode == selectedMode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                color = if (isSelected) VargWhite else VargSurfaceCard,
                                shape = RoundedCornerShape(10.dp),
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) VargWhite else VargBorderSubtle,
                                shape = RoundedCornerShape(10.dp),
                            )
                            .clickable { selectedMode = mode }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            VargIconModes(
                                color = if (isSelected) VargPitchBlack else VargTextSecondary.copy(alpha = 0.5f),
                                sizeDp = 12.dp,
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = mode,
                                color = if (isSelected) VargPitchBlack else VargTextSecondary,
                                style = StarkTypographyTokens.CardTitle.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                ),
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "$selectedMode • ${settings.powerMap.uppercase()}",
                color = VargWhite,
                style = StarkTypographyTokens.ScreenTitle,
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
                    style = StarkTypographyTokens.ButtonLabel,
                )
            }
        }
    }
}
