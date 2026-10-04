package com.stark.varg.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.BikeInfo
import com.stark.varg.domain.model.ChargingState
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.domain.model.SessionData
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.domain.model.WarningSeverity
import com.stark.varg.domain.util.UnitConverter
import com.stark.varg.presentation.util.VargStrings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber
import com.stark.varg.ui.theme.VargWhite

/**
 * Handlebar Cockpit Grid in Landscape mode.
 * Perfectly balances the motorcycle hero stage, centered range/battery readings,
 * and full-sized enterprise service cards matching the portrait design system.
 */
@Composable
fun LandscapeCockpitGrid(
    bike: BikeInfo,
    battery: BatteryStatus,
    settings: RideSettings,
    motor: MotorStatus,
    session: SessionData,
    diagnostics: DiagnosticsData,
    selectedMode: String,
    activeMaxPowerHp: Int = settings.maxPowerHp,
    unitSystem: UnitSystem = UnitSystem.METRIC,
    language: AppLanguage = AppLanguage.SPANISH,
    isLiveStreamActive: Boolean = false,
    modifier: Modifier = Modifier,
    onToggleUnitSystem: () -> Unit = {},
    onToggleLiveStream: () -> Unit = {},
    onOpenModes: () -> Unit = {},
    onOpenMotor: () -> Unit = {},
    onOpenStats: () -> Unit = {},
    onOpenDiagnostics: () -> Unit = {},
    onOpenBatteryOptions: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val haptic = LocalHapticFeedback.current
    val hasAlert = diagnostics.warnings.isNotEmpty() || diagnostics.faultCodes.isNotEmpty()
    val hasCritical = diagnostics.warnings.any { it.severity == WarningSeverity.CRITICAL } || diagnostics.faultCodes.isNotEmpty()
    val alertAccent = if (hasCritical) VargStarkRed else VargWarningAmber
    val primaryWarning = diagnostics.warnings.firstOrNull()?.message ?: "Check diagnostics"
    val isCharging = battery.chargingState == ChargingState.CHARGING

    val (tripDistVal, tripDistUnit) = UnitConverter.formatDistance(session.distanceKm, unitSystem)
    val (maxSpeedVal, maxSpeedUnit) = UnitConverter.formatSpeed(session.maxSpeedKmh, unitSystem)
    val (motorTempVal, motorTempUnit) = UnitConverter.formatTemperature(motor.temperatureC, unitSystem)
    val (rangeVal, rangeUnit) = UnitConverter.formatRange(battery.estimatedRangeKm, unitSystem)
    val hpUnit = if (language == AppLanguage.SPANISH) "CV" else "HP"

    val liveBorderColor by animateColorAsState(
        targetValue = if (isLiveStreamActive) VargElectricGreen else Color(0xFF363642),
        label = "liveBorderCockpit",
    )
    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransitionCockpit")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseAlphaCockpit",
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .padding(horizontal = 20.dp, vertical = 6.dp),
    ) {
        // 1. Top Bar: Pure Stark VARG Brand, CAN Toggle, Units, Bell, Settings
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left: Telemetry Sync + Live CAN Stream Toggle (Clean Integrated Header Icons)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // Sync Icon Action (Integrated, transparent, 38dp touch target)
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onRefresh()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    VargIconSync(color = VargWhite.copy(alpha = 0.85f), sizeDp = 20.dp)
                }

                // Live CAN Broadcast Stream Action (Sleek integrated indicator)
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isLiveStreamActive) Color(0x2200E676) else Color.Transparent)
                        .border(
                            width = 1.dp,
                            color = if (isLiveStreamActive) VargElectricGreen.copy(alpha = 0.5f) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onToggleLiveStream()
                        }
                        .padding(horizontal = if (isLiveStreamActive) 8.dp else 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        VargIconBroadcast(
                            color = if (isLiveStreamActive) VargElectricGreen else VargSecondary,
                            sizeDp = 18.dp,
                        )
                        if (isLiveStreamActive) {
                            Spacer(Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .graphicsLayer { alpha = pulseAlpha }
                                    .background(VargElectricGreen, CircleShape),
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "LIVE",
                                color = VargElectricGreen,
                                style = StarkTypographyTokens.ScaleTick.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                            )
                        }
                    }
                }
            }

            // Center: Vehicle Model & Status Subtitle (Exact same layout as portrait)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "STARK VARG",
                    color = VargWhite,
                    style = StarkTypographyTokens.BrandTitle.copy(fontSize = 16.sp, letterSpacing = 2.sp),
                )
                Spacer(Modifier.height(2.dp))
                if (isCharging) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(VargElectricGreen, CircleShape),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = VargStrings.chargingHeroSubtitle(language),
                            color = VargElectricGreen,
                            style = StarkTypographyTokens.CardSubtitle.copy(
                                color = VargElectricGreen,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                            ),
                        )
                    }
                } else {
                    Text(
                        text = VargStrings.readyToRide(language),
                        color = VargTextSecondary,
                        style = StarkTypographyTokens.CardSubtitle.copy(fontSize = 12.sp),
                    )
                }
            }

            // Right: Integrated Unit Pill + Bell + Settings Gear
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                // Sleek Compact Unit Switcher Pill
                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x16FFFFFF))
                        .border(1.dp, Color(0x24FFFFFF), RoundedCornerShape(14.dp))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onToggleUnitSystem()
                        }
                        .padding(horizontal = 9.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (unitSystem == UnitSystem.METRIC) "KM" else "MI",
                        color = VargWhite,
                        style = StarkTypographyTokens.ScaleTick.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                    )
                }

                // Diagnostics Bell (Clean Integrated Icon)
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onOpenDiagnostics()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    VargIconBell(color = VargWhite.copy(alpha = 0.85f), sizeDp = 20.dp)
                    if (hasAlert) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 5.dp, end = 5.dp)
                                .size(7.dp)
                                .background(VargStarkRed, CircleShape),
                        )
                    }
                }

                // Settings Gear (Clean Integrated Icon)
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onOpenSettings()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = VargWhite.copy(alpha = 0.85f),
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        // 2. Cockpit Body: Open Showroom Stage on Left, Large Services on Right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // LEFT COLUMN: Centered Battery & Range Gauge + Motorcycle Showroom Stage + Alert Banner
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // 1. Centered Range & Battery SoC Gauge (Identical to portrait)
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onOpenBatteryOptions()
                        }
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = rangeVal,
                            color = VargWhite,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraLight,
                            letterSpacing = (-0.5).sp,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = rangeUnit.uppercase(),
                            color = VargWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 3.dp),
                        )

                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = "|",
                            color = Color(0xFF383838),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraLight,
                        )
                        Spacer(Modifier.width(16.dp))

                        VargIconBatteryVertical(
                            percentage = battery.stateOfChargePct,
                            color = VargSecondary,
                            sizeDp = 18.dp,
                        )
                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = "${battery.stateOfChargePct}",
                            color = VargWhite,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraLight,
                            letterSpacing = (-0.5).sp,
                        )
                        Spacer(Modifier.width(3.dp))
                        Text(
                            text = "%",
                            color = VargWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 3.dp),
                        )
                    }

                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = VargStrings.estimatedRangeLabel(language, if (unitSystem == UnitSystem.METRIC) "km" else "mi"),
                        color = VargTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 1.sp,
                    )
                }

                // 2. Centered Motorcycle Showroom Stage (No box enclosure, pure pitch black canvas)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    // Red/Green Ambient Glow
                    Box(
                        modifier = Modifier
                            .size(260.dp, 120.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        if (isCharging) VargElectricGreen.copy(alpha = 0.25f) else VargStarkRed.copy(alpha = 0.2f),
                                        Color.Transparent,
                                    ),
                                ),
                            ),
                    )

                    // Ground shadow
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 6.dp)
                            .size(width = 240.dp, height = 16.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.95f),
                                        Color.Transparent,
                                    ),
                                ),
                            ),
                    )

                    // Showroom floor highlight line
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                            .size(width = 210.dp, height = 1.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        VargSecondary.copy(alpha = 0.4f),
                                        Color.Transparent,
                                    ),
                                ),
                            ),
                    )

                    // Motorcycle Cutout Image
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(bike.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = bike.model,
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .fillMaxHeight(),
                        contentScale = ContentScale.Fit,
                    )
                }

                // 3. Active Warning Banner (Identical to portrait style)
                if (hasAlert) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF2C2825),
                                        Color(0xFF262320),
                                    ),
                                ),
                            )
                            .border(1.dp, alertAccent.copy(alpha = 0.55f), RoundedCornerShape(10.dp))
                            .clickable { onOpenDiagnostics() }
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                VargIconDiagnostics(color = alertAccent, sizeDp = 16.dp)
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = primaryWarning,
                                    color = VargWhite,
                                    style = StarkTypographyTokens.CardSubtitle.copy(color = VargWhite),
                                    maxLines = 1,
                                )
                            }
                            Text(
                                text = VargStrings.viewAction(language),
                                color = alertAccent,
                                style = StarkTypographyTokens.TechnicalLabel.copy(color = alertAccent),
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "✓ ${VargStrings.allSystemsOperational(language)}",
                            color = VargElectricGreen,
                            style = StarkTypographyTokens.TechnicalLabel.copy(
                                color = VargElectricGreen,
                                fontSize = 11.sp,
                            ),
                        )
                    }
                }
            }

            // RIGHT COLUMN: Large, Spacious Enterprise Service Cards (Matching portrait DashboardServiceCard)
            Column(
                modifier = Modifier
                    .weight(1.05f)
                    .fillMaxHeight()
                    .padding(start = 4.dp),
            ) {
                // Section Header with < > (Pinned at top)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp, end = 2.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = VargStrings.servicesHeader(language),
                        style = StarkTypographyTokens.TechnicalHeader,
                        letterSpacing = 1.5.sp,
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("<", style = StarkTypographyTokens.TechnicalLabel.copy(color = VargSecondary.copy(alpha = 0.6f)))
                        Text(">", style = StarkTypographyTokens.TechnicalLabel.copy(color = VargSecondary.copy(alpha = 0.6f)))
                    }
                }

                // Scrollable Cards List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // 1. Modos de Potencia
                    LandscapeServiceCard(
                        iconContent = { VargIconModes(color = VargSecondary, sizeDp = 18.dp) },
                        title = VargStrings.servicePowerModes(language),
                        subtitle = "$selectedMode Profile • $activeMaxPowerHp $hpUnit",
                        onClick = onOpenModes,
                    )

                    // 2. Motor y Transmisión
                    LandscapeServiceCard(
                        iconContent = { VargIconMotor(color = VargSecondary, sizeDp = 18.dp) },
                        title = VargStrings.serviceMotor(language),
                        subtitle = "${motor.powerHp} $hpUnit • $motorTempVal $motorTempUnit",
                        onClick = onOpenMotor,
                    )

                    // 3. Telemetría de Viaje
                    LandscapeServiceCard(
                        iconContent = { VargIconStats(color = VargSecondary, sizeDp = 18.dp) },
                        title = VargStrings.serviceTelemetry(language),
                        subtitle = "$tripDistVal $tripDistUnit • Peak $maxSpeedVal $maxSpeedUnit",
                        onClick = onOpenStats,
                    )

                    // 4. Diagnóstico del Sistema
                    LandscapeServiceCard(
                        iconContent = {
                            VargIconDiagnostics(
                                color = if (hasAlert) alertAccent else VargSecondary,
                                sizeDp = 18.dp,
                            )
                        },
                        title = VargStrings.serviceDiagnostics(language),
                        subtitle = if (hasAlert) {
                            VargStrings.activeAdvisory(language, diagnostics.warnings.size.coerceAtLeast(1))
                        } else {
                            VargStrings.allSystemsOperational(language)
                        },
                        onClick = onOpenDiagnostics,
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Full-size spacious service card for Landscape mode,
 * matching portrait DashboardServiceCard's height, typography, icon box, and aesthetics.
 */
@Composable
private fun LandscapeServiceCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF161619),
                        Color(0xFF111113),
                    ),
                ),
            )
            .border(1.dp, Color(0xFF26262C), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 11.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1C1C22))
                        .border(1.dp, VargSecondary.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    iconContent()
                }

                Spacer(Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        style = StarkTypographyTokens.CardTitle,
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = subtitle,
                        style = StarkTypographyTokens.CardSubtitle,
                    )
                }
            }

            VargIconChevronRight(color = VargSecondary.copy(alpha = 0.75f), sizeDp = 14.dp)
        }
    }
}
