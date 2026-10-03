package com.stark.varg.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.domain.model.BikeInfo
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargBorderSubtle
import com.stark.varg.ui.theme.VargElectricGreen
import com.stark.varg.ui.theme.VargPillDark
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargTextPrimary
import com.stark.varg.ui.theme.VargTextSecondary

/**
 * Enterprise Vehicle Status Header.
 */
@Composable
fun BikeHeaderCard(
    bike: BikeInfo,
    modifier: Modifier = Modifier,
    isLandscape: Boolean = false,
) {
    StarkCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bike_header_card"),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = bike.model,
                            color = VargTextPrimary,
                            style = StarkTypographyTokens.ScreenTitle,
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Variant: ${bike.variant}  •  Firmware: v${bike.firmwareVersion}",
                        color = VargTextSecondary,
                        style = StarkTypographyTokens.CardSubtitle,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    StarkPill(
                        text = bike.variant.uppercase(),
                        backgroundColor = VargStarkRed.copy(alpha = 0.15f),
                        borderColor = VargStarkRed.copy(alpha = 0.4f),
                        textColor = VargStarkRed,
                    )
                    Row(
                        modifier = Modifier
                            .background(VargPillDark, CircleShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(VargElectricGreen, CircleShape),
                        )
                        Spacer(Modifier.size(5.dp))
                        Text(
                            text = "ONLINE",
                            color = VargElectricGreen,
                            style = StarkTypographyTokens.TechnicalLabel.copy(color = VargElectricGreen),
                        )
                    }
                }
            }
        }
    }
}
