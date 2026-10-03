package com.stark.varg.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stark.varg.ui.theme.StarkBackground
import com.stark.varg.ui.theme.StarkRed
import com.stark.varg.ui.theme.StarkTextPrimary
import com.stark.varg.ui.theme.StarkWhite

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
            .background(StarkBackground)
            .padding(32.dp)
            .testTag("error_content"),
    ) {
        StarkCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = StarkRed.copy(alpha = 0.5f),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "CONNECTION ERROR",
                    color = StarkRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = message,
                    color = StarkTextPrimary,
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    modifier = Modifier.testTag("error_message"),
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StarkRed,
                        contentColor = StarkWhite,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("retry_button"),
                ) {
                    Text(
                        text = "RETRY CONNECTION",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                    )
                }
            }
        }
    }
}
