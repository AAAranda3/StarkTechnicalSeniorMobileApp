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

    val distLabel = if (language == AppLanguage.SPANISH) "DISTANCIA TOTAL" else "TOTAL DISTANCE"
    val maxSpeedLabel = if (language == AppLanguage.SPANISH) "VELOCIDAD MÁXIMA" else "MAX SPEED"
    val sessionTimeLabel = if (language == AppLanguage.SPANISH) "TIEMPO DE SESIÓN" else "SESSION TIME"
    val avgSpeedLabel = if (language == AppLanguage.SPANISH) "VELOCIDAD MEDIA" else "AVERAGE SPEED"

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
                            label = distLabel,
                            value = "$distVal $distUnit",
                            modifier = Modifier.weight(1f),
                        )
                        MetricCard(
                            label = maxSpeedLabel,
                            value = "$maxSpeedVal $maxSpeedUnit",
                            modifier = Modifier.weight(1f),
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        MetricCard(
                            label = sessionTimeLabel,
                            value = formatFullDuration(session.durationS),
                            modifier = Modifier.weight(1f),
                        )
                        MetricCard(
                            label = avgSpeedLabel,
                            value = avgSpeed,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    SessionLogCard(session = session, timestamp = timestamp, language = language)
                }

                // Right Column: Motocross / Race Track GPS Circuit Telemetry Visualizer
                Column(
                    modifier = Modifier
                        .weight(0.52f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                ) {
                    CircuitTelemetryCard(session = session, language = language)
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
                        label = distLabel,
                        value = "$distVal $distUnit",
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        label = maxSpeedLabel,
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
                        label = sessionTimeLabel,
                        value = formatFullDuration(session.durationS),
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        label = avgSpeedLabel,
                        value = avgSpeed,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Motocross / Race Track GPS Circuit Telemetry Visualizer
                CircuitTelemetryCard(session = session, language = language)

                Spacer(Modifier.height(14.dp))

                // Session Metadata & Telemetry Log
                SessionLogCard(session = session, timestamp = timestamp, language = language)
            }
        }
    }
}

@Composable
private fun SessionLogCard(
    session: SessionData,
    timestamp: String,
    language: AppLanguage = AppLanguage.SPANISH,
    modifier: Modifier = Modifier,
) {
    val dateStr = formatSessionDate(timestamp, language)
    val timeStr = formatSessionTime(timestamp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = if (language == AppLanguage.SPANISH) "METADATOS Y REGISTRO DE SESIÓN" else "SESSION METADATA & TELEMETRY LOG",
            style = StarkTypographyTokens.TechnicalHeader,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = if (language == AppLanguage.SPANISH) "Fecha de registro" else "Recorded Date",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text(dateStr, color = VargWhite, style = StarkTypographyTokens.ValueMedium)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = if (language == AppLanguage.SPANISH) "Hora de registro" else "Recorded Time",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text(timeStr, color = VargWhite, style = StarkTypographyTokens.ValueMedium)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = if (language == AppLanguage.SPANISH) "Duración exacta" else "Raw Elapsed Duration",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text(
                text = if (language == AppLanguage.SPANISH) "${session.durationS} segundos" else "${session.durationS} seconds",
                color = VargWhite,
                style = StarkTypographyTokens.ValueMedium,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = if (language == AppLanguage.SPANISH) "Distancia registrada" else "Distance Logged",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text(
                text = if (language == AppLanguage.SPANISH) "${(session.distanceKm * 1000).toInt()} metros" else "${(session.distanceKm * 1000).toInt()} meters",
                color = VargWhite,
                style = StarkTypographyTokens.ValueMedium,
            )
        }
    }
}

private fun formatSessionDate(isoTimestamp: String, language: AppLanguage): String {
    return try {
        val instant = java.time.Instant.parse(isoTimestamp)
        val zone = java.time.ZoneId.systemDefault()
        val zdt = instant.atZone(zone)
        val locale = if (language == AppLanguage.SPANISH) java.util.Locale.forLanguageTag("es-ES") else java.util.Locale.US
        val formatter = java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy", locale)
        zdt.format(formatter).replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    } catch (_: Exception) {
        isoTimestamp
    }
}

private fun formatSessionTime(isoTimestamp: String): String {
    return try {
        val instant = java.time.Instant.parse(isoTimestamp)
        val zone = java.time.ZoneId.systemDefault()
        val zdt = instant.atZone(zone)
        val formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
        zdt.format(formatter)
    } catch (_: Exception) {
        "--:--"
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
