package com.dicoding.wiseorganic.data.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreference(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("user_token")
        val USERNAME_KEY = stringPreferencesKey("username")
        val DEPARTEMENT_ID_KEY = intPreferencesKey("departement_id")
    }

    suspend fun saveUserToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }

    suspend fun saveDepartmentId(departmentId: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEPARTEMENT_ID_KEY] = departmentId
        }
    }

    val userToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY]
        }

    val departmentId: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[DEPARTEMENT_ID_KEY]
        }

    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}