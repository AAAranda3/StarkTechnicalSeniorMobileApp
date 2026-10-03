package com.stark.varg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.stark.varg.presentation.TelemetryScreen
import com.stark.varg.presentation.screens.SplashScreen
import com.stark.varg.ui.theme.StarkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        // Immediately dismiss the system window splash so our Compose splash screen takes over smoothly
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            splashScreenViewProvider.remove()
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarkTheme {
                var showSplash by rememberSaveable { mutableStateOf(true) }

                Crossfade(
                    targetState = showSplash,
                    animationSpec = tween(durationMillis = 600),
                    label = "SplashCrossfade",
                ) { isSplashActive ->
                    if (isSplashActive) {
                        SplashScreen(
                            onSplashFinished = { showSplash = false },
                        )
                    } else {
                        TelemetryScreen()
                    }
                }
            }
        }
    }
}
