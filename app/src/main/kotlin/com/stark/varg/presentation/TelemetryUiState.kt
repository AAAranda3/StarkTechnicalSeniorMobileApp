package com.stark.varg.presentation

import com.stark.varg.domain.model.TelemetrySnapshot

sealed interface TelemetryUiState {
    data object Loading : TelemetryUiState
    data class Success(val snapshot: TelemetrySnapshot) : TelemetryUiState
    data class Error(val message: String) : TelemetryUiState
}
