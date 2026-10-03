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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.ui.theme.VargBorder
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargSurfaceDark
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

/**
 * Battery Health & Charging Options Modal (Inspired by modern EV & Stark design).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatteryOptionsModal(
    battery: BatteryStatus,
    onDismiss: () -> Unit,
) {
    var selectedThreshold by remember { mutableStateOf("100%") }
    val thresholds = listOf("80%", "90%", "100%")

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
            // Header with Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "BATTERY HEALTH & OPTIONS",
                    color = VargWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
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

            Spacer(Modifier.height(18.dp))

            // Battery Health Overview Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VargSurfaceCard, RoundedCornerShape(14.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = "Current Charge",
                            color = VargTextSecondary,
                            fontSize = 12.sp,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "${battery.stateOfChargePct}%",
                            color = VargElectricGreen,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Estimated Range",
                            color = VargTextSecondary,
                            fontSize = 12.sp,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "${battery.estimatedRangeKm} km",
                            color = VargWhite,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { (battery.stateOfChargePct / 100f).coerceIn(0f, 1f) },
                    color = VargElectricGreen,
                    trackColor = VargBorderSubtle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                )
            }

            Spacer(Modifier.height(12.dp))

            // Cell Metrics Grid (Temperature, Charging Status, Condition)
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
                    Text("Cell Temp", color = VargTextSecondary, fontSize = 11.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("${battery.temperatureC}°C", color = VargWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(2.dp))
                    Text("Status: Optimal", color = VargElectricGreen, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(VargSurfaceCard, RoundedCornerShape(12.dp))
                        .border(1.dp, VargBorderSubtle, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                ) {
                    Text("Power State", color = VargTextSecondary, fontSize = 11.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        battery.chargingState.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = VargWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text("Health: 99%", color = VargElectricGreen, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Charging Limit / Threshold Selector (from Photo 4)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VargSurfaceCard, RoundedCornerShape(12.dp))
                    .border(1.dp, VargBorderSubtle, RoundedCornerShape(12.dp))
                    .padding(14.dp),
            ) {
                Text(
                    text = "Charging Threshold Target",
                    color = VargTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    thresholds.forEach { thresh ->
                        val isSelected = thresh == selectedThreshold
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .background(
                                    color = if (isSelected) VargElectricGreen else VargPillDark,
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) VargElectricGreen else VargBorderSubtle,
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .clickable { selectedThreshold = thresh }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = thresh,
                                color = if (isSelected) com.stark.varg.ui.theme.VargPitchBlack else VargWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
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
                    text = "DONE",
                    color = VargTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
            }
        }
    }
}
