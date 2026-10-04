package com.stark.varg.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.SessionData
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.domain.util.UnitConverter
import com.stark.varg.presentation.components.CircuitTelemetryCard
import com.stark.varg.presentation.components.VargScreenTopAppBar
import com.stark.varg.presentation.util.VargStrings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

@Composable
fun StatsScreen(
    session: SessionData,
    timestamp: String,
    onBack: () -> Unit,
    unitSystem: UnitSystem = UnitSystem.METRIC,
    language: AppLanguage = AppLanguage.SPANISH,
    modifier: Modifier = Modifier,
) {
    val (distVal, distUnit) = UnitConverter.formatDistance(session.distanceKm, unitSystem)
    val (maxSpeedVal, maxSpeedUnit) = UnitConverter.formatSpeed(session.maxSpeedKmh, unitSystem)

    val avgSpeed = if (session.durationS > 0) {
        val hours = session.durationS / 3600.0
        val avgKmh = session.distanceKm / hours
        val (avgVal, avgUnit) = UnitConverter.formatSpeed(avgKmh, unitSystem)
        "$avgVal $avgUnit"
    } else {
        "-- $maxSpeedUnit"
    }

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
            title = VargStrings.statsTitle(language),
            subtitle = VargStrings.statsSubtitle(language),
            onBack = onBack,
            statusText = if (language == AppLanguage.SPANISH) "REGISTRADO" else "RECORDED",
            statusColor = Color(0xFFD4D4DC),
        )

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Left Column: Metrics Grid & Session Metadata Log
                Column(
                    modifier = Modifier
                        .weight(0.48f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        MetricCard(
                            label = "TOTAL DISTANCE",
                            value = "$distVal $distUnit",
                            modifier = Modifier.weight(1f),
                        )
                        MetricCard(
                            label = "MAX SPEED",
                            value = "$maxSpeedVal $maxSpeedUnit",
                            modifier = Modifier.weight(1f),
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        MetricCard(
                            label = "SESSION TIME",
                            value = formatFullDuration(session.durationS),
                            modifier = Modifier.weight(1f),
                        )
                        MetricCard(
                            label = "AVERAGE SPEED",
                            value = avgSpeed,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    SessionLogCard(session = session, timestamp = timestamp)
                }

                // Right Column: Motocross / Race Track GPS Circuit Telemetry Visualizer
                Column(
                    modifier = Modifier
                        .weight(0.52f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                ) {
                    CircuitTelemetryCard(session = session)
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

                // Primary Metrics Grid strictly from session JSON
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MetricCard(
                        label = "TOTAL DISTANCE",
                        value = "$distVal $distUnit",
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        label = "MAX SPEED",
                        value = "$maxSpeedVal $maxSpeedUnit",
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MetricCard(
                        label = "SESSION TIME",
                        value = formatFullDuration(session.durationS),
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        label = "AVERAGE SPEED",
                        value = avgSpeed,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Motocross / Race Track GPS Circuit Telemetry Visualizer
                CircuitTelemetryCard(session = session)

                Spacer(Modifier.height(14.dp))

                // Session Metadata & Telemetry Log (Non-redundant technical details)
                SessionLogCard(session = session, timestamp = timestamp)
            }
        }
    }
}

@Composable
private fun SessionLogCard(
    session: SessionData,
    timestamp: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "SESSION METADATA & TELEMETRY LOG",
            style = StarkTypographyTokens.TechnicalHeader,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Recorded Timestamp", color = VargTextSecondary, style = StarkTypographyTokens.CardSubtitle)
            Text(timestamp, color = VargWhite, style = StarkTypographyTokens.ScaleTick)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Raw Elapsed Duration", color = VargTextSecondary, style = StarkTypographyTokens.CardSubtitle)
            Text("${session.durationS} seconds", color = VargWhite, style = StarkTypographyTokens.ValueMedium)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Distance Logged", color = VargTextSecondary, style = StarkTypographyTokens.CardSubtitle)
            Text("${(session.distanceKm * 1000).toInt()} meters", color = VargWhite, style = StarkTypographyTokens.ValueMedium)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Telemetry Sync Status", color = VargTextSecondary, style = StarkTypographyTokens.CardSubtitle)
            Text("100% Synchronized", color = Color(0xFFD4D4DC), style = StarkTypographyTokens.ValueMedium)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Telemetry Bus Channel", color = VargTextSecondary, style = StarkTypographyTokens.CardSubtitle)
            Text("CAN Bus Varg Logger", color = VargTextSecondary, style = StarkTypographyTokens.CardSubtitle)
        }
    }
}

@Composable
private fun MetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(16.dp),
    ) {
        Text(
            text = value,
            color = VargWhite,
            style = StarkTypographyTokens.MetricSmall,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = StarkTypographyTokens.TechnicalHeader,
        )
    }
}

private fun formatFullDuration(durationS: Int): String {
    val h = durationS / 3600
    val m = (durationS % 3600) / 60
    val s = durationS % 60
    return "%dh %02dm %02ds".format(h, m, s)
}

private val ColorDarkTile = VargSurfaceCard
