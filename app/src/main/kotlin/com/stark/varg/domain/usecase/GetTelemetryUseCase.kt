package com.stark.varg.domain.usecase

import com.stark.varg.domain.model.TelemetrySnapshot
import com.stark.varg.domain.repository.TelemetryRepository
import javax.inject.Inject

class GetTelemetryUseCase @Inject constructor(
    private val repository: TelemetryRepository,
) {
    suspend operator fun invoke(): Result<TelemetrySnapshot> {
        return repository.getTelemetrySnapshot()
    }
}
