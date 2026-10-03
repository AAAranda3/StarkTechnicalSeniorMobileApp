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
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.ui.theme.VargBorder
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargCyan
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargSurfaceDark
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber
import com.stark.varg.ui.theme.VargWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotorModal(
    motor: MotorStatus,
    onDismiss: () -> Unit,
) {
    val isTempElevated = motor.temperatureC >= 60.0
    val tempColor = if (isTempElevated) VargWarningAmber else VargElectricGreen

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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⚙",
                        color = VargElectricGreen,
                        fontSize = 18.sp,
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = "POWERTRAIN & MOTOR",
                        color = VargWhite,
                        fontSize = 15.sp,
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

            Spacer(Modifier.height(16.dp))

            // Primary Output Meter
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VargSurfaceCard, RoundedCornerShape(14.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
                    .padding(16.dp),
            ) {
                VargPrecisionMeter(
                    title = "⚡ Instantaneous Power",
                    unit = "hp",
                    value = motor.powerHp.toFloat(),
                    maxValue = 80f,
                    displayValue = "${motor.powerHp}",
                    accentColor = VargCyan,
                    tickSteps = listOf(0, 20, 40, 60, 80),
                    sideLabel = "ALPHA",
                )
            }

            Spacer(Modifier.height(12.dp))

            // Telemetry Grid: Temp & Inverter Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(VargSurfaceCard, RoundedCornerShape(12.dp))
                        .border(1.dp, VargBorderSubtle, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                ) {
                    Text(
                        text = "${motor.temperatureC}°C",
                        color = tempColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "CORE TEMP",
                        color = VargTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(VargSurfaceCard, RoundedCornerShape(12.dp))
                        .border(1.dp, VargBorderSubtle, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                ) {
                    Text(
                        text = if (isTempElevated) "WARM" else "OPTIMAL",
                        color = tempColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "THERMAL STATE",
                        color = VargTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // System Inverter & Stator Stats
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VargSurfaceCard, RoundedCornerShape(12.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(12.dp))
                    .padding(14.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Carbon Sleeve Inverter", color = VargTextSecondary, fontSize = 12.sp)
                    Text("Active (360V)", color = VargWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Peak Torque", color = VargTextSecondary, fontSize = 12.sp)
                    Text("938 Nm (Wheel)", color = VargWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Liquid Cooling Loop", color = VargTextSecondary, fontSize = 12.sp)
                    Text("Circulating", color = VargElectricGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(18.dp))

            // Close Button
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
                    text = "CLOSE MOTOR",
                    color = VargTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
            }
        }
    }
}
