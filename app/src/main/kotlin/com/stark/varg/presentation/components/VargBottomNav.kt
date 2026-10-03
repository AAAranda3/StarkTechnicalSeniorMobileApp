package com.stark.varg.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

/**
 * Bottom navigation bar.
 * Features an active pill capsule background highlight and clean monochrome vector icons.
 */
@Composable
fun VargBottomNav(
    modifier: Modifier = Modifier,
    activeTab: String = "home",
    onTabSelected: (String) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(VargPitchBlack)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Vehicle Tab (Home)
        VargBottomNavItem(
            label = "Vehicle",
            isActive = activeTab == "home",
            onClick = { onTabSelected("home") },
            icon = { color ->
                Canvas(modifier = Modifier.size(18.dp)) {
                    val path = Path().apply {
                        moveTo(size.width / 2f, 2f)
                        lineTo(size.width - 2f, size.height * 0.45f)
                        lineTo(size.width - 4f, size.height * 0.45f)
                        lineTo(size.width - 4f, size.height - 2f)
                        lineTo(size.width * 0.62f, size.height - 2f)
                        lineTo(size.width * 0.62f, size.height * 0.6f)
                        lineTo(size.width * 0.38f, size.height * 0.6f)
                        lineTo(size.width * 0.38f, size.height - 2f)
                        lineTo(4f, size.height - 2f)
                        lineTo(4f, size.height * 0.45f)
                        lineTo(2f, size.height * 0.45f)
                        close()
                    }
                    drawPath(path = path, color = color, style = Stroke(width = 1.8f))
                }
            },
        )

        // Telemetry Tab
        VargBottomNavItem(
            label = "Telemetry",
            isActive = activeTab == "stats",
            onClick = { onTabSelected("stats") },
            icon = { color ->
                VargIconStats(color = color, sizeDp = 18.dp)
            },
        )

        // Modes Tab
        VargBottomNavItem(
            label = "Modes",
            isActive = activeTab == "modes",
            onClick = { onTabSelected("modes") },
            icon = { color ->
                VargIconModes(color = color, sizeDp = 18.dp)
            },
        )

        // Profile Tab
        VargBottomNavItem(
            label = "Profile",
            isActive = activeTab == "profile",
            onClick = { onTabSelected("profile") },
            icon = { color ->
                Canvas(modifier = Modifier.size(18.dp)) {
                    drawCircle(
                        color = color,
                        radius = size.minDimension / 4.8f,
                        center = Offset(center.x, size.height * 0.30f),
                        style = Stroke(width = 1.8f),
                    )
                    val shoulderPath = Path().apply {
                        moveTo(2f, size.height - 2f)
                        cubicTo(
                            size.width * 0.2f, size.height * 0.65f,
                            size.width * 0.8f, size.height * 0.65f,
                            size.width - 2f, size.height - 2f,
                        )
                    }
                    drawPath(path = shoulderPath, color = color, style = Stroke(width = 1.8f))
                }
            },
        )
    }
}

@Composable
private fun VargBottomNavItem(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    icon: @Composable (Color) -> Unit,
) {
    val contentColor = if (isActive) VargWhite else VargTextSecondary

    if (isActive) {
        // Active tab: Horizontal rounded capsule pill
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF22222A))
                .clickable { onClick() }
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            icon(contentColor)
            Text(
                text = label,
                color = contentColor,
                style = StarkTypographyTokens.PillLabel,
            )
        }
    } else {
        // Inactive tab: Vertical stack with subtle text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { onClick() }
                .padding(horizontal = 10.dp, vertical = 4.dp),
        ) {
            icon(contentColor)
            Spacer(Modifier.height(3.dp))
            Text(
                text = label,
                color = contentColor,
                style = StarkTypographyTokens.ScaleTick,
            )
        }
    }
}
