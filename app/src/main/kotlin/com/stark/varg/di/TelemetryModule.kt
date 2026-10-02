package com.stark.varg.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.stark.varg.data.repository.TelemetryRepositoryImpl
import com.stark.varg.data.source.LocalTelemetryDataSource
import com.stark.varg.data.source.TelemetryDataSource
import com.stark.varg.domain.repository.TelemetryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TelemetryModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideTelemetryDataSource(
        @ApplicationContext context: Context,
        moshi: Moshi,
    ): TelemetryDataSource = LocalTelemetryDataSource(context, moshi)

    @Provides
    @Singleton
    fun provideTelemetryRepository(
        dataSource: TelemetryDataSource,
    ): TelemetryRepository = TelemetryRepositoryImpl(dataSource)
}
