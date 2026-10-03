package com.stark.varg.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.ui.theme.StarkTypography
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargSecondary
import com.stark.varg.ui.theme.VargSurfaceDark
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

/**
 * Enterprise Navigation TopAppBar for full-screen destinations.
 * Replaces the ad-hoc back buttons with standard, high-fidelity app bar navigation.
 */
@Composable
fun VargScreenTopAppBar(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    statusText: String? = null,
    statusColor: Color = VargElectricGreen,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(VargPitchBlack)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Navigation Back Button + Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clickable { onBack() }
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                VargIconChevronLeft(color = VargSecondary, sizeDp = 22.dp)
            }

            Spacer(Modifier.width(4.dp))

            Column {
                Text(
                    text = title,
                    color = VargWhite,
                    style = StarkTypography.titleLarge,
                    letterSpacing = 1.sp,
                )
                Text(
                    text = subtitle,
                    color = VargTextSecondary,
                    fontSize = 11.sp,
                    letterSpacing = 0.3.sp,
                )
            }
        }

        // Optional Status Badge on the Right (Rectangular, slightly rounded)
        if (statusText != null) {
            Box(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 9.dp, vertical = 4.dp),
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
            }
        }
    }
}
