package com.stark.varg

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.stark.varg.presentation.TelemetryScreen
import com.stark.varg.presentation.screens.SplashScreen
import com.stark.varg.ui.theme.StarkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        // Seamless exit animation: smoothly fade out the corporate system splash
        // into the cinematic Compose splash without any abrupt visual jumps
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenViewProvider.view,
                View.ALPHA,
                1f,
                0f,
            ).apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 380L
                doOnEnd { splashScreenViewProvider.remove() }
            }
            fadeOut.start()
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
