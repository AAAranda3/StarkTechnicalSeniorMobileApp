package com.stark.varg.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.stark.varg.domain.model.AppLanguage
import com.stark.varg.domain.model.SavedPowerMode
import com.stark.varg.domain.model.UnitSystem
import com.stark.varg.domain.model.UserPreferences
import com.stark.varg.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "stark_user_preferences")

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : UserPreferencesRepository {

    private object Keys {
        val SELECTED_MODE = stringPreferencesKey("selected_mode")
        val CHARGE_TARGET_PCT = intPreferencesKey("charge_target_pct")
        val UNIT_SYSTEM = stringPreferencesKey("unit_system")
        val LANGUAGE = stringPreferencesKey("app_language")

        fun hpKey(mode: String) = intPreferencesKey("profile_${mode}_hp")
        fun ebKey(mode: String) = intPreferencesKey("profile_${mode}_eb")
        fun regenKey(mode: String) = intPreferencesKey("profile_${mode}_regen")
    }

    override val preferencesFlow: Flow<UserPreferences> = context.userPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedMode = preferences[Keys.SELECTED_MODE] ?: "M3"
            val chargeTarget = preferences[Keys.CHARGE_TARGET_PCT] ?: 80
            val unitSystemName = preferences[Keys.UNIT_SYSTEM] ?: UnitSystem.METRIC.name
            val unitSystem = runCatching { UnitSystem.valueOf(unitSystemName) }.getOrDefault(UnitSystem.METRIC)
            val langName = preferences[Keys.LANGUAGE] ?: AppLanguage.SPANISH.name
            val language = runCatching { AppLanguage.valueOf(langName) }.getOrDefault(AppLanguage.SPANISH)

            val defaults = UserPreferences.defaultModeProfiles()
            val profiles = listOf("M1", "M2", "M3", "M4", "M5").associateWith { mode ->
                val defaultProf = defaults[mode] ?: SavedPowerMode(80, 45, 60)
                SavedPowerMode(
                    maxPowerHp = preferences[Keys.hpKey(mode)] ?: defaultProf.maxPowerHp,
                    engineBrakingPct = preferences[Keys.ebKey(mode)] ?: defaultProf.engineBrakingPct,
                    regenPct = preferences[Keys.regenKey(mode)] ?: defaultProf.regenPct,
                )
            }

            UserPreferences(
                selectedMode = selectedMode,
                modeProfiles = profiles,
                chargeTargetPct = chargeTarget,
                unitSystem = unitSystem,
                language = language,
            )
        }

    override suspend fun saveSelectedMode(mode: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.SELECTED_MODE] = mode
        }
    }

    override suspend fun savePowerModeProfile(mode: String, profile: SavedPowerMode) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.hpKey(mode)] = profile.maxPowerHp
            preferences[Keys.ebKey(mode)] = profile.engineBrakingPct
            preferences[Keys.regenKey(mode)] = profile.regenPct
        }
    }

    override suspend fun saveChargeTargetPct(targetPct: Int) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.CHARGE_TARGET_PCT] = targetPct
        }
    }

    override suspend fun saveUnitSystem(unitSystem: UnitSystem) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.UNIT_SYSTEM] = unitSystem.name
        }
    }

    override suspend fun saveLanguage(language: AppLanguage) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.LANGUAGE] = language.name
        }
    }
}
