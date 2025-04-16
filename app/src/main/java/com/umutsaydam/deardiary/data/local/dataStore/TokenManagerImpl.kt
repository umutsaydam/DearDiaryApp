package com.umutsaydam.deardiary.data.local.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.umutsaydam.deardiary.domain.manager.TokenManager
import com.umutsaydam.deardiary.util.CryptoUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TokenManager {

    override suspend fun saveToken(token: String) {
        val encryptedToken = CryptoUtils.encrypt(token)
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = encryptedToken
        }
    }

    override fun getTokenFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_TOKEN]?.let { CryptoUtils.decrypt(it) }
        }
    }
}

private object PreferencesKeys {
    val USER_TOKEN = stringPreferencesKey("user_token")
}