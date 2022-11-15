package org.appcenter.inudorm.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import org.appcenter.inudorm.Prefs
import org.appcenter.inudorm.model.SavedUser
import org.appcenter.inudorm.repository.PrefsRepository.PreferenceKeys.TOKEN
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "prefs")

class PrefsRepository(private val context: Context) {
    private object PreferenceKeys {
        val TOKEN = stringPreferencesKey("token")
    }

    suspend fun getUserToken(): Flow<String?> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e("Error reading preferences: ", exception.toString())
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        mapUserPrefs(it)
    }

    suspend fun setUserToken(token: String) = context.dataStore.edit { preferences ->
        preferences[TOKEN] = token
    }

    private fun mapPrefs(prefs: Preferences): Prefs {
        val user = mapUserPrefs(prefs)
        return Prefs(user)
    }

    private fun mapUserPrefs(preferences: Preferences): String? {
        return preferences[TOKEN]
    }
}