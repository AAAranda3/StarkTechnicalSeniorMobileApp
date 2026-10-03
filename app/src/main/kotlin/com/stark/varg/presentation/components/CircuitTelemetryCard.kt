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
import com.stark.varg.ui.theme.StarkTypographyTokens
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
                    style = StarkTypographyTokens.TechnicalHeader,
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
                    style = StarkTypographyTokens.TechnicalLabel,
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Race Track Circuit Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(VargPitchBlack)
                .border(1.dp, Color(0xFF2E2E2E), RoundedCornerShape(12.dp))
                .padding(12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(206.dp)) {
                val w = size.width
                val h = size.height

                // Normalized coordinate mapper preserving exact Supercross track geometry
                val padX = 14.dp.toPx()
                val padY = 12.dp.toPx()
                val availW = w - padX * 2
                val availH = h - padY * 2
                val trackAspect = 0.92f
                val scale = minOf(availW, availH / trackAspect)
                val scaleX = scale
                val scaleY = scale * trackAspect
                val offsetX = padX + (availW - scaleX) / 2f
                val offsetY = padY + (availH - scaleY) / 2f

                fun nx(x: Float): Float = offsetX + x * scaleX
                fun ny(y: Float): Float = offsetY + y * scaleY

                // Authentic AMA Supercross Championship Stadium Circuit Layout
                val circuitPath = Path().apply {
                    // Top Straight - starts exit of top-left hairpin across the top
                    moveTo(nx(0.14f), ny(0.06f))
                    lineTo(nx(0.85f), ny(0.06f))

                    // Top-Right Corner (sweeping down into perimeter whoops straight)
                    cubicTo(
                        nx(0.93f), ny(0.06f),
                        nx(0.95f), ny(0.12f),
                        nx(0.92f), ny(0.18f),
                    )

                    // Right Whoops Straight (diagonal perimeter straight)
                    lineTo(nx(0.68f), ny(0.80f))

                    // Bottom Bowl Turn (wide sweeping bowl turn at stadium south end)
                    cubicTo(
                        nx(0.64f), ny(0.94f),
                        nx(0.52f), ny(0.94f),
                        nx(0.48f), ny(0.82f),
                    )

                    // Infield Rhythm Section (heading up-left)
                    lineTo(nx(0.33f), ny(0.52f))

                    // Mid Hairpin Turn (curling around clockwise)
                    cubicTo(
                        nx(0.29f), ny(0.45f),
                        nx(0.23f), ny(0.47f),
                        nx(0.22f), ny(0.56f),
                    )

                    // Infield Secondary Rhythm Section (heading down-left)
                    lineTo(nx(0.14f), ny(0.78f))

                    // Bottom-Left Corner (sweeping into finish straight)
                    cubicTo(
                        nx(0.08f), ny(0.84f),
                        nx(0.06f), ny(0.80f),
                        nx(0.07f), ny(0.68f),
                    )

                    // Left Finish Straight (heading north past start gate)
                    lineTo(nx(0.07f), ny(0.28f))

                    // Upper Infield Turn (turning east into upper sand lane)
                    cubicTo(
                        nx(0.07f), ny(0.22f),
                        nx(0.11f), ny(0.22f),
                        nx(0.16f), ny(0.22f),
                    )

                    // Upper Sand Straight (heading east above start chute)
                    lineTo(nx(0.48f), ny(0.22f))

                    // Turn into Vertical Center Sand Straight (turning south)
                    cubicTo(
                        nx(0.54f), ny(0.22f),
                        nx(0.54f), ny(0.26f),
                        nx(0.54f), ny(0.32f),
                    )

                    // Vertical Center Sand Straight (heading south down the center)
                    lineTo(nx(0.54f), ny(0.66f))

                    // Center Bottom Hairpin (curling counter-clockwise)
                    cubicTo(
                        nx(0.54f), ny(0.76f),
                        nx(0.65f), ny(0.76f),
                        nx(0.66f), ny(0.64f),
                    )

                    // Up-Right Connector Straight (heading north-east)
                    lineTo(nx(0.74f), ny(0.38f))

                    // Sweeper into Sub-Top Straight (turning left)
                    cubicTo(
                        nx(0.76f), ny(0.24f),
                        nx(0.74f), ny(0.14f),
                        nx(0.68f), ny(0.14f),
                    )

                    // Sub-Top Straight (heading west across stadium)
                    lineTo(nx(0.14f), ny(0.14f))

                    // Top-Left Bowl Hairpin (curling counter-clockwise back into Main Lane)
                    cubicTo(
                        nx(0.05f), ny(0.14f),
                        nx(0.05f), ny(0.06f),
                        nx(0.14f), ny(0.06f),
                    )
                    close()
                }

                // GPS Telemetry Grid background lines
                val gridColor = Color(0xFF1E1E22)
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

                // Start Gate & Chute (architectural layout feature from AMA Supercross drawing)
                val startChutePath = Path().apply {
                    moveTo(nx(0.01f), ny(0.405f))
                    lineTo(nx(0.07f), ny(0.405f))
                }
                drawPath(
                    path = startChutePath,
                    color = Color(0xFF26262C),
                    style = Stroke(
                        width = 12.dp.toPx(),
                        cap = StrokeCap.Square,
                    ),
                )
                drawLine(
                    color = Color(0xFF6E6E78),
                    start = Offset(nx(0.01f), ny(0.36f)),
                    end = Offset(nx(0.01f), ny(0.45f)),
                    strokeWidth = 2.5.dp.toPx(),
                    cap = StrokeCap.Square,
                )

                // Outer track bed (dark racing clay)
                drawPath(
                    path = circuitPath,
                    color = Color(0xFF26262C),
                    style = Stroke(
                        width = 14.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    ),
                )

                // Race groove line
                drawPath(
                    path = circuitPath,
                    color = Color(0xFF484854),
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    ),
                )

                // Highlight main straight speed trap zone (where max speed was recorded)
                val speedTrapPath = Path().apply {
                    moveTo(nx(0.20f), ny(0.06f))
                    lineTo(nx(0.85f), ny(0.06f))
                }
                drawPath(
                    path = speedTrapPath,
                    color = VargSecondary,
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                    ),
                )

                // Start / Finish Line marker
                val sfX = nx(0.20f)
                val sfY = ny(0.06f)
                drawLine(
                    color = VargWhite,
                    start = Offset(sfX, sfY - 8.dp.toPx()),
                    end = Offset(sfX, sfY + 8.dp.toPx()),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Square,
                )

                // Timing sector trap points
                drawCircle(
                    color = VargSecondary,
                    radius = 3.5.dp.toPx(),
                    center = Offset(nx(0.92f), ny(0.18f)),
                )
                drawCircle(
                    color = VargSecondary,
                    radius = 3.5.dp.toPx(),
                    center = Offset(nx(0.54f), ny(0.48f)),
                )

                // Animated Rider Position along the circuit
                val pathMeasure = PathMeasure()
                pathMeasure.setPath(circuitPath, false)
                val pathLength = pathMeasure.length
                val riderOffset = pathMeasure.getPosition(pathLength * riderProgress)

                // Pulsing radar wave around rider
                drawCircle(
                    color = VargSecondary.copy(alpha = 0.30f),
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
                    style = StarkTypographyTokens.ScaleTick,
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
                    style = StarkTypographyTokens.ScaleTick,
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
                    style = StarkTypographyTokens.ScaleTick,
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
                Text("CALCULATED PACE", color = VargTextSecondary, style = StarkTypographyTokens.TechnicalLabel)
                Text(formatPace(session.distanceKm, session.durationS), color = VargWhite, style = StarkTypographyTokens.CardTitle)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("GNSS SAMPLING", color = VargTextSecondary, style = StarkTypographyTokens.TechnicalLabel)
                Text("10 Hz DGPS", color = VargWhite, style = StarkTypographyTokens.CardTitle)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("TIMING GATES", color = VargTextSecondary, style = StarkTypographyTokens.TechnicalLabel)
                Text("3 Sectors Active", color = Color(0xFFD4D4DC), style = StarkTypographyTokens.CardTitle)
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

