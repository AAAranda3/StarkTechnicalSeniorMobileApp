package com.stark.varg.data.mapper

import com.stark.varg.data.dto.BatteryDto
import com.stark.varg.data.dto.BikeDto
import com.stark.varg.data.dto.DiagnosticsDto
import com.stark.varg.data.dto.MotorDto
import com.stark.varg.data.dto.RideSettingsDto
import com.stark.varg.data.dto.SessionDto
import com.stark.varg.data.dto.TelemetryDto
import com.stark.varg.data.dto.WarningDto
import com.stark.varg.domain.model.ChargingState
import com.stark.varg.domain.model.WarningSeverity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TelemetryMapperTest {

    // --- Helpers -------------------------------------------------------------

    private fun buildDto(
        chargingState: String = "discharging",
        warnings: List<WarningDto> = listOf(
            WarningDto("W_MOT_TEMP_HIGH", "Motor temperature elevated", "warning"),
        ),
        faultCodes: List<String> = emptyList(),
    ) = TelemetryDto(
        bike = BikeDto("Stark VARG MX 1.2", "Alpha", "3.4.1", "https://img.example.com/bike.webp"),
        timestamp = "2025-05-19T10:32:45Z",
        battery = BatteryDto(73, 38, 34.7, chargingState),
        motor = MotorDto(52.4, 61.2),
        rideSettings = RideSettingsDto("enduro", 80, 45, 60),
        session = SessionDto(3742, 24.7, 94.1),
        diagnostics = DiagnosticsDto(faultCodes, warnings),
    )

    // --- Positive cases -------------------------------------------------------

    @Test
    fun `maps full DTO to domain snapshot correctly`() {
        val snapshot = buildDto().toDomain()

        assertEquals("Stark VARG MX 1.2", snapshot.bike.model)
        assertEquals("Alpha", snapshot.bike.variant)
        assertEquals("3.4.1", snapshot.bike.firmwareVersion)
        assertEquals("https://img.example.com/bike.webp", snapshot.bike.imageUrl)
        assertEquals("2025-05-19T10:32:45Z", snapshot.timestamp)
        assertEquals(73, snapshot.battery.stateOfChargePct)
        assertEquals(38, snapshot.battery.estimatedRangeKm)
        assertEquals(34.7, snapshot.battery.temperatureC, 0.001)
        assertEquals(52.4, snapshot.motor.powerHp, 0.001)
        assertEquals(61.2, snapshot.motor.temperatureC, 0.001)
        assertEquals("enduro", snapshot.rideSettings.powerMap)
        assertEquals(80, snapshot.rideSettings.maxPowerHp)
        assertEquals(45, snapshot.rideSettings.engineBrakingPct)
        assertEquals(60, snapshot.rideSettings.regenPct)
        assertEquals(3742, snapshot.session.durationS)
        assertEquals(24.7, snapshot.session.distanceKm, 0.001)
        assertEquals(94.1, snapshot.session.maxSpeedKmh, 0.001)
    }

    @Test
    fun `maps discharging charging state`() {
        val snapshot = buildDto(chargingState = "discharging").toDomain()
        assertEquals(ChargingState.DISCHARGING, snapshot.battery.chargingState)
    }

    @Test
    fun `maps charging charging state`() {
        val snapshot = buildDto(chargingState = "charging").toDomain()
        assertEquals(ChargingState.CHARGING, snapshot.battery.chargingState)
    }

    @Test
    fun `maps warning severity to WARNING`() {
        val snapshot = buildDto(warnings = listOf(WarningDto("W1", "msg", "warning"))).toDomain()
        assertEquals(WarningSeverity.WARNING, snapshot.diagnostics.warnings.first().severity)
    }

    @Test
    fun `maps warning severity to CRITICAL`() {
        val snapshot = buildDto(warnings = listOf(WarningDto("W1", "msg", "critical"))).toDomain()
        assertEquals(WarningSeverity.CRITICAL, snapshot.diagnostics.warnings.first().severity)
    }

    @Test
    fun `maps warning severity to INFO`() {
        val snapshot = buildDto(warnings = listOf(WarningDto("W1", "msg", "info"))).toDomain()
        assertEquals(WarningSeverity.INFO, snapshot.diagnostics.warnings.first().severity)
    }

    // --- Negative cases -------------------------------------------------------

    @Test
    fun `unknown charging state maps to UNKNOWN`() {
        val snapshot = buildDto(chargingState = "idle").toDomain()
        assertEquals(ChargingState.UNKNOWN, snapshot.battery.chargingState)
    }

    @Test
    fun `unknown warning severity maps to UNKNOWN`() {
        val snapshot = buildDto(warnings = listOf(WarningDto("W1", "msg", "fatal"))).toDomain()
        assertEquals(WarningSeverity.UNKNOWN, snapshot.diagnostics.warnings.first().severity)
    }

    @Test
    fun `empty warnings list maps to empty list`() {
        val snapshot = buildDto(warnings = emptyList()).toDomain()
        assertTrue(snapshot.diagnostics.warnings.isEmpty())
    }

    @Test
    fun `empty fault codes list maps to empty list`() {
        val snapshot = buildDto(faultCodes = emptyList()).toDomain()
        assertTrue(snapshot.diagnostics.faultCodes.isEmpty())
    }

    // --- Corner cases ---------------------------------------------------------

    @Test
    fun `charging state comparison is case-insensitive`() {
        assertEquals(ChargingState.DISCHARGING, buildDto(chargingState = "DISCHARGING").toDomain().battery.chargingState)
        assertEquals(ChargingState.CHARGING, buildDto(chargingState = "Charging").toDomain().battery.chargingState)
    }

    @Test
    fun `multiple warnings preserve order and map severities independently`() {
        val warnings = listOf(
            WarningDto("W1", "First", "warning"),
            WarningDto("W2", "Second", "critical"),
            WarningDto("W3", "Third", "info"),
        )
        val mapped = buildDto(warnings = warnings).toDomain().diagnostics.warnings
        assertEquals(3, mapped.size)
        assertEquals(WarningSeverity.WARNING, mapped[0].severity)
        assertEquals(WarningSeverity.CRITICAL, mapped[1].severity)
        assertEquals(WarningSeverity.INFO, mapped[2].severity)
    }

    @Test
    fun `zero numeric values are mapped without defaulting`() {
        val dto = buildDto().copy(
            battery = BatteryDto(0, 0, 0.0, "discharging"),
            motor = MotorDto(0.0, 0.0),
        )
        val snapshot = dto.toDomain()
        assertEquals(0, snapshot.battery.stateOfChargePct)
        assertEquals(0.0, snapshot.battery.temperatureC, 0.0)
        assertEquals(0.0, snapshot.motor.powerHp, 0.0)
    }

    @Test
    fun `fault codes and warnings can both be non-empty`() {
        val snapshot = buildDto(
            faultCodes = listOf("F_001", "F_002"),
            warnings = listOf(WarningDto("W1", "msg", "warning")),
        ).toDomain()
        assertEquals(listOf("F_001", "F_002"), snapshot.diagnostics.faultCodes)
        assertEquals(1, snapshot.diagnostics.warnings.size)
    }
}
