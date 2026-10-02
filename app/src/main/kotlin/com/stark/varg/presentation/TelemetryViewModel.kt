package com.stark.varg.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stark.varg.domain.usecase.GetTelemetryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelemetryViewModel @Inject constructor(
    private val getTelemetryUseCase: GetTelemetryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TelemetryUiState>(TelemetryUiState.Loading)
    val uiState: StateFlow<TelemetryUiState> = _uiState.asStateFlow()

    init {
        loadTelemetry()
    }

    fun loadTelemetry() {
        viewModelScope.launch {
            _uiState.value = TelemetryUiState.Loading
            getTelemetryUseCase().fold(
                onSuccess = { _uiState.value = TelemetryUiState.Success(it) },
                onFailure = { _uiState.value = TelemetryUiState.Error(it.message ?: "Unknown error") },
            )
        }
    }
}
