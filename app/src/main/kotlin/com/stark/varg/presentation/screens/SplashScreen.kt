package com.stark.varg.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.R
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Cinematic startup splash screen displaying the Stark Varg EX visual
 * with smooth entrance transitions and technical powertrain initialization telemetry.
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hudAlpha = remember { Animatable(0f) }
    val progressAnim = remember { Animatable(0f) }
    var statusText by remember { mutableStateOf("INITIALIZING CAN BUS...") }

    // Pulsing radar glow for live connection indicator
    val infiniteTransition = rememberInfiniteTransition(label = "SplashPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "PulseAlpha",
    )

    LaunchedEffect(Unit) {
        // Entrance animation of telemetry HUD overlay
        launch {
            hudAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
            )
        }

        // Progress bar and system boot sequence
        launch {
            progressAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            )
        }

        // Dynamic telemetry diagnostics stages
        delay(650)
        statusText = "CALIBRATING VARG POWERTRAIN..."
        delay(750)
        statusText = "SYSTEMS ONLINE • READY"
        delay(700)

        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onSplashFinished()
            },
    ) {
        // Full screen motorcycle hero visual occupying practically the entire space
        Image(
            painter = painterResource(id = R.drawable.stark_splash),
            contentDescription = "Stark Varg",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        // Soft bottom gradient scrim to guarantee telemetry legibility over any display
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            VargPitchBlack.copy(alpha = 0.7f),
                            VargPitchBlack.copy(alpha = 0.95f),
                        ),
                    ),
                ),
        )

        // Top Stark Branding
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 28.dp)
                .alpha(hudAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "STARK VARG",
                color = VargWhite,
                style = StarkTypographyTokens.BrandTitle.copy(
                    fontSize = 18.sp,
                    letterSpacing = 6.sp,
                    fontWeight = FontWeight.Light,
                ),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "ELECTRIC POWERTRAIN ARCHITECTURE",
                color = VargSecondary.copy(alpha = 0.85f),
                style = StarkTypographyTokens.TechnicalLabel.copy(
                    fontSize = 9.sp,
                    letterSpacing = 2.sp,
                ),
            )
        }

        // Bottom Telemetry Initialization Status Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 24.dp)
                .alpha(hudAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(VargSecondary.copy(alpha = pulseAlpha), CircleShape),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = statusText,
                        color = VargWhite,
                        style = StarkTypographyTokens.ScaleTick.copy(
                            fontSize = 11.sp,
                            letterSpacing = 1.2.sp,
                        ),
                    )
                }

                Text(
                    text = "${(progressAnim.value * 100).toInt()}%",
                    color = VargSecondary,
                    style = StarkTypographyTokens.ScaleTick.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }

            Spacer(Modifier.height(10.dp))

            // Precision High-Voltage Progress Beam
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF1E1E24)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progressAnim.value.coerceIn(0f, 1f))
                        .height(3.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF8E6E1A),
                                    VargSecondary,
                                    Color(0xFFF4D884),
                                ),
                            ),
                        ),
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "TAP TO SKIP",
                color = VargTextSecondary.copy(alpha = 0.5f),
                style = StarkTypographyTokens.TechnicalLabel.copy(
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp,
                ),
            )
        }
    }
}
