package com.stark.varg.domain.model

data class DiagnosticsData(
    val faultCodes: List<String>,
    val warnings: List<Warning>,
)
