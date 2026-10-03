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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
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
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.domain.util.UnitConverter
import com.stark.varg.presentation.util.VargStrings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

@Composable
fun BikeShowcase(
    bike: BikeInfo,
    battery: BatteryStatus? = null,
    diagnostics: DiagnosticsData? = null,
    unitSystem: UnitSystem = UnitSystem.METRIC,
    language: AppLanguage = AppLanguage.SPANISH,
    isLiveStreamActive: Boolean = false,
    onToggleUnitSystem: () -> Unit = {},
    onToggleLiveStream: () -> Unit = {},
    onOpenBatteryOptions: () -> Unit = {},
    onOpenDiagnostics: () -> Unit = {},
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val hasAlert = diagnostics?.warnings?.isNotEmpty() == true || diagnostics?.faultCodes?.isNotEmpty() == true
    val isCharging = battery?.chargingState == ChargingState.CHARGING
    val haptic = LocalHapticFeedback.current

    val liveBorderColor by animateColorAsState(
        targetValue = if (isLiveStreamActive) VargElectricGreen else Color(0xFF363642),
        label = "liveBorder"
    )
    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(VargPitchBlack)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left: Telemetry Sync + Live 4Hz Stream Toggle (Glove-Friendly)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF222228))
                        .border(1.dp, Color(0xFF363642), RoundedCornerShape(10.dp))
                        .semantics {
                            contentDescription = "Sync telemetry with vehicle CAN-bus"
                            role = Role.Button
                        }
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onRefresh()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    VargIconSync(color = VargSecondary, sizeDp = 24.dp)
                }

                Box(
                    modifier = Modifier
                        .height(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isLiveStreamActive) Color(0x2200E676) else Color(0xFF222228))
                        .border(1.dp, liveBorderColor, RoundedCornerShape(10.dp))
                        .semantics {
                            contentDescription = if (isLiveStreamActive) {
                                "Live 4 Hertz CAN-bus telemetry stream active. Tap to pause."
                            } else {
                                "Live CAN-bus telemetry stream paused. Tap to activate 4 Hertz stream."
                            }
                            role = Role.Button
                        }
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onToggleLiveStream()
                        }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .graphicsLayer { alpha = if (isLiveStreamActive) pulseAlpha else 0.4f }
                                .background(if (isLiveStreamActive) VargElectricGreen else VargSecondary, CircleShape)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = if (isLiveStreamActive) VargStrings.streamLiveBadge(language) else VargStrings.streamOffBadge(language),
                            color = if (isLiveStreamActive) VargElectricGreen else VargSecondary,
                            style = StarkTypographyTokens.ScaleTick.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                        )
                    }
                }
            }

            // Center: Vehicle Model & Status Subtitle
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

            // Right: Unit System Pill & Notification Bell (Glove-Friendly)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .height(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF222228))
                        .border(1.dp, Color(0xFF363642), RoundedCornerShape(10.dp))
                        .semantics {
                            contentDescription = "Toggle unit system. Current: ${if (unitSystem == UnitSystem.METRIC) "Metric" else "Imperial"}"
                            role = Role.Button
                        }
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onToggleUnitSystem()
                        }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (unitSystem == UnitSystem.METRIC) "KM / °C" else "MI / °F",
                        color = VargWhite,
                        style = StarkTypographyTokens.ScaleTick.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF222228))
                        .border(1.dp, Color(0xFF363642), RoundedCornerShape(10.dp))
                        .semantics {
                            contentDescription = if (hasAlert) "Diagnostics alerts active. Tap to view." else "Diagnostics nominal. Tap to view."
                            role = Role.Button
                        }
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onOpenDiagnostics()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    VargIconBell(color = VargSecondary, sizeDp = 24.dp)
                    if (hasAlert) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 5.dp, end = 5.dp)
                                .size(9.dp)
                                .background(VargStarkRed, CircleShape),
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        if (battery != null) {
            val (rangeVal, rangeUnit) = UnitConverter.formatRange(battery.estimatedRangeKm, unitSystem)
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onOpenBatteryOptions()
                    }
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    // Range Value (Stark Sans ExtraLight)
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

                    // Vertical Divider
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

                    // SoC Percentage Value (Stark Sans ExtraLight)
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

                Spacer(Modifier.height(4.dp))
                Text(
                    text = VargStrings.estimatedRangeLabel(language, if (unitSystem == UnitSystem.METRIC) "km" else "mi"),
                    color = VargTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 1.sp,
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Hero Vehicle Stage with Pure Black Background & Showroom Floor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(185.dp)
                .background(VargPitchBlack),
            contentAlignment = Alignment.Center,
        ) {
            // Ambient Showroom Glow behind the motorcycle chassis (switches to electric green when charging)
            Box(
                modifier = Modifier
                    .size(240.dp, 120.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                if (isCharging) VargElectricGreen.copy(alpha = 0.22f) else VargStarkRed.copy(alpha = 0.18f),
                                Color.Transparent,
                            ),
                        ),
                    ),
            )

            // Motorcycle Ground Contact Shadow (ellipse under wheels)
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

            // Showroom Studio Floor Highlight Line
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 9.dp)
                    .size(width = 220.dp, height = 1.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                VargSecondary.copy(alpha = 0.35f),
                                Color.Transparent,
                            ),
                        ),
                    ),
            )

            // Async Image of the Stark VARG - Seamlessly blends on pure black
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bike.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = bike.model,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(160.dp),
                contentScale = ContentScale.Fit,
            )
        }
    }
}
