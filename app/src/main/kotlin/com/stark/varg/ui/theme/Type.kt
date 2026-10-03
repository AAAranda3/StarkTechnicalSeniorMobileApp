package com.stark.varg.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Stark Future Brand Typography System
 * 
 * Centralized typography design tokens:
 * - Numbers / Metrics / Gauges: "Stark Sans ExtraLight" (FontWeight.ExtraLight, negative letter spacing)
 * - Text / Headers / Subtitles: "Universal-sans" (Clean geometric sans, letter-spaced uppercase technical headers)
 * Fallback font stack: system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, sans-serif
 */
val StarkSansFont = FontFamily.SansSerif

object StarkTypographyTokens {
    // Primary Big Metrics
    val MetricHero = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 44.sp,
        lineHeight = 48.sp,
        letterSpacing = (-1).sp,
        color = StarkWhite,
    )

    val MetricLarge = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 38.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.5).sp,
        color = StarkWhite,
    )

    val MetricMedium = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.5).sp,
        color = StarkWhite,
    )

    val MetricSmall = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.25).sp,
        color = StarkWhite,
    )

    val MetricMini = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.2).sp,
        color = StarkWhite,
    )

    // Unit labels paired with metrics
    val MetricUnit = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.sp,
        color = StarkWhite,
    )

    // Headers (Universal-sans uppercase)
    val TechnicalHeader = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 1.3.sp,
        color = StarkTextSecondary,
    )

    val TechnicalLabel = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.8.sp,
        color = StarkTextSecondary,
    )

    // Modal Titles
    val ScreenTitle = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Light,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
        color = StarkTextPrimary,
    )

    val ModalTitle = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.8.sp,
        color = StarkWhite,
    )

    val CardTitle = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.2.sp,
        color = StarkWhite,
    )

    val CardSubtitle = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp,
        color = StarkTextSecondary,
    )

    // Body
    val BodyRegular = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp,
        color = StarkTextPrimary,
    )

    val BodySmall = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp,
        color = StarkTextSecondary,
    )

    val ValueMedium = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.2.sp,
        color = StarkWhite,
    )

    // Navigation, Pills, Buttons & Scale Ticks
    val PillLabel = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.6.sp,
        color = StarkTextPrimary,
    )

    val ButtonLabel = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.8.sp,
        color = StarkTextPrimary,
    )

    val ScaleTick = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
        color = StarkTextSecondary,
    )

    val BrandTitle = TextStyle(
        fontFamily = StarkSansFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 2.sp,
        color = StarkTextPrimary,
    )
}

// Aliases for direct imports
val StarkNumberDisplayLarge = StarkTypographyTokens.MetricLarge
val StarkNumberDisplayMedium = StarkTypographyTokens.MetricMedium
val StarkNumberDisplaySmall = StarkTypographyTokens.MetricSmall

val StarkTypography = Typography(
    displayLarge = StarkTypographyTokens.MetricHero,
    displayMedium = StarkTypographyTokens.MetricLarge,
    displaySmall = StarkTypographyTokens.MetricMedium,
    headlineLarge = StarkTypographyTokens.MetricMedium,
    headlineMedium = StarkTypographyTokens.MetricSmall,
    headlineSmall = StarkTypographyTokens.CardTitle,
    titleLarge = StarkTypographyTokens.ScreenTitle,
    titleMedium = StarkTypographyTokens.CardTitle,
    titleSmall = StarkTypographyTokens.TechnicalHeader,
    bodyLarge = StarkTypographyTokens.BodyRegular,
    bodyMedium = StarkTypographyTokens.CardSubtitle,
    bodySmall = StarkTypographyTokens.ScaleTick,
    labelLarge = StarkTypographyTokens.TechnicalHeader,
    labelMedium = StarkTypographyTokens.PillLabel,
    labelSmall = StarkTypographyTokens.ScaleTick,
)

