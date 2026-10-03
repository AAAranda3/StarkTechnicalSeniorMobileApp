package com.stark.varg.domain.usecase

import com.stark.varg.domain.model.TelemetrySnapshot
import com.stark.varg.domain.repository.TelemetryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLiveTelemetryUseCase @Inject constructor(
    private val repository: TelemetryRepository,
) {
    operator fun invoke(): Flow<TelemetrySnapshot> {
        return repository.observeLiveTelemetryStream()
    }
}
