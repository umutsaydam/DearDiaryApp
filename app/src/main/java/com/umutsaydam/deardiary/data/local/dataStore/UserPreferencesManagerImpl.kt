package com.umutsaydam.deardiary.data.local.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
}

private object PreferencesKey {
    val REMINDER_ENABLED = booleanPreferencesKey("reminder_enable")
}