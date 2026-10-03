package com.stark.varg.domain.util

import com.stark.varg.domain.model.UnitSystem
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitConverterTest {

    @Test
    fun `formatDistance returns exact km for metric and converted miles for imperial`() {
        val (metricVal, metricUnit) = UnitConverter.formatDistance(24.7, UnitSystem.METRIC)
        assertEquals("24.7", metricVal)
        assertEquals("km", metricUnit)

        val (impVal, impUnit) = UnitConverter.formatDistance(24.7, UnitSystem.IMPERIAL)
        assertEquals("15.3", impVal)
        assertEquals("mi", impUnit)
    }

    @Test
    fun `formatRange returns exact integer km for metric and rounded miles for imperial`() {
        val (metricVal, metricUnit) = UnitConverter.formatRange(73, UnitSystem.METRIC)
        assertEquals("73", metricVal)
        assertEquals("km", metricUnit)

        val (impVal, impUnit) = UnitConverter.formatRange(73, UnitSystem.IMPERIAL)
        assertEquals("45", impVal)
        assertEquals("mi", impUnit)
    }

    @Test
    fun `formatSpeed returns exact kmh for metric and converted mph for imperial`() {
        val (metricVal, metricUnit) = UnitConverter.formatSpeed(94.1, UnitSystem.METRIC)
        assertEquals("94.1", metricVal)
        assertEquals("km/h", metricUnit)

        val (impVal, impUnit) = UnitConverter.formatSpeed(94.1, UnitSystem.IMPERIAL)
        assertEquals("58.5", impVal)
        assertEquals("mph", impUnit)
    }

    @Test
    fun `formatTemperature returns exact Celsius for metric and Fahrenheit for imperial`() {
        val (metricVal, metricUnit) = UnitConverter.formatTemperature(34.7, UnitSystem.METRIC)
        assertEquals("34.7", metricVal)
        assertEquals("°C", metricUnit)

        val (impVal, impUnit) = UnitConverter.formatTemperature(34.7, UnitSystem.IMPERIAL)
        assertEquals("94.5", impVal)
        assertEquals("°F", impUnit)
    }
}
