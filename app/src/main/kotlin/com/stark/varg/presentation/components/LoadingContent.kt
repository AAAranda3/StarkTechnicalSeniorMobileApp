package com.stark.varg.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.stark.varg.ui.theme.StarkBackground
import com.stark.varg.ui.theme.StarkRed

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(StarkBackground)
            .testTag("loading_indicator"),
    ) {
        CircularProgressIndicator(
            color = StarkRed,
            strokeWidth = 3.dp,
            modifier = Modifier.size(48.dp),
        )
    }
}
