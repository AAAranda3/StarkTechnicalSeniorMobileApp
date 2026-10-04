package com.stark.varg.presentation.util

import com.stark.varg.domain.model.AppLanguage

/**
 * Technical string repository supporting dynamic live localization (Spanish & English)
 * for the Stark VARG telemetry interface without requiring activity recreation.
 */
object VargStrings {

    // Bottom Navigation
    fun navCockpit(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Cabina"
        AppLanguage.ENGLISH -> "Cockpit"
    }

    fun navModes(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Modos"
        AppLanguage.ENGLISH -> "Modes"
    }

    fun navBattery(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Batería"
        AppLanguage.ENGLISH -> "Battery"
    }

    fun navSettings(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Ajustes"
        AppLanguage.ENGLISH -> "Settings"
    }

    // Dashboard Hero & Showcase
    fun readyToRide(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "¡Listo para rodar!"
        AppLanguage.ENGLISH -> "Ready to ride!"
    }

    fun riderWelcome(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Bienvenido a tu Stark VARG"
        AppLanguage.ENGLISH -> "Welcome to your Stark VARG"
    }

    fun chargingHeroSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "⚡ CARGANDO • 3.3 kW"
        AppLanguage.ENGLISH -> "⚡ CHARGING • 3.3 kW"
    }

    fun streamLiveBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "CAN EN VIVO"
        AppLanguage.ENGLISH -> "LIVE 4Hz"
    }

    fun streamOffBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "CAN OFF"
        AppLanguage.ENGLISH -> "STREAM OFF"
    }

    fun estimatedRangeLabel(lang: AppLanguage, unit: String): String = when (lang) {
        AppLanguage.SPANISH -> "Autonomía estimada ($unit)"
        AppLanguage.ENGLISH -> "Estimated range ($unit)"
    }

    fun connectedStatus(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Conectado"
        AppLanguage.ENGLISH -> "Connected"
    }

    fun allSystemsOperational(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Sistemas Operativos"
        AppLanguage.ENGLISH -> "All Systems Operational"
    }

    // Dashboard Systems & Cards
    fun servicesHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "SISTEMAS DEL VEHÍCULO"
        AppLanguage.ENGLISH -> "VEHICLE SYSTEMS"
    }

    fun systemsHeader(lang: AppLanguage): String = servicesHeader(lang)

    fun viewAction(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "VER →"
        AppLanguage.ENGLISH -> "VIEW →"
    }

    fun activeAdvisory(lang: AppLanguage, count: Int): String = when (lang) {
        AppLanguage.SPANISH -> if (count == 1) "1 Advertencia Activa" else "$count Advertencias Activas"
        AppLanguage.ENGLISH -> if (count == 1) "1 Active Advisory" else "$count Active Advisories"
    }

    fun servicePowerModes(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Modos de Potencia"
        AppLanguage.ENGLISH -> "Power Modes"
    }

    fun serviceMotor(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Motor y Transmisión"
        AppLanguage.ENGLISH -> "Motor & Powertrain"
    }

    fun serviceTelemetry(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Telemetría de Viaje"
        AppLanguage.ENGLISH -> "Trip & Circuit Telemetry"
    }

    fun serviceDiagnostics(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Diagnóstico del Sistema"
        AppLanguage.ENGLISH -> "System Diagnostics"
    }

    // Screen TopAppBar Titles & Subtitles
    fun powerModesTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "MODOS DE POTENCIA"
        AppLanguage.ENGLISH -> "POWER MODES"
    }

    fun powerModesSubtitle(lang: AppLanguage, map: String): String = when (lang) {
        AppLanguage.SPANISH -> "Mapa Activo: ${map.uppercase()}"
        AppLanguage.ENGLISH -> "Active Map: ${map.uppercase()}"
    }

    fun batteryTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "BATERÍA Y CARGA"
        AppLanguage.ENGLISH -> "BATTERY & CHARGING"
    }

    fun batteryStateCharging(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Estado: carga activa (3.3 kW)"
        AppLanguage.ENGLISH -> "State: active charging (3.3 kW)"
    }

    fun batteryStateDischarging(lang: AppLanguage, stateName: String): String = when (lang) {
        AppLanguage.SPANISH -> "Estado: ${stateName.lowercase()}"
        AppLanguage.ENGLISH -> "State: ${stateName.lowercase()}"
    }

    fun motorTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "MOTOR Y TRANSMISIÓN"
        AppLanguage.ENGLISH -> "MOTOR & POWERTRAIN"
    }

    fun statsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "ESTADÍSTICAS DE SESIÓN"
        AppLanguage.ENGLISH -> "SESSION STATISTICS"
    }

    fun statsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Métricas de viaje registradas"
        AppLanguage.ENGLISH -> "Recorded Trip Metrics"
    }

    fun diagnosticsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "DIAGNÓSTICO DEL SISTEMA"
        AppLanguage.ENGLISH -> "VEHICLE DIAGNOSTICS"
    }

    fun diagnosticsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Registro de avisos y fallos CAN"
        AppLanguage.ENGLISH -> "Advisory Log & Fault Diagnostics"
    }

    // Settings Screen Titles & Headers
    fun settingsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "AJUSTES"
        AppLanguage.ENGLISH -> "SETTINGS"
    }

    fun settingsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "PREFERENCIAS DE LA VARG Y SISTEMA"
        AppLanguage.ENGLISH -> "VARG PREFERENCES & SYSTEM SETUP"
    }

    // Language Section
    fun languageSectionTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "IDIOMA DE LA APLICACIÓN"
        AppLanguage.ENGLISH -> "APPLICATION LANGUAGE"
    }

    fun languageSectionDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Cambio reactivo e instantáneo de interfaz en cabina"
        AppLanguage.ENGLISH -> "Instant reactive cockpit interface language switch"
    }

    // Units Section
    fun unitSectionTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "SISTEMA DE UNIDADES"
        AppLanguage.ENGLISH -> "UNIT SYSTEM"
    }

    fun unitSectionDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Conversión automática de distancias, velocidades y temperaturas"
        AppLanguage.ENGLISH -> "Automatic conversion of distance, speed and temperature"
    }

    fun metricLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Métrico [ KM / °C ]"
        AppLanguage.ENGLISH -> "Metric [ KM / °C ]"
    }

    fun imperialLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Imperial [ MI / °F ]"
        AppLanguage.ENGLISH -> "Imperial [ MI / °F ]"
    }

    // Telemetry Stream Section
    fun liveStreamSectionTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "TELEMETRÍA EN VIVO (CAN-BUS 4Hz)"
        AppLanguage.ENGLISH -> "LIVE TELEMETRY (CAN-BUS 4Hz)"
    }

    fun liveStreamSectionDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Transmisión telemétrica continua en banco de pruebas o circuito"
        AppLanguage.ENGLISH -> "Continuous telemetry broadcast for dyno bench or circuit"
    }

    fun liveStreamActiveBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "ENLACE CAN ACTIVO (4Hz)"
        AppLanguage.ENGLISH -> "CAN LINK ACTIVE (4Hz)"
    }

    fun liveStreamInactiveBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "TRANSMISIÓN EN ESPERA"
        AppLanguage.ENGLISH -> "STREAM PAUSED"
    }

    // Vehicle Specifications Section
    fun vehicleSectionTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "VEHÍCULO Y TELEMETRÍA"
        AppLanguage.ENGLISH -> "VEHICLE & TELEMETRY"
    }

    fun modelLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Modelo"
        AppLanguage.ENGLISH -> "Model"
    }

    fun vinLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Bastidor (VIN)"
        AppLanguage.ENGLISH -> "Chassis (VIN)"
    }

    fun motorLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Tren de Potencia"
        AppLanguage.ENGLISH -> "Powertrain"
    }

    fun firmwareLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Firmware ECU"
        AppLanguage.ENGLISH -> "ECU Firmware"
    }

    fun appVersionLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Versión de la App"
        AppLanguage.ENGLISH -> "App Version"
    }

    // Factory Reset
    fun restoreFactoryTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "MAPAS DE POTENCIA"
        AppLanguage.ENGLISH -> "POWER MAPS"
    }

    fun restoreFactoryButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "RESTABLECER MAPAS A VALORES DE FÁBRICA"
        AppLanguage.ENGLISH -> "RESTORE FACTORY POWER MAPS"
    }

    fun restoreFactoryNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.SPANISH -> "Restaura curvas de fábrica para M1, M2, M3, M4 y M5"
        AppLanguage.ENGLISH -> "Restores factory power curves for M1, M2, M3, M4 and M5"
    }
}
