package com.umutsaydam.deardiary.data.local.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.umutsaydam.deardiary.domain.entity.FontFamilySealed
import com.umutsaydam.deardiary.domain.entity.FontSizeSealed
import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesManager {

    override suspend fun setReminderEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.REMINDER_ENABLED] = enabled
        }
    }

    override fun isReminderEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.REMINDER_ENABLED] ?: false
        }
    }

    override suspend fun setDefaultFontFamily(fontFamilyId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.FONT_FAMILY] = fontFamilyId
        }
    }

    override fun getDefaultFontFamily(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.FONT_FAMILY] ?: FontFamilySealed.Serif.id
        }
    }

    override suspend fun setDefaultFontSize(fontSizeId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.FONT_SIZE] = fontSizeId
        }
    }

    override fun getDefaultFontSize(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.FONT_SIZE] ?: FontSizeSealed.BodyMedium.id
        }
    }
}

private object PreferencesKey {
    val REMINDER_ENABLED = booleanPreferencesKey("reminder_enable")
    val FONT_FAMILY = intPreferencesKey("font_family_id")
    val FONT_SIZE = intPreferencesKey("font_size_id")
}