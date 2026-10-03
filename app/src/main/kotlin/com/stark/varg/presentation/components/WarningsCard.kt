package com.stark.varg.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.DiagnosticsData
import com.stark.varg.domain.model.WarningSeverity
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargWarningAmber
import com.stark.varg.ui.theme.VargWhite

/**
 * Compact clickable alert strip that shows human-readable messages without raw code strings.
 */
@Composable
fun WarningsCard(
    diagnostics: DiagnosticsData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    if (diagnostics.warnings.isEmpty() && diagnostics.faultCodes.isEmpty()) return

    val hasCritical = diagnostics.warnings.any { it.severity == WarningSeverity.CRITICAL } || diagnostics.faultCodes.isNotEmpty()
    val alertColor = if (hasCritical) VargStarkRed else VargWarningAmber
    val alertBg = if (hasCritical) VargStarkRed.copy(alpha = 0.16f) else VargWarningAmber.copy(alpha = 0.14f)

    // User friendly message only (never show raw code like W_MOT_TEMP_HIGH)
    val friendlyMessage = when {
        diagnostics.warnings.isNotEmpty() -> diagnostics.warnings.first().message
        diagnostics.faultCodes.isNotEmpty() -> "System Diagnostic Advisory"
        else -> "System Alert"
    }

    val totalCount = diagnostics.warnings.size + diagnostics.faultCodes.size

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(alertBg, RoundedCornerShape(12.dp))
            .border(1.dp, alertColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 9.dp)
            .testTag("warnings_card"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "⚠",
                color = alertColor,
                fontSize = 14.sp,
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = friendlyMessage,
                color = VargWhite,
                style = StarkTypographyTokens.CardTitle,
                maxLines = 1,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            StarkPill(
                text = if (totalCount > 1) "$totalCount ALERTS ›" else "DETAILS ›",
                backgroundColor = alertColor.copy(alpha = 0.2f),
                borderColor = alertColor.copy(alpha = 0.5f),
                textColor = alertColor,
            )
        }
    }
}
