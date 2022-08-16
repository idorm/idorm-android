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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.appcenter.inudorm.Prefs
import org.appcenter.inudorm.model.SavedUser
import org.appcenter.inudorm.repository.PrefsRepository.PreferenceKeys.USER
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "prefs")

class PrefsRepository(private val context: Context) {
    private object PreferenceKeys {
        val USER = stringPreferencesKey("user")
    }

    suspend fun getUser(): Flow<SavedUser?> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e("Error reading preferences: ", exception.toString())
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        mapUserPrefs(prefs)
    }

    suspend fun getPrefs(): Flow<Prefs> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e("Error reading preferences: ", exception.toString())
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        mapPrefs(preferences)
    }

    suspend fun setUser(user: SavedUser) = context.dataStore.edit { preferences ->
        val gson = Gson()
        preferences[USER] = gson.toJson(user)
    }

    private fun mapPrefs(prefs: Preferences): Prefs {
        val user = mapUserPrefs(prefs)
        return Prefs(user)
    }

    private fun mapUserPrefs(preferences: Preferences): SavedUser? {
        val userJson: String = preferences[USER] ?: EMPTY_USER
        val gson = Gson()
        return gson.fromJson(userJson, SavedUser::class.java)
    }

    companion object {
        const val EMPTY_USER = ""
    }
}