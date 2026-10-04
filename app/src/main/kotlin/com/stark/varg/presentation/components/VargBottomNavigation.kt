package com.stark.varg.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.presentation.TelemetryDestination
import com.stark.varg.presentation.util.VargStrings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargWhite

enum class BottomNavTab(
    val destination: TelemetryDestination,
) {
    COCKPIT(TelemetryDestination.Dashboard),
    MODES(TelemetryDestination.Modes),
    BATTERY(TelemetryDestination.Battery),
    SETTINGS(TelemetryDestination.Settings),
}

@Composable
fun VargBottomNavigation(
    currentDestination: TelemetryDestination,
    language: AppLanguage,
    onTabSelected: (TelemetryDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0C0D10)),
    ) {
        // Subtle top border divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF1E2128)),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomNavTab.entries.forEach { tab ->
                val isSelected = when (tab) {
                    BottomNavTab.COCKPIT -> currentDestination == TelemetryDestination.Dashboard
                    BottomNavTab.MODES -> currentDestination == TelemetryDestination.Modes
                    BottomNavTab.BATTERY -> currentDestination == TelemetryDestination.Battery
                    BottomNavTab.SETTINGS -> currentDestination == TelemetryDestination.Settings
                }

                val label = when (tab) {
                    BottomNavTab.COCKPIT -> VargStrings.navCockpit(language)
                    BottomNavTab.MODES -> VargStrings.navModes(language)
                    BottomNavTab.BATTERY -> VargStrings.navBattery(language)
                    BottomNavTab.SETTINGS -> VargStrings.navSettings(language)
                }

                VargBottomNavItem(
                    label = label,
                    tab = tab,
                    isSelected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onTabSelected(tab.destination)
                        }
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun VargBottomNavItem(
    label: String,
    tab: BottomNavTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) VargWhite else Color(0xFF6B7280),
        animationSpec = tween(durationMillis = 200),
        label = "BottomNavIconColor",
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) VargWhite else Color(0xFF6B7280),
        animationSpec = tween(durationMillis = 200),
        label = "BottomNavTextColor",
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .semantics {
                role = Role.Tab
                selected = isSelected
            }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Micro indicator bar above active tab
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(2.dp)
                .background(
                    color = if (isSelected) VargStarkRed else Color.Transparent,
                    shape = RoundedCornerShape(1.dp),
                ),
        )

        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier.size(22.dp),
            contentAlignment = Alignment.Center,
        ) {
            when (tab) {
                BottomNavTab.COCKPIT -> VargIconGarage(color = iconColor, sizeDp = 22.dp)
                BottomNavTab.MODES -> VargIconModes(color = iconColor, sizeDp = 20.dp)
                BottomNavTab.BATTERY -> VargIconBatteryVertical(percentage = 100, color = iconColor, sizeDp = 18.dp)
                BottomNavTab.SETTINGS -> Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        Spacer(Modifier.height(3.dp))

        Text(
            text = label.uppercase(),
            color = textColor,
            style = StarkTypographyTokens.TechnicalLabel.copy(
                fontSize = 9.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                letterSpacing = 1.sp,
            ),
        )
    }
}
