package com.stark.varg.domain.model

data class Warning(
    val code: String,
    val message: String,
    val severity: WarningSeverity,
)
