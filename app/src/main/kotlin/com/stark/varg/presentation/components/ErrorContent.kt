package com.stark.varg.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.ui.theme.StarkTypographyTokens
import com.stark.varg.ui.theme.VargPitchBlack
import com.stark.varg.ui.theme.VargStarkRed
import com.stark.varg.ui.theme.VargSurfaceCard
import com.stark.varg.ui.theme.VargTextSecondary
import com.stark.varg.ui.theme.VargWhite

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(VargPitchBlack)
            .padding(24.dp)
            .testTag("error_content"),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VargSurfaceCard)
                .border(1.dp, VargStarkRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(24.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                // Hazard Diagnostics Icon
                VargIconDiagnostics(color = VargStarkRed, sizeDp = 40.dp)

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "CAN BUS SYNCHRONIZATION FAULT",
                    color = VargStarkRed,
                    style = StarkTypographyTokens.TechnicalHeader.copy(
                        color = VargStarkRed,
                        letterSpacing = 1.2.sp,
                    ),
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = message,
                    color = VargTextSecondary,
                    textAlign = TextAlign.Center,
                    style = StarkTypographyTokens.BodyRegular,
                    modifier = Modifier.testTag("error_message"),
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VargStarkRed,
                        contentColor = VargWhite,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("retry_button"),
                ) {
                    Text(
                        text = "RETRY CAN-BUS CONNECTION",
                        style = StarkTypographyTokens.ButtonLabel.copy(
                            color = VargWhite,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                }
            }
        }
    }
}
