package com.stark.varg.data.repository

import com.stark.varg.data.mapper.toDomain
import com.stark.varg.data.source.TelemetryDataSource
import com.stark.varg.domain.model.TelemetrySnapshot
import com.stark.varg.domain.repository.TelemetryRepository
import javax.inject.Inject

class TelemetryRepositoryImpl @Inject constructor(
    private val dataSource: TelemetryDataSource,
) : TelemetryRepository {

    override suspend fun getTelemetrySnapshot(): Result<TelemetrySnapshot> = runCatching {
        dataSource.fetchSnapshot().toDomain()
    }
}
