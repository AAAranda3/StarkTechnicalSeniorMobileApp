package com.stark.varg.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.stark.varg.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * End-to-End Instrumented UI Test verifying full navigation through all application screens,
 * reactive configuration adjustments (Power Modes, Battery Targets, Charging toggle,
 * Language, Unit System, CAN Live Stream), and their visual reflection across the UI.
 */
@HiltAndroidTest
class FullNavigationAndConfigurationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        // Wait for splash transition to complete and Dashboard to load telemetry
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithText("73%")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    @Test
    fun navigateThroughAllScreens_changeConfigurations_andVerifyReflectedResults() {
        // --- 1. VERIFY INITIAL DASHBOARD STATE ---
        composeTestRule.onNodeWithText("73%").assertIsDisplayed()
        composeTestRule.onNodeWithText("RUEDA DONDE SEA, CUANDO SEA", ignoreCase = true).assertIsDisplayed()

        // --- 2. POWER MODES SCREEN: Select Mode M1 and verify reflection ---
        // Navigate via Bottom Bar
        composeTestRule.onNodeWithText("MODOS", ignoreCase = true).performClick()
        composeTestRule.waitForIdle()

        // Assert Power Modes Screen TopBar
        composeTestRule.onNodeWithText("MODOS DE POTENCIA", ignoreCase = true).assertIsDisplayed()

        // Select Mode M1 (calibrated to 35 CV)
        composeTestRule.onNodeWithText("M1").performClick()
        composeTestRule.waitForIdle()

        // Verify M1 profile header is updated uniquely
        composeTestRule.onNodeWithText("M1 TRAIL").assertIsDisplayed()

        // Navigate back to Dashboard using top app bar back button
        composeTestRule.onNodeWithContentDescription("Navigate back").performClick()
        composeTestRule.waitForIdle()

        // In Dashboard, verify that the active power mode card reflects "M1 Trail"
        composeTestRule.onNodeWithText("M1 Trail", substring = true).performScrollTo().assertIsDisplayed()

        // --- 3. MOTOR & POWERTRAIN SCREEN: Verify reflection of selected mode ---
        composeTestRule.onNodeWithText("Motor y Transmisión", ignoreCase = true).performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Assert Motor Screen TopBar and mode reflection
        composeTestRule.onNodeWithText("MOTOR Y TRANSMISIÓN", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Modo M1", substring = true).assertIsDisplayed()

        // Navigate back to Dashboard
        composeTestRule.onNodeWithContentDescription("Navigate back").performClick()
        composeTestRule.waitForIdle()

        // --- 4. SESSION STATISTICS SCREEN: Navigate and inspect ---
        composeTestRule.onNodeWithText("Telemetría de Viaje", ignoreCase = true).performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Assert Stats Screen TopBar & Metrics
        composeTestRule.onNodeWithText("ESTADÍSTICAS DE SESIÓN", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("DISTANCIA TOTAL", ignoreCase = true).assertIsDisplayed()

        // Navigate back to Dashboard
        composeTestRule.onNodeWithContentDescription("Navigate back").performClick()
        composeTestRule.waitForIdle()

        // --- 5. SYSTEM DIAGNOSTICS SCREEN: Navigate and inspect active fault advisory ---
        composeTestRule.onNodeWithText("Diagnóstico del Sistema", ignoreCase = true).performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Assert Diagnostics Screen TopBar & Active warning from mock JSON
        composeTestRule.onNodeWithText("DIAGNÓSTICO DEL SISTEMA", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Motor temperature elevated", substring = true).assertIsDisplayed()

        // Navigate back to Dashboard
        composeTestRule.onNodeWithContentDescription("Navigate back").performClick()
        composeTestRule.waitForIdle()

        // --- 6. BATTERY SCREEN: Change charge target & toggle charger ---
        composeTestRule.onNodeWithText("BATERÍA", ignoreCase = true).performClick()
        composeTestRule.waitForIdle()

        // Assert Battery Screen TopBar
        composeTestRule.onNodeWithText("BATERÍA Y CARGA", ignoreCase = true).assertIsDisplayed()

        // Change target charge limit to 90%
        composeTestRule.onNodeWithText("90%").performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Verify that target estimate updates to 90%
        composeTestRule.onNodeWithText("TIME TO 90%", substring = true).performScrollTo().assertIsDisplayed()

        // Toggle charging process to Active (3.3 kW)
        composeTestRule.onNodeWithText("CONNECT CHARGER (3.3 kW)", substring = true).performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Verify charging state reflects Active
        composeTestRule.onNodeWithText("DISCONNECT & STOP CHARGING", substring = true).performScrollTo().assertIsDisplayed()

        // Disconnect charger to return to normal state
        composeTestRule.onNodeWithText("DISCONNECT & STOP CHARGING", substring = true).performClick()
        composeTestRule.waitForIdle()

        // --- 7. SETTINGS SCREEN: Change Language, Unit System and Live CAN Stream ---
        composeTestRule.onNodeWithText("AJUSTES", ignoreCase = true).performClick()
        composeTestRule.waitForIdle()

        // Assert Settings Screen Header / Subtitle
        composeTestRule.onNodeWithText("PREFERENCIAS DE LA VARG Y SISTEMA", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("IDIOMA DE LA APLICACIÓN", ignoreCase = true).assertIsDisplayed()

        // Switch Language to English
        composeTestRule.onNodeWithText("English").performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Verify that the UI instantly reflects English
        composeTestRule.onNodeWithText("VARG PREFERENCES & SYSTEM SETUP", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("APPLICATION LANGUAGE", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("UNIT SYSTEM", ignoreCase = true).assertIsDisplayed()

        // Switch Unit System to Imperial [ MI / °F ]
        composeTestRule.onNodeWithText("Imperial [ MI / °F ]").performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Toggle Live CAN Stream
        composeTestRule.onNodeWithText("START").performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Verify Live CAN Stream status badge is active
        composeTestRule.onNodeWithText("CAN LINK ACTIVE (4Hz)", ignoreCase = true).assertIsDisplayed()

        // --- 8. RETURN TO COCKPIT / DASHBOARD: Verify language and unit reflections ---
        composeTestRule.onNodeWithText("COCKPIT", ignoreCase = true).performClick()
        composeTestRule.waitForIdle()

        // Verify that Dashboard displays English slogan
        composeTestRule.onNodeWithText("RIDE ANYWHERE, ANYTIME", ignoreCase = true).assertIsDisplayed()

        // Verify that Live Stream badge shows LIVE in Cockpit
        composeTestRule.onNodeWithText("LIVE").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("telemetry stream active", substring = true).assertIsDisplayed()
    }
}
