package com.umutsaydam.deardiary.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.umutsaydam.deardiary.domain.manager.TokenManager
import com.umutsaydam.deardiary.util.CryptoUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_datastore")

    override suspend fun saveToken(token: String) {
        val encryptedToken = CryptoUtils.encrypt(token)
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = encryptedToken
        }
    }

    override fun getTokenFlow(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_TOKEN]?.let { CryptoUtils.decrypt(it) }
        }
    }
}

object PreferencesKeys {
    val USER_TOKEN = stringPreferencesKey("user_token")
}