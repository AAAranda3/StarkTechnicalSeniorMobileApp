package com.stark.varg.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.stark.varg.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TelemetryScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun happyPath_showsBikeInfo_battery_andActiveWarning() {
        // Real LocalTelemetryDataSource reads from assets - no fake needed
        composeTestRule.waitForIdle()

        // Bike header visible
        composeTestRule.onNodeWithText("STARK VARG", substring = true, ignoreCase = true).assertIsDisplayed()

        // Battery percentage visible
        composeTestRule.onNodeWithText("73%").assertIsDisplayed()

        // Active warning from mock JSON is visible
        composeTestRule.onNodeWithText("Motor temperature elevated", substring = true)
            .assertIsDisplayed()
    }
}
