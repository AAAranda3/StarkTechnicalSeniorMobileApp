package com.stark.varg.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.MotorStatus
import com.stark.varg.domain.model.RideSettings
import com.stark.varg.presentation.components.VargIconModes
import com.stark.varg.presentation.components.VargScreenTopAppBar
import com.stark.varg.presentation.util.VargStrings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite
import kotlin.math.roundToInt

@Composable
fun PowerModesScreen(
    settings: RideSettings,
    motor: MotorStatus,
    selectedMode: String,
    maxPowerHp: Int,
    engineBrakingPct: Int,
    regenPct: Int,
    onModeSelect: (String) -> Unit,
    onPowerChange: (Int) -> Unit,
    onEngineBrakingChange: (Int) -> Unit,
    onRegenChange: (Int) -> Unit,
    onBack: () -> Unit,
    language: AppLanguage = AppLanguage.SPANISH,
    modifier: Modifier = Modifier,
) {
    val modes = listOf("M1", "M2", "M3", "M4", "M5")

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .padding(horizontal = 16.dp),
    ) {
        // Standard Navigation TopAppBar
        VargScreenTopAppBar(
            title = VargStrings.powerModesTitle(language),
            subtitle = VargStrings.powerModesSubtitle(
                language,
                "${selectedMode.uppercase()} • ${com.stark.varg.presentation.util.VargModeUtil.getMapName(selectedMode, settings.powerMap)}"
            ),
            onBack = onBack,
        )

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Left Column: Modes & Max Power
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Selectable Modes Pills Row
                    ModesPillsRow(
                        modes = modes,
                        selectedMode = selectedMode,
                        onModeSelect = onModeSelect,
                    )

                    // Header for Selected Mode & Map
                    ModeProfileHeader(
                        selectedMode = selectedMode,
                        settings = settings,
                        motor = motor,
                    )

                    // Interactive Slider: Max Power Limit
                    InteractiveSliderCard(
                        title = "MAX POWER LIMIT",
                        unit = "HP",
                        currentValue = maxPowerHp.toFloat(),
                        range = 10f..80f,
                        accentColor = VargSecondary,
                        onValueChange = { onPowerChange(it.toInt()) },
                    )
                }

                // Right Column: Braking Controls
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Interactive Slider: Regenerative Braking
                    InteractiveSliderCard(
                        title = "REGENERATIVE BRAKING",
                        unit = "%",
                        currentValue = regenPct.toFloat(),
                        range = 0f..100f,
                        accentColor = VargSecondary,
                        onValueChange = { onRegenChange(it.toInt()) },
                    )

                    // Interactive Slider: Engine Braking
                    InteractiveSliderCard(
                        title = "ENGINE BRAKING",
                        unit = "%",
                        currentValue = engineBrakingPct.toFloat(),
                        range = 0f..100f,
                        accentColor = VargSecondary,
                        onValueChange = { onEngineBrakingChange(it.toInt()) },
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp),
            ) {
                Spacer(Modifier.height(8.dp))

                // Selectable Modes Pills Row (Maintains state upon click)
                ModesPillsRow(
                    modes = modes,
                    selectedMode = selectedMode,
                    onModeSelect = onModeSelect,
                )

                Spacer(Modifier.height(20.dp))

                // Header for Selected Mode & Map
                ModeProfileHeader(
                    selectedMode = selectedMode,
                    settings = settings,
                    motor = motor,
                )

                Spacer(Modifier.height(16.dp))

                // Interactive Slider: Max Power Limit
                InteractiveSliderCard(
                    title = "MAX POWER LIMIT",
                    unit = "HP",
                    currentValue = maxPowerHp.toFloat(),
                    range = 10f..80f,
                    accentColor = VargSecondary,
                    onValueChange = { onPowerChange(it.toInt()) },
                )

                Spacer(Modifier.height(14.dp))

                // Interactive Slider: Regenerative Braking
                InteractiveSliderCard(
                    title = "REGENERATIVE BRAKING",
                    unit = "%",
                    currentValue = regenPct.toFloat(),
                    range = 0f..100f,
                    accentColor = VargSecondary,
                    onValueChange = { onRegenChange(it.toInt()) },
                )

                Spacer(Modifier.height(14.dp))

                // Interactive Slider: Engine Braking
                InteractiveSliderCard(
                    title = "ENGINE BRAKING",
                    unit = "%",
                    currentValue = engineBrakingPct.toFloat(),
                    range = 0f..100f,
                    accentColor = VargSecondary,
                    onValueChange = { onEngineBrakingChange(it.toInt()) },
                )
            }
        }
    }
}

@Composable
private fun ModesPillsRow(
    modes: List<String>,
    selectedMode: String,
    onModeSelect: (String) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        modes.forEach { mode ->
            val isSelected = mode == selectedMode
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .background(
                        color = if (isSelected) VargWhite else ColorDarkTile,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) VargWhite else VargBorderSubtle,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .semantics {
                        contentDescription = "Power mode $mode. ${if (isSelected) "Selected" else "Not selected"}"
                        role = Role.RadioButton
                        selected = isSelected
                    }
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onModeSelect(mode)
                    },
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    VargIconModes(
                        color = if (isSelected) VargPitchBlack else VargTextSecondary.copy(alpha = 0.5f),
                        sizeDp = 12.dp,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = mode,
                        color = if (isSelected) VargPitchBlack else VargTextSecondary,
                        style = StarkTypographyTokens.CardTitle.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeProfileHeader(
    selectedMode: String,
    settings: RideSettings,
    motor: MotorStatus,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(16.dp),
    ) {
        val modeMap = com.stark.varg.presentation.util.VargModeUtil.getMapName(selectedMode, settings.powerMap)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$selectedMode $modeMap",
                color = VargWhite,
                style = StarkTypographyTokens.CardTitle,
            )
            Box(
                modifier = Modifier
                    .background(Color(0xFF222228), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF363642), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    text = "MAP: $modeMap",
                    color = VargWhite,
                    style = StarkTypographyTokens.TechnicalLabel,
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Current motor delivery: ${motor.powerHp} HP • Temp: ${motor.temperatureC}°C",
            color = VargTextSecondary,
            style = StarkTypographyTokens.CardSubtitle,
        )
    }
}

@Composable
private fun InteractiveSliderCard(
    title: String,
    unit: String,
    currentValue: Float,
    range: ClosedFloatingPointRange<Float>,
    accentColor: Color,
    onValueChange: (Float) -> Unit,
) {
    val startInt = range.start.toInt()
    val endInt = range.endInclusive.toInt()
    val tickSteps = ((startInt / 10) * 10..endInt step 10).filter { it in startInt..endInt }
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(horizontal = 18.dp, vertical = 16.dp),
    ) {
        // Top Row: Title (Universal-sans uppercase) on Left, Value (Stark Sans ExtraLight) on Right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                color = VargTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp,
                fontFamily = FontFamily.SansSerif,
            )

            // Value Display: Large number in Stark Sans ExtraLight + tracked unit
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = "${currentValue.toInt()}",
                    color = VargWhite,
                    fontSize = 28.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.ExtraLight,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = (-0.5).sp,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = unit,
                    color = accentColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(bottom = 2.dp),
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Precision Gauge Track with Señaladores de 10 en 10
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .semantics {
                    contentDescription = "$title slider, currently ${currentValue.toInt()} $unit"
                    progressBarRangeInfo = ProgressBarRangeInfo(
                        current = currentValue,
                        range = range,
                        steps = (range.endInclusive - range.start).toInt(),
                    )
                }
                .pointerInput(range, currentValue) {
                    detectTapGestures { offset ->
                        val fraction = (offset.x / size.width).coerceIn(0f, 1f)
                        val newValue = (range.start + fraction * (range.endInclusive - range.start)).roundToInt().toFloat()
                        if (newValue != currentValue) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onValueChange(newValue)
                        }
                    }
                }
                .pointerInput(range, currentValue) {
                    detectHorizontalDragGestures { change, _ ->
                        change.consume()
                        val fraction = (change.position.x / size.width).coerceIn(0f, 1f)
                        val newValue = (range.start + fraction * (range.endInclusive - range.start)).roundToInt().toFloat()
                        if (newValue != currentValue) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onValueChange(newValue)
                        }
                    }
                },
        ) {
            val w = size.width
            val h = size.height
            val trackHeight = 6.dp.toPx()
            val trackY = (h - trackHeight) / 2f
            val fraction = ((currentValue - range.start) / (range.endInclusive - range.start)).coerceIn(0f, 1f)
            val activeWidth = w * fraction

            // Recessed Channel Background
            drawRoundRect(
                color = Color(0xFF1E1E24),
                topLeft = Offset(0f, trackY),
                size = Size(w, trackHeight),
                cornerRadius = CornerRadius(trackHeight / 2f, trackHeight / 2f),
            )
            drawRoundRect(
                color = Color(0xFF2C2C36),
                topLeft = Offset(0f, trackY),
                size = Size(w, trackHeight),
                cornerRadius = CornerRadius(trackHeight / 2f, trackHeight / 2f),
                style = Stroke(width = 1.dp.toPx()),
            )

            // Active Luminous Fill (Gold Bronze Gradient)
            if (activeWidth > 0f) {
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF9E7E24),
                            accentColor,
                            Color(0xFFE2C46A),
                        ),
                        startX = 0f,
                        endX = activeWidth.coerceAtLeast(1f),
                    ),
                    topLeft = Offset(0f, trackY),
                    size = Size(activeWidth, trackHeight),
                    cornerRadius = CornerRadius(trackHeight / 2f, trackHeight / 2f),
                )
            }

            // Señaladores de 10 en 10 (Graduation Ticks)
            tickSteps.forEach { tick ->
                val f = (tick - range.start) / (range.endInclusive - range.start)
                val tickX = w * f
                val isPast = tick <= currentValue

                val tickH = 14.dp.toPx()
                val tickTop = (h - tickH) / 2f
                val tickBottom = tickTop + tickH

                drawLine(
                    color = if (isPast) Color(0xFFE8C86A) else Color(0xFF383842),
                    start = Offset(tickX, tickTop),
                    end = Offset(tickX, tickBottom),
                    strokeWidth = if (isPast) 1.8.dp.toPx() else 1.dp.toPx(),
                )
            }

            // Precision Needle Cursor Thumb
            val thumbWidth = 4.dp.toPx()
            val thumbHeight = 24.dp.toPx()
            val thumbX = (activeWidth - thumbWidth / 2f).coerceIn(0f, w - thumbWidth)
            val thumbTop = (h - thumbHeight) / 2f

            // Bezel halo
            drawRoundRect(
                color = accentColor,
                topLeft = Offset(thumbX - 1.dp.toPx(), thumbTop - 1.dp.toPx()),
                size = Size(thumbWidth + 2.dp.toPx(), thumbHeight + 2.dp.toPx()),
                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
            )
            // Crisp white indicator
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(thumbX, thumbTop),
                size = Size(thumbWidth, thumbHeight),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            )
        }

        Spacer(Modifier.height(4.dp))

        // Señaladores Numeric Labels (10, 20, 30, ...) centered precisely at each tick
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
        ) {
            val totalWidth = maxWidth
            tickSteps.forEach { tick ->
                val f = (tick - range.start) / (range.endInclusive - range.start)
                val itemWidth = 24.dp
                val rawOffset = totalWidth * f - (itemWidth / 2)
                val clampedOffset = rawOffset.coerceIn(0.dp, totalWidth - itemWidth)
                val isNear = (currentValue.toInt() / 10) * 10 == tick

                Text(
                    text = "$tick",
                    color = if (isNear) VargWhite else VargTextSecondary.copy(alpha = 0.65f),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (isNear) FontWeight.Medium else FontWeight.Light,
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(x = clampedOffset)
                        .width(itemWidth),
                )
            }
        }
    }
}

private val ColorDarkTile = VargSurfaceCard
