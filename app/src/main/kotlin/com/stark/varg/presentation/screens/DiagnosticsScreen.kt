package com.stark.varg.presentation.screens

import android.content.res.Configuration
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.Warning
import com.stark.varg.domain.model.WarningSeverity
import com.stark.varg.presentation.components.StarkPill
import com.stark.varg.presentation.components.VargScreenTopAppBar
import com.stark.varg.presentation.util.VargStrings
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWarningAmber
import com.stark.varg.ui.theme.VargWhite

@Composable
fun DiagnosticsScreen(
    diagnostics: DiagnosticsData,
    timestamp: String,
    onBack: () -> Unit,
    language: AppLanguage = AppLanguage.SPANISH,
    modifier: Modifier = Modifier,
) {
    val hasCritical = diagnostics.warnings.any { it.severity == WarningSeverity.CRITICAL } || diagnostics.faultCodes.isNotEmpty()
    val alertColor = if (hasCritical) VargStarkRed else VargWarningAmber
    val hasAlerts = diagnostics.warnings.isNotEmpty() || diagnostics.faultCodes.isNotEmpty()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .padding(horizontal = 16.dp),
    ) {
        // Standard Navigation TopAppBar
        val activeText = if (language == AppLanguage.SPANISH) "ACTIVO" else "ACTIVE"
        VargScreenTopAppBar(
            title = VargStrings.diagnosticsTitle(language),
            subtitle = VargStrings.diagnosticsSubtitle(language),
            onBack = onBack,
            statusText = if (hasAlerts) "${diagnostics.warnings.size} $activeText" else "NOMINAL",
            statusColor = if (hasAlerts) alertColor else VargElectricGreen,
        )

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Left Column: Status Banner & Fault Codes
                Column(
                    modifier = Modifier
                        .weight(0.48f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    DiagnosticStatusBanner(hasAlerts = hasAlerts, alertColor = alertColor, diagnostics = diagnostics, language = language)
                    FaultCodesCard(diagnostics = diagnostics, timestamp = timestamp, language = language)
                }

                // Right Column: Active Warnings
                Column(
                    modifier = Modifier
                        .weight(0.52f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    if (diagnostics.warnings.isNotEmpty()) {
                        Text(
                            text = if (language == AppLanguage.SPANISH) "ADVERTENCIAS ACTIVAS (${diagnostics.warnings.size})" else "ACTIVE WARNINGS (${diagnostics.warnings.size})",
                            style = StarkTypographyTokens.TechnicalHeader,
                            color = VargWhite,
                        )

                        diagnostics.warnings.forEach { warning ->
                            FullWarningCard(warning = warning, language = language)
                        }
                    } else {
                        Text(
                            text = if (language == AppLanguage.SPANISH) "Sin advertencias activas" else "No active warnings",
                            color = VargTextSecondary,
                            style = StarkTypographyTokens.CardSubtitle,
                        )
                    }
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

                DiagnosticStatusBanner(hasAlerts = hasAlerts, alertColor = alertColor, diagnostics = diagnostics, language = language)

                Spacer(Modifier.height(16.dp))

                // Warnings Section strictly from JSON
                if (diagnostics.warnings.isNotEmpty()) {
                    Text(
                        text = if (language == AppLanguage.SPANISH) "ADVERTENCIAS ACTIVAS (${diagnostics.warnings.size})" else "ACTIVE WARNINGS (${diagnostics.warnings.size})",
                        style = StarkTypographyTokens.TechnicalHeader,
                        color = VargWhite,
                    )

                    Spacer(Modifier.height(8.dp))

                    diagnostics.warnings.forEach { warning ->
                        FullWarningCard(warning = warning, language = language)
                        Spacer(Modifier.height(10.dp))
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ColorDarkTile, RoundedCornerShape(14.dp))
                            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
                            .padding(18.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(VargElectricGreen, androidx.compose.foundation.shape.CircleShape),
                            )
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.SPANISH) "SIN ADVERTENCIAS ACTIVAS" else "NO ACTIVE WARNINGS",
                                    color = VargWhite,
                                    style = StarkTypographyTokens.TechnicalHeader,
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = if (language == AppLanguage.SPANISH) "Todos los sensores y módulos operando con normalidad." else "All powertrain sensors and modules operating nominally.",
                                    color = VargTextSecondary,
                                    style = StarkTypographyTokens.CardSubtitle,
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                FaultCodesCard(diagnostics = diagnostics, timestamp = timestamp, language = language)
            }
        }
    }
}

@Composable
private fun DiagnosticStatusBanner(
    hasAlerts: Boolean,
    alertColor: Color,
    diagnostics: DiagnosticsData,
    language: AppLanguage = AppLanguage.SPANISH,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (hasAlerts) alertColor.copy(alpha = 0.5f) else VargBorderSubtle,
                RoundedCornerShape(14.dp),
            )
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (hasAlerts) {
                    if (language == AppLanguage.SPANISH) "AVISOS REGISTRADOS" else "ADVISORIES REPORTED"
                } else {
                    if (language == AppLanguage.SPANISH) "SENSORES NOMINALES" else "ALL SENSORS NOMINAL"
                },
                color = if (hasAlerts) alertColor else VargElectricGreen,
                style = StarkTypographyTokens.TechnicalHeader,
            )
            Text(
                text = if (language == AppLanguage.SPANISH) {
                    "${diagnostics.warnings.size} Advertencias • ${diagnostics.faultCodes.size} Fallos"
                } else {
                    "${diagnostics.warnings.size} Warnings • ${diagnostics.faultCodes.size} Faults"
                },
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
        }
    }
}

@Composable
private fun FaultCodesCard(
    diagnostics: DiagnosticsData,
    timestamp: String,
    language: AppLanguage = AppLanguage.SPANISH,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, VargBorderSubtle, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = if (language == AppLanguage.SPANISH) "REGISTRO DE CÓDIGOS DE FALLO" else "FAULT CODES LOG",
            color = VargWhite,
            style = StarkTypographyTokens.TechnicalHeader,
        )

        if (diagnostics.faultCodes.isEmpty()) {
            Text(
                text = if (language == AppLanguage.SPANISH) "0 Códigos de fallo registrados en la memoria del vehículo." else "0 Fault codes registered in vehicle memory.",
                color = VargTextSecondary,
                style = StarkTypographyTokens.BodySmall,
            )
        } else {
            diagnostics.faultCodes.forEach { code ->
                Text(
                    text = code,
                    color = VargStarkRed,
                    style = StarkTypographyTokens.ValueMedium.copy(color = VargStarkRed),
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = if (language == AppLanguage.SPANISH) "Fecha de telemetría" else "Telemetry Timestamp",
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
            Text(
                text = formatDiagnosticsDateTime(timestamp, language),
                color = VargTextSecondary,
                style = StarkTypographyTokens.CardSubtitle,
            )
        }
    }
}

@Composable
private fun FullWarningCard(
    warning: Warning,
    language: AppLanguage = AppLanguage.SPANISH,
) {
    val severityColor = when (warning.severity) {
        WarningSeverity.CRITICAL -> VargStarkRed
        WarningSeverity.WARNING -> VargWarningAmber
        WarningSeverity.INFO -> VargWhite
        WarningSeverity.UNKNOWN -> VargTextSecondary
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorDarkTile, RoundedCornerShape(14.dp))
            .border(1.dp, severityColor.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = warning.message,
                color = VargWhite,
                style = StarkTypographyTokens.CardTitle,
                modifier = Modifier.weight(1f).padding(end = 8.dp),
            )
            StarkPill(
                text = warning.severity.name,
                backgroundColor = severityColor.copy(alpha = 0.15f),
                borderColor = severityColor.copy(alpha = 0.5f),
                textColor = severityColor,
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = if (language == AppLanguage.SPANISH) "Aviso registrado de nivel ${warning.severity.name.lowercase()}." else "Telemetry report: ${warning.severity.name.lowercase()} level advisory.",
            color = VargTextSecondary,
            style = StarkTypographyTokens.CardSubtitle,
        )
    }
}

private fun formatDiagnosticsDateTime(isoTimestamp: String, language: AppLanguage): String {
    return try {
        val instant = java.time.Instant.parse(isoTimestamp)
        val zone = java.time.ZoneId.systemDefault()
        val zdt = instant.atZone(zone)
        val locale = if (language == AppLanguage.SPANISH) java.util.Locale.forLanguageTag("es-ES") else java.util.Locale.US
        val formatter = java.time.format.DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", locale)
        zdt.format(formatter)
    } catch (_: Exception) {
        isoTimestamp
    }
}

private val ColorDarkTile = VargSurfaceCard
