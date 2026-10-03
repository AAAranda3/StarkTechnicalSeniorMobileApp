package com.stark.varg.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargWhite

/**
 * Unified Monochrome Vector Icons (Gris / Blanco).
 * Eradicates colored OS emojis to ensure enterprise consistency.
 */

@Composable
fun VargIconStats(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 18.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val barW = w * 0.22f
        val radius = CornerRadius(2.dp.toPx(), 2.dp.toPx())

        // Short bar
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.08f, h * 0.55f),
            size = Size(barW, h * 0.42f),
            cornerRadius = radius,
        )
        // Medium bar
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.39f, h * 0.25f),
            size = Size(barW, h * 0.72f),
            cornerRadius = radius,
        )
        // Tall bar
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.70f, h * 0.05f),
            size = Size(barW, h * 0.92f),
            cornerRadius = radius,
        )
    }
}

@Composable
fun VargIconModes(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 18.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.58f, 0f)
            lineTo(w * 0.18f, h * 0.55f)
            lineTo(w * 0.50f, h * 0.55f)
            lineTo(w * 0.42f, h)
            lineTo(w * 0.82f, h * 0.45f)
            lineTo(w * 0.50f, h * 0.45f)
            close()
        }
        drawPath(path = path, color = color, style = Fill)
    }
}

@Composable
fun VargIconMotor(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 18.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()

        // Classic Automotive Motor / Engine Block silhouette
        val enginePath = Path().apply {
            // Valve cover / top cap
            moveTo(w * 0.32f, h * 0.24f)
            lineTo(w * 0.32f, h * 0.12f)
            lineTo(w * 0.58f, h * 0.12f)
            lineTo(w * 0.58f, h * 0.24f)

            // Right upper shoulder
            lineTo(w * 0.74f, h * 0.24f)
            lineTo(w * 0.74f, h * 0.36f)
            // Output pulley / accessory right
            lineTo(w * 0.90f, h * 0.36f)
            lineTo(w * 0.90f, h * 0.62f)
            lineTo(w * 0.74f, h * 0.62f)
            // Lower right engine base
            lineTo(w * 0.74f, h * 0.82f)
            // Oil pan / bottom base
            lineTo(w * 0.26f, h * 0.82f)
            // Lower left engine base
            lineTo(w * 0.26f, h * 0.64f)
            // Intake / starter left
            lineTo(w * 0.10f, h * 0.64f)
            lineTo(w * 0.10f, h * 0.44f)
            lineTo(w * 0.26f, h * 0.44f)
            // Left upper shoulder
            lineTo(w * 0.26f, h * 0.24f)
            close()
        }

        drawPath(
            path = enginePath,
            color = color,
            style = Stroke(
                width = stroke,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )

        // Internal engine / motor cooling lines
        drawLine(
            color = color,
            start = Offset(w * 0.40f, h * 0.44f),
            end = Offset(w * 0.60f, h * 0.44f),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(w * 0.36f, h * 0.60f),
            end = Offset(w * 0.64f, h * 0.60f),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
fun VargIconDiagnostics(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 18.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        // Hazard Triangle
        val path = Path().apply {
            moveTo(w / 2f, h * 0.08f)
            lineTo(w * 0.94f, h * 0.92f)
            lineTo(w * 0.06f, h * 0.92f)
            close()
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )
        // Exclamation point stem
        drawLine(
            color = color,
            start = Offset(w / 2f, h * 0.40f),
            end = Offset(w / 2f, h * 0.65f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
        )
        // Exclamation point dot
        drawCircle(
            color = color,
            radius = 1.2.dp.toPx(),
            center = Offset(w / 2f, h * 0.79f),
        )
    }
}

@Composable
fun VargIconChevronLeft(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 20.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.65f, h * 0.15f)
            lineTo(w * 0.30f, h * 0.50f)
            lineTo(w * 0.65f, h * 0.85f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = 2.5.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )
    }
}

@Composable
fun VargIconChevronRight(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 16.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.35f, h * 0.20f)
            lineTo(w * 0.65f, h * 0.50f)
            lineTo(w * 0.35f, h * 0.80f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )
    }
}

@Composable
fun VargIconGarage(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 22.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()
        // Roof gable
        val roof = Path().apply {
            moveTo(w * 0.12f, h * 0.36f)
            lineTo(w * 0.50f, h * 0.14f)
            lineTo(w * 0.88f, h * 0.36f)
        }
        drawPath(roof, color, style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Vehicle outline underneath
        val vehicle = Path().apply {
            moveTo(w * 0.22f, h * 0.54f)
            lineTo(w * 0.34f, h * 0.40f)
            lineTo(w * 0.66f, h * 0.40f)
            lineTo(w * 0.78f, h * 0.54f)
            lineTo(w * 0.85f, h * 0.62f)
            lineTo(w * 0.85f, h * 0.82f)
            lineTo(w * 0.15f, h * 0.82f)
            lineTo(w * 0.15f, h * 0.62f)
            close()
        }
        drawPath(vehicle, color, style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Headlights
        drawCircle(color, radius = 1.4.dp.toPx(), center = Offset(w * 0.28f, h * 0.66f))
        drawCircle(color, radius = 1.4.dp.toPx(), center = Offset(w * 0.72f, h * 0.66f))
    }
}

@Composable
fun VargIconBell(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 20.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()
        val bell = Path().apply {
            moveTo(w * 0.50f, h * 0.12f)
            cubicTo(w * 0.32f, h * 0.15f, w * 0.26f, h * 0.44f, w * 0.22f, h * 0.68f)
            lineTo(w * 0.15f, h * 0.75f)
            lineTo(w * 0.85f, h * 0.75f)
            lineTo(w * 0.78f, h * 0.68f)
            cubicTo(w * 0.74f, h * 0.44f, w * 0.68f, h * 0.15f, w * 0.50f, h * 0.12f)
            close()
        }
        drawPath(bell, color, style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.38f, h * 0.78f),
            size = Size(w * 0.24f, h * 0.14f),
            style = Stroke(stroke, cap = StrokeCap.Round),
        )
    }
}

@Composable
fun VargIconLock(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 18.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()
        // Shackle
        drawArc(
            color = color,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.28f, h * 0.14f),
            size = Size(w * 0.44f, h * 0.42f),
            style = Stroke(stroke, cap = StrokeCap.Round),
        )
        // Body
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.20f, h * 0.42f),
            size = Size(w * 0.60f, h * 0.46f),
            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
            style = Stroke(stroke),
        )
        // Keyhole
        drawCircle(color, radius = 1.6.dp.toPx(), center = Offset(w * 0.50f, h * 0.60f))
    }
}

@Composable
fun VargIconPower(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 18.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()
        drawArc(
            color = color,
            startAngle = 130f,
            sweepAngle = 280f,
            useCenter = false,
            topLeft = Offset(w * 0.18f, h * 0.20f),
            size = Size(w * 0.64f, h * 0.64f),
            style = Stroke(stroke, cap = StrokeCap.Round),
        )
        drawLine(
            color = color,
            start = Offset(w * 0.50f, h * 0.10f),
            end = Offset(w * 0.50f, h * 0.48f),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
fun VargIconUnlock(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 18.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()
        // Open raised shackle
        drawArc(
            color = color,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.36f, h * 0.04f),
            size = Size(w * 0.44f, h * 0.42f),
            style = Stroke(stroke, cap = StrokeCap.Round),
        )
        // Body
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.20f, h * 0.42f),
            size = Size(w * 0.60f, h * 0.46f),
            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
            style = Stroke(stroke),
        )
        // Keyhole
        drawCircle(color, radius = 1.6.dp.toPx(), center = Offset(w * 0.50f, h * 0.60f))
    }
}

@Composable
fun VargIconHorn(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 18.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()
        // Bugle / Horn path
        val horn = Path().apply {
            moveTo(w * 0.20f, h * 0.56f)
            cubicTo(w * 0.35f, h * 0.56f, w * 0.42f, h * 0.68f, w * 0.52f, h * 0.68f)
            cubicTo(w * 0.62f, h * 0.68f, w * 0.62f, h * 0.40f, w * 0.52f, h * 0.40f)
            lineTo(w * 0.28f, h * 0.40f)
            cubicTo(w * 0.16f, h * 0.40f, w * 0.16f, h * 0.56f, w * 0.28f, h * 0.56f)
            // Bell flare
            moveTo(w * 0.50f, h * 0.40f)
            lineTo(w * 0.78f, h * 0.26f)
            lineTo(w * 0.78f, h * 0.74f)
            lineTo(w * 0.50f, h * 0.60f)
        }
        drawPath(horn, color, style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Sound arcs
        drawArc(
            color = color,
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(w * 0.76f, h * 0.32f),
            size = Size(w * 0.18f, h * 0.36f),
            style = Stroke(stroke, cap = StrokeCap.Round),
        )
    }
}

@Composable
fun VargIconLights(
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 18.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()

        // Central headlamp dome
        val dome = Path().apply {
            moveTo(w * 0.38f, h * 0.25f)
            lineTo(w * 0.62f, h * 0.25f)
            cubicTo(w * 0.74f, h * 0.25f, w * 0.74f, h * 0.75f, w * 0.62f, h * 0.75f)
            lineTo(w * 0.38f, h * 0.75f)
            cubicTo(w * 0.26f, h * 0.75f, w * 0.26f, h * 0.25f, w * 0.38f, h * 0.25f)
            close()
        }
        drawPath(dome, color, style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Left rays
        drawLine(color, Offset(w * 0.22f, h * 0.34f), Offset(w * 0.08f, h * 0.28f), stroke, StrokeCap.Round)
        drawLine(color, Offset(w * 0.20f, h * 0.50f), Offset(w * 0.06f, h * 0.50f), stroke, StrokeCap.Round)
        drawLine(color, Offset(w * 0.22f, h * 0.66f), Offset(w * 0.08f, h * 0.72f), stroke, StrokeCap.Round)

        // Right rays
        drawLine(color, Offset(w * 0.78f, h * 0.34f), Offset(w * 0.92f, h * 0.28f), stroke, StrokeCap.Round)
        drawLine(color, Offset(w * 0.80f, h * 0.50f), Offset(w * 0.94f, h * 0.50f), stroke, StrokeCap.Round)
        drawLine(color, Offset(w * 0.78f, h * 0.66f), Offset(w * 0.92f, h * 0.72f), stroke, StrokeCap.Round)
    }
}

@Composable
fun VargIconBatteryVertical(
    percentage: Int,
    modifier: Modifier = Modifier,
    color: Color = VargSecondary,
    sizeDp: Dp = 18.dp,
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val stroke = 1.3.dp.toPx()
        val capW = w * 0.40f
        val capH = h * 0.10f
        val bodyW = w * 0.70f
        val bodyH = h * 0.82f
        val bodyX = (w - bodyW) / 2f
        val bodyY = capH + (h * 0.04f)

        // Terminal Cap on top
        drawRoundRect(
            color = color,
            topLeft = Offset((w - capW) / 2f, 0f),
            size = Size(capW, capH),
            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx()),
        )

        // Battery outer shell
        drawRoundRect(
            color = color,
            topLeft = Offset(bodyX, bodyY),
            size = Size(bodyW, bodyH),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = Stroke(stroke),
        )

        // Fill from bottom up
        val fillPct = (percentage.coerceIn(0, 100)) / 100f
        if (fillPct > 0f) {
            val innerPadding = 2.dp.toPx()
            val fillW = bodyW - (innerPadding * 2)
            val maxFillH = bodyH - (innerPadding * 2)
            val currentFillH = maxFillH * fillPct
            val fillY = bodyY + bodyH - innerPadding - currentFillH

            drawRoundRect(
                color = color,
                topLeft = Offset(bodyX + innerPadding, fillY),
                size = Size(fillW, currentFillH),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx()),
            )
        }
    }
}

