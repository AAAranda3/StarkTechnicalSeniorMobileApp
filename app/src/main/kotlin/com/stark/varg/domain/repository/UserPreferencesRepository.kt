package com.stark.varg.domain.repository

import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.SavedPowerMode
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val preferencesFlow: Flow<UserPreferences>
    suspend fun saveSelectedMode(mode: String)
    suspend fun savePowerModeProfile(mode: String, profile: SavedPowerMode)
    suspend fun saveChargeTargetPct(targetPct: Int)
    suspend fun saveUnitSystem(unitSystem: UnitSystem)
    suspend fun saveLanguage(language: AppLanguage)
}
