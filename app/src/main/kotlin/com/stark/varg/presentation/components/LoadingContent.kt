package com.stark.varg.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

/**
 * Premium dark skeleton placeholder displayed while loading CAN-bus vehicle telemetry.
 * Completely mirrors the Stark VARG cockpit structure with smooth obsidian shimmer effects.
 */
@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .testTag("loading_indicator"),
    ) {
        if (isLandscape) {
            LandscapeLoadingSkeleton()
        } else {
            PortraitLoadingSkeleton()
        }
    }
}

@Composable
private fun PortraitLoadingSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Top Bar Skeleton
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .vargShimmer(CircleShape),
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(width = 110.dp, height = 16.dp)
                        .vargShimmer(RoundedCornerShape(4.dp)),
                )
                Spacer(Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 11.dp)
                        .vargShimmer(RoundedCornerShape(3.dp)),
                )
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .vargShimmer(CircleShape),
            )
        }

        Spacer(Modifier.height(14.dp))

        // Hero Autonomy & Battery Skeleton Numbers
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 74.dp, height = 38.dp)
                    .vargShimmer(RoundedCornerShape(6.dp)),
            )
            Spacer(Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(width = 2.dp, height = 28.dp)
                    .vargShimmer(RoundedCornerShape(1.dp)),
            )
            Spacer(Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(width = 74.dp, height = 38.dp)
                    .vargShimmer(RoundedCornerShape(6.dp)),
            )
        }

        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .size(width = 120.dp, height = 12.dp)
                .vargShimmer(RoundedCornerShape(4.dp)),
        )

        Spacer(Modifier.height(18.dp))

        // Hero Motorcycle Stage Silhouette Skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(185.dp)
                .background(VargPitchBlack),
            contentAlignment = Alignment.Center,
        ) {
            // Ambient red halo behind bike
            Box(
                modifier = Modifier
                    .size(230.dp, 110.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                VargStarkRed.copy(alpha = 0.12f),
                                Color.Transparent,
                            ),
                        ),
                    ),
            )

            // Motorcycle Silhouette Placeholder with Sleek Form
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(140.dp)
                    .vargShimmer(RoundedCornerShape(16.dp)),
            )

            // Ground Floor Studio Light Line
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
        }

        Spacer(Modifier.height(14.dp))

        // High-Tech CAN-Bus Telemetry Sync Pulse Banner
        CanBusSyncingBanner()

        Spacer(Modifier.height(16.dp))

        // Services Section Skeleton
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 85.dp, height = 13.dp)
                        .vargShimmer(RoundedCornerShape(4.dp)),
                )
                Box(
                    modifier = Modifier
                        .size(width = 28.dp, height = 13.dp)
                        .vargShimmer(RoundedCornerShape(4.dp)),
                )
            }

            repeat(4) {
                ServiceCardSkeleton()
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun LandscapeLoadingSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Left Column: Bike Stage Skeleton
        Column(
            modifier = Modifier
                .weight(0.44f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 130.dp, height = 18.dp)
                    .vargShimmer(RoundedCornerShape(4.dp)),
            )
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(width = 65.dp, height = 32.dp)
                        .vargShimmer(RoundedCornerShape(6.dp)),
                )
                Box(
                    modifier = Modifier
                        .size(width = 65.dp, height = 32.dp)
                        .vargShimmer(RoundedCornerShape(6.dp)),
                )
            }
            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .vargShimmer(RoundedCornerShape(14.dp)),
            )

            Spacer(Modifier.height(12.dp))
            CanBusSyncingBanner()
        }

        // Right Column: Services Skeletons
        Column(
            modifier = Modifier
                .weight(0.56f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 14.dp)
                    .vargShimmer(RoundedCornerShape(4.dp)),
            )
            repeat(4) {
                ServiceCardSkeleton()
            }
        }
    }
}

@Composable
private fun ServiceCardSkeleton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF131316))
            .border(1.dp, Color(0xFF222228), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
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
                // Square Icon Shimmer
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .vargShimmer(RoundedCornerShape(10.dp)),
                )
                Spacer(Modifier.width(14.dp))
                Column {
                    Box(
                        modifier = Modifier
                            .size(width = 120.dp, height = 14.dp)
                            .vargShimmer(RoundedCornerShape(4.dp)),
                    )
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(width = 160.dp, height = 11.dp)
                            .vargShimmer(RoundedCornerShape(3.dp)),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(width = 10.dp, height = 14.dp)
                    .vargShimmer(RoundedCornerShape(3.dp)),
            )
        }
    }
}

@Composable
private fun CanBusSyncingBanner() {
    val pulseTransition = rememberInfiniteTransition(label = "CanBusPulse")
    val pulseAlpha by pulseTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "CanBusPulseAlpha",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF121215))
            .border(1.dp, VargSecondary.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(VargSecondary.copy(alpha = pulseAlpha), CircleShape),
                )
                Spacer(Modifier.width(9.dp))
                Column {
                    Text(
                        text = "SYNCHRONIZING CAN BUS TELEMETRY...",
                        color = VargWhite,
                        style = StarkTypographyTokens.ScaleTick.copy(
                            fontSize = 10.sp,
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "CAN-ID 0x7E0 • 500 KBPS • READING ECU",
                        color = VargTextSecondary,
                        style = StarkTypographyTokens.TechnicalLabel.copy(
                            fontSize = 8.5.sp,
                            letterSpacing = 0.8.sp,
                        ),
                    )
                }
            }

            Text(
                text = "VARG OS",
                color = VargSecondary.copy(alpha = 0.7f),
                style = StarkTypographyTokens.TechnicalHeader.copy(
                    fontSize = 9.sp,
                    letterSpacing = 1.2.sp,
                ),
            )
        }
    }
}
