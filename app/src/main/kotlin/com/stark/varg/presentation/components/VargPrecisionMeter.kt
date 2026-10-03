package com.stark.varg.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

@Composable
fun VargPrecisionMeter(
    title: String,
    unit: String,
    value: Float,
    maxValue: Float,
    displayValue: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    tickSteps: List<Int> = listOf(0, 20, 40, 60, 80),
    sideLabel: String? = null,
) {
    val fraction = if (maxValue > 0f) (value / maxValue).coerceIn(0f, 1f) else 0f

    Column(modifier = modifier.fillMaxWidth()) {
        // Header: Title & Unit + Optional Map Tag + Numeric Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    color = VargTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    text = unit,
                    color = VargTextSecondary,
                    fontSize = 12.sp,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (sideLabel != null) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF22242E), RoundedCornerShape(4.dp))
                            .border(1.dp, Color(0xFF343844), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                    ) {
                        Text(
                            text = sideLabel,
                            color = VargTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp,
                        )
                    }
                }

                // Numeric Badge in serious slightly rounded rectangular shape
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1E2028), RoundedCornerShape(4.dp))
                        .border(1.dp, Color(0xFF383C4C), RoundedCornerShape(4.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "$displayValue HP",
                        color = VargWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Gauge Track + Thumb Knob with 1:1 Pixel Exact Positioning
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            val thumbRadius = 8.dp
            val availableWidth = maxWidth
            val trackX = availableWidth * fraction

            // Background Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(VargBorderSubtle, RoundedCornerShape(2.dp)),
            )

            // Active Track
            Box(
                modifier = Modifier
                    .width(trackX)
                    .height(4.dp)
                    .background(accentColor, RoundedCornerShape(2.dp)),
            )

            // Thumb Knob centered precisely at trackX
            val thumbLeft = (trackX - thumbRadius).coerceIn(0.dp, availableWidth - (thumbRadius * 2))
            Box(
                modifier = Modifier
                    .offset(x = thumbLeft)
                    .size(thumbRadius * 2)
                    .background(VargWhite, CircleShape)
                    .border(2.dp, accentColor, CircleShape),
            )
        }

        // Scale Tick Marks (Only numeric ticks spaced evenly: 0, 20, 40, 60, 80)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tickSteps.forEach { tick ->
                Text(
                    text = tick.toString(),
                    color = VargTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

