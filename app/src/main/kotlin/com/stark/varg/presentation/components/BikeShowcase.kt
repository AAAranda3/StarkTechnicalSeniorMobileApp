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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.stark.varg.domain.model.BatteryStatus
import com.stark.varg.domain.model.BikeInfo
import com.stark.varg.domain.model.DiagnosticsData
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
    onOpenBatteryOptions: () -> Unit = {},
    onOpenDiagnostics: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val hasAlert = diagnostics?.warnings?.isNotEmpty() == true || diagnostics?.faultCodes?.isNotEmpty() == true

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
            // Left spacer balancing the bell icon on the right so title stays centered
            Spacer(modifier = Modifier.size(36.dp))

            // Center: Vehicle Model & Status Subtitle
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "STARK VARG",
                    color = VargWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "Ready to ride!",
                    color = VargTextSecondary,
                    fontSize = 12.sp,
                    letterSpacing = 0.3.sp,
                )
            }

            // Right: Notification Bell with alert indicator (borderless, minimalist)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onOpenDiagnostics() },
                contentAlignment = Alignment.CenterEnd,
            ) {
                VargIconBell(color = VargSecondary, sizeDp = 22.dp)
                if (hasAlert) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 2.dp, end = 2.dp)
                            .size(7.dp)
                            .background(VargStarkRed, CircleShape),
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        if (battery != null) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onOpenBatteryOptions() }
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    // Range Value
                    Text(
                        text = "${battery.estimatedRangeKm}",
                        color = VargWhite,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = "KM",
                        color = VargWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )

                    // Vertical Divider
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "|",
                        color = Color(0xFF383838),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light,
                    )
                    Spacer(Modifier.width(16.dp))

                    VargIconBatteryVertical(
                        percentage = battery.stateOfChargePct,
                        color = VargSecondary,
                        sizeDp = 18.dp,
                    )
                    Spacer(Modifier.width(8.dp))

                    // SoC Percentage Value
                    Text(
                        text = "${battery.stateOfChargePct}",
                        color = VargWhite,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "%",
                        color = VargWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                }

                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Estimated range",
                    color = VargTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // 3. Hero Vehicle Stage with Seamless Studio Lighting & Floor Reflection
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(185.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF000000),
                            Color(0xFF0B0B0D),
                            VargPitchBlack,
                        ),
                        radius = 580f,
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            // Ambient Showroom Glow behind the motorcycle chassis
            Box(
                modifier = Modifier
                    .size(240.dp, 120.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                VargStarkRed.copy(alpha = 0.18f),
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

            // Async Image of the Stark VARG with soft edge feathering to dissolve black box
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bike.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = bike.model,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(160.dp)
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .drawWithContent {
                        drawContent()
                        // Fade horizontal boundaries into the showroom stage
                        drawRect(
                            brush = Brush.horizontalGradient(
                                0f to Color.Transparent,
                                0.12f to Color.Black,
                                0.88f to Color.Black,
                                1f to Color.Transparent,
                            ),
                            blendMode = BlendMode.DstIn,
                        )
                        // Fade vertical boundaries into the showroom stage
                        drawRect(
                            brush = Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.14f to Color.Black,
                                0.86f to Color.Black,
                                1f to Color.Transparent,
                            ),
                            blendMode = BlendMode.DstIn,
                        )
                    },
                contentScale = ContentScale.Fit,
            )
        }
    }
}
