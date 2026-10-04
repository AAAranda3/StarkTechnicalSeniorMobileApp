package com.stark.varg.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.presentation.components.VargIconGarage
import com.stark.varg.presentation.components.VargIconPower
import com.stark.varg.presentation.components.VargIconStats
import com.stark.varg.presentation.components.VargScreenTopAppBar
import com.stark.varg.presentation.util.VargStrings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

@Composable
fun SettingsScreen(
    language: AppLanguage,
    unitSystem: UnitSystem,
    isLiveStreamActive: Boolean,
    onLanguageChange: (AppLanguage) -> Unit,
    onUnitSystemChange: (UnitSystem) -> Unit,
    onToggleLiveStream: () -> Unit,
    onRestoreFactoryModes: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack),
    ) {
        VargScreenTopAppBar(
            title = VargStrings.settingsTitle(language),
            subtitle = VargStrings.settingsSubtitle(language),
            onBack = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // 1. Language Section
            SettingsCard(
                title = VargStrings.languageSectionTitle(language),
                description = VargStrings.languageSectionDesc(language),
                iconContent = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = VargSecondary,
                        modifier = Modifier.size(16.dp),
                    )
                },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AppLanguage.entries.forEach { lang ->
                        val isSelected = lang == language
                        LanguageSelectionPill(
                            language = lang,
                            isSelected = isSelected,
                            onSelect = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onLanguageChange(lang)
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            // 2. Unit System Section
            SettingsCard(
                title = VargStrings.unitSectionTitle(language),
                description = VargStrings.unitSectionDesc(language),
                iconContent = {
                    VargIconStats(color = VargSecondary, sizeDp = 16.dp)
                },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    UnitSystemSelectionPill(
                        label = VargStrings.metricLabel(language),
                        isSelected = unitSystem == UnitSystem.METRIC,
                        onSelect = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onUnitSystemChange(UnitSystem.METRIC)
                        },
                        modifier = Modifier.weight(1f),
                    )

                    UnitSystemSelectionPill(
                        label = VargStrings.imperialLabel(language),
                        isSelected = unitSystem == UnitSystem.IMPERIAL,
                        onSelect = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onUnitSystemChange(UnitSystem.IMPERIAL)
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            // 3. Live CAN Telemetry Stream Section
            SettingsCard(
                title = VargStrings.liveStreamSectionTitle(language),
                description = VargStrings.liveStreamSectionDesc(language),
                iconContent = {
                    VargIconPower(color = VargSecondary, sizeDp = 16.dp)
                },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isLiveStreamActive) Color(0xFF0F1A12) else Color(0xFF16171B))
                        .border(
                            width = 1.dp,
                            color = if (isLiveStreamActive) VargElectricGreen.copy(alpha = 0.5f) else Color(0xFF26282E),
                            shape = RoundedCornerShape(8.dp),
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onToggleLiveStream()
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (isLiveStreamActive) VargElectricGreen else VargTextSecondary,
                                    shape = CircleShape,
                                ),
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = if (isLiveStreamActive) {
                                VargStrings.liveStreamActiveBadge(language)
                            } else {
                                VargStrings.liveStreamInactiveBadge(language)
                            },
                            color = if (isLiveStreamActive) VargElectricGreen else VargWhite,
                            style = StarkTypographyTokens.TechnicalLabel.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp,
                            ),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isLiveStreamActive) VargElectricGreen.copy(alpha = 0.2f) else Color(0xFF22242B)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                    ) {
                        Text(
                            text = if (isLiveStreamActive) "STOP" else "START",
                            color = if (isLiveStreamActive) VargElectricGreen else VargSecondary,
                            style = StarkTypographyTokens.TechnicalLabel.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                            ),
                        )
                    }
                }
            }

            // 4. Vehicle & Firmware Telemetry Info
            SettingsCard(
                title = VargStrings.vehicleSectionTitle(language),
                description = "Hardware specifications & firmware version",
                iconContent = {
                    VargIconGarage(color = VargSecondary, sizeDp = 18.dp)
                },
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    VehicleSpecRow(label = VargStrings.modelLabel(language), value = "Stark VARG Alpha EX-80")
                    VehicleSpecRow(label = VargStrings.vinLabel(language), value = "SV-2024-EX80-9921")
                    VehicleSpecRow(label = VargStrings.motorLabel(language), value = "80 HP • 6.0 kWh Inverter")
                    VehicleSpecRow(label = VargStrings.firmwareLabel(language), value = "v2.4.1-rc3 (CAN 2.0B)")
                    VehicleSpecRow(label = VargStrings.appVersionLabel(language), value = "v1.2.0 (Official Telemetry)")
                }
            }

            // 5. Restore Factory Power Maps
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF141215))
                    .border(1.dp, Color(0xFF2A2226), RoundedCornerShape(10.dp))
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onRestoreFactoryModes()
                    }
                    .padding(vertical = 16.dp, horizontal = 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = VargStarkRed,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = VargStrings.restoreFactoryButton(language),
                        color = VargStarkRed,
                        style = StarkTypographyTokens.TechnicalLabel.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                        ),
                    )
                }
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    description: String,
    iconContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(VargSurfaceCard)
            .border(1.dp, Color(0xFF202228), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF1B1D23), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF2C2F38), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                iconContent()
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = VargWhite,
                    style = StarkTypographyTokens.TechnicalLabel.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    ),
                )
                Text(
                    text = description,
                    color = VargTextSecondary,
                    style = StarkTypographyTokens.TechnicalLabel.copy(
                        fontSize = 9.sp,
                        letterSpacing = 0.5.sp,
                    ),
                )
            }
        }

        content()
    }
}

@Composable
private fun LanguageSelectionPill(
    language: AppLanguage,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) VargWhite else Color(0xFF181A20))
            .border(
                width = 1.dp,
                color = if (isSelected) VargSecondary else Color(0xFF262832),
                shape = RoundedCornerShape(8.dp),
            )
            .semantics {
                role = Role.RadioButton
                selected = isSelected
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect,
            )
            .padding(vertical = 12.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = language.flagEmoji, fontSize = 14.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                text = language.displayName,
                color = if (isSelected) VargPitchBlack else VargWhite,
                style = StarkTypographyTokens.TechnicalLabel.copy(
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                ),
            )
        }
    }
}

@Composable
private fun UnitSystemSelectionPill(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) VargWhite else Color(0xFF181A20))
            .border(
                width = 1.dp,
                color = if (isSelected) VargSecondary else Color(0xFF262832),
                shape = RoundedCornerShape(8.dp),
            )
            .semantics {
                role = Role.RadioButton
                selected = isSelected
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect,
            )
            .padding(vertical = 12.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (isSelected) VargPitchBlack else VargWhite,
            style = StarkTypographyTokens.TechnicalLabel.copy(
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                letterSpacing = 0.5.sp,
            ),
        )
    }
}

@Composable
private fun VehicleSpecRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF141519))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = VargTextSecondary,
            style = StarkTypographyTokens.TechnicalLabel.copy(
                fontSize = 10.sp,
                letterSpacing = 0.5.sp,
            ),
        )
        Text(
            text = value,
            color = VargWhite,
            style = StarkTypographyTokens.TechnicalLabel.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp,
            ),
        )
    }
}
