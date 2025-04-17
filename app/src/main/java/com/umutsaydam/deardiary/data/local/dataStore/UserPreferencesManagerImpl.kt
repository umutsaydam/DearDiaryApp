package com.umutsaydam.deardiary.data.local.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.umutsaydam.deardiary.domain.entity.FontFamilySealed
import com.umutsaydam.deardiary.domain.entity.FontSizeSealed
import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import com.umutsaydam.deardiary.util.CryptoUtils
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

    override suspend fun setPin(pin: String) {
        val encryptedPin = CryptoUtils.encrypt(pin)
        dataStore.edit { preferences ->
            preferences[PreferencesKey.PIN] = encryptedPin
        }
    }

    override fun getPin(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.PIN]?.let {
                CryptoUtils.decrypt(it)
            } ?: ""
        }
    }

    override fun isFingerPrintEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.FINGER_PRINT_ENABLED] ?: false
        }
    }

    override suspend fun setFingerPrintEnable(enable: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.FINGER_PRINT_ENABLED] = enable
        }
    }
}

private object PreferencesKey {
    val REMINDER_ENABLED = booleanPreferencesKey("reminder_enable")
    val FONT_FAMILY = intPreferencesKey("font_family_id")
    val FONT_SIZE = intPreferencesKey("font_size_id")
    val PIN = stringPreferencesKey("pin")
    val FINGER_PRINT_ENABLED = booleanPreferencesKey("finger_print_enabled")
}