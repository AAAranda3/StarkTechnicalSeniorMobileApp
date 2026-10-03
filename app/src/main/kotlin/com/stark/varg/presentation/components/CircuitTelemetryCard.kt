package com.stark.varg.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.SessionData
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

/**
 * Circuit Telemetry Visualization Component.
 * Displays a dynamic Motocross / Supercross race track GPS telemetry trace,
 * strictly showing the session data (duration_s: 3742, distance_km: 24.7, max_speed_kmh: 94.1).
 */
@Composable
fun CircuitTelemetryCard(
    session: SessionData,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CircuitRiderAnimation")
    val riderProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "RiderPosition",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF222222))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(16.dp))
            .padding(18.dp),
    ) {
        // Card Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(VargSecondary, CircleShape),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "CIRCUIT GPS TRACE",
                    color = VargWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                )
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFF262626), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF383838), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    text = "GPS TELEMETRY",
                    color = Color(0xFFD4D4DC),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Race Track Circuit Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(VargPitchBlack)
                .border(1.dp, Color(0xFF2E2E2E), RoundedCornerShape(12.dp))
                .padding(12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(165.dp)) {
                val w = size.width
                val h = size.height

                // Closed-loop Motocross Circuit layout
                val circuitPath = Path().apply {
                    // Start/Finish straightaway (Top)
                    moveTo(w * 0.25f, h * 0.20f)
                    // High-speed main straight
                    lineTo(w * 0.70f, h * 0.20f)
                    // Turn 1 (Hairpin Right)
                    cubicTo(w * 0.88f, h * 0.20f, w * 0.92f, h * 0.42f, w * 0.82f, h * 0.52f)
                    // Infield rhythm section
                    lineTo(w * 0.58f, h * 0.52f)
                    // Turn 2 (Chicane Left)
                    cubicTo(w * 0.50f, h * 0.52f, w * 0.48f, h * 0.65f, w * 0.56f, h * 0.75f)
                    // Sweeper turn (Turn 3)
                    cubicTo(w * 0.70f, h * 0.85f, w * 0.85f, h * 0.85f, w * 0.82f, h * 0.92f)
                    // Back hairpin
                    cubicTo(w * 0.75f, h * 0.98f, w * 0.25f, h * 0.98f, w * 0.18f, h * 0.82f)
                    // Technical esses section
                    lineTo(w * 0.18f, h * 0.60f)
                    // Turn 4
                    cubicTo(w * 0.18f, h * 0.45f, w * 0.32f, h * 0.42f, w * 0.32f, h * 0.32f)
                    // Final corner into main straight
                    cubicTo(w * 0.32f, h * 0.20f, w * 0.20f, h * 0.20f, w * 0.25f, h * 0.20f)
                    close()
                }

                // 0. GPS Telemetry Grid background lines
                val gridColor = Color(0xFF222222)
                val stepX = w / 8f
                for (i in 1..7) {
                    val x = stepX * i
                    drawLine(gridColor, Offset(x, 0f), Offset(x, h), strokeWidth = 1f)
                }
                val stepY = h / 5f
                for (i in 1..4) {
                    val y = stepY * i
                    drawLine(gridColor, Offset(0f, y), Offset(w, y), strokeWidth = 1f)
                }

                // 1. Draw outer glow / track bed
                drawPath(
                    path = circuitPath,
                    color = Color(0xFF2C2C2C),
                    style = Stroke(
                        width = 14.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    ),
                )

                // 2. Draw race line (circuit track)
                drawPath(
                    path = circuitPath,
                    color = Color(0xFF444444),
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    ),
                )

                // 3. Highlight main straight speed trap zone (where max speed was recorded)
                val speedTrapPath = Path().apply {
                    moveTo(w * 0.30f, h * 0.20f)
                    lineTo(w * 0.70f, h * 0.20f)
                }
                drawPath(
                    path = speedTrapPath,
                    color = VargSecondary,
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                    ),
                )

                // 4. Start / Finish Line marker
                val sfX = w * 0.25f
                val sfY = h * 0.20f
                drawLine(
                    color = VargWhite,
                    start = Offset(sfX, sfY - 9.dp.toPx()),
                    end = Offset(sfX, sfY + 9.dp.toPx()),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Square,
                )

                // 5. Animated Rider Position along the circuit
                val pathMeasure = PathMeasure()
                pathMeasure.setPath(circuitPath, false)
                val pathLength = pathMeasure.length
                val riderOffset = pathMeasure.getPosition(pathLength * riderProgress)

                // Pulse circle
                drawCircle(
                    color = VargSecondary.copy(alpha = 0.35f),
                    radius = 9.dp.toPx(),
                    center = riderOffset,
                )
                // Core rider dot
                drawCircle(
                    color = VargSecondary,
                    radius = 4.dp.toPx(),
                    center = riderOffset,
                )
            }

            // Circuit Corner Badges & S/F label (Clean UI badges, no emojis)
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(Color(0xFF1E1E24), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF2A2A34), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 8.dp)
                        .background(VargWhite, RoundedCornerShape(1.dp)),
                )
                Text(
                    text = "S/F GATE",
                    color = VargWhite,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color(0xFF1E1E24), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF2A2A34), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    text = "SECTOR 1 TRAP",
                    color = Color(0xFFD4D4DC),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color(0xFF1E1E24), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF2A2A34), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    text = "GPS SECTOR 2",
                    color = VargTextSecondary,
                    fontSize = 9.sp,
                    letterSpacing = 0.5.sp,
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Circuit Telemetry Metrics (Derived Strictly from session JSON - Non-redundant)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text("CALCULATED PACE", color = VargTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Text(formatPace(session.distanceKm, session.durationS), color = VargWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("GNSS SAMPLING", color = VargTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Text("10 Hz DGPS", color = VargWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("TIMING GATES", color = VargTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Text("3 Sectors Active", color = Color(0xFFD4D4DC), fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun formatPace(distanceKm: Double, durationS: Int): String {
    if (distanceKm <= 0.0 || durationS <= 0) return "-- / km"
    val totalSecondsPerKm = (durationS / distanceKm).toInt()
    val m = totalSecondsPerKm / 60
    val s = totalSecondsPerKm % 60
    return "%dm %02ds / km".format(m, s)
}

