package org.appcenter.inudorm.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.appcenter.inudorm.App
import org.appcenter.inudorm.Prefs
import org.appcenter.inudorm.model.OnboardInfo
import org.appcenter.inudorm.networking.IDormError
import org.appcenter.inudorm.repository.PrefsRepository.PreferenceKeys.MATCHING_INFO
import org.appcenter.inudorm.repository.PrefsRepository.PreferenceKeys.TOKEN
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "prefs")

class PrefsRepository(private val context: Context) {
    private object PreferenceKeys {
        val TOKEN = stringPreferencesKey("token")
        val MATCHING_INFO = stringPreferencesKey("matchingInfo")
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

    suspend fun getMatchingInfo(): Flow<OnboardInfo?> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e("Error reading preferences: ", exception.toString())
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        mapMatchingInfo(it)
    }

    suspend fun setMatchingInfo(matchingInfo: OnboardInfo) = context.dataStore.edit { prefs ->
        prefs[MATCHING_INFO] = App.gson.toJson(matchingInfo)
    }

    suspend fun setUserToken(token: String?) = context.dataStore.edit { preferences ->
        preferences[TOKEN] = token ?: ""
    }

    suspend fun signOut() {
        try {
            App.userRepository.signOut()
        } catch (_: IDormError) {
            // 무시
        }
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            App.token = null
        }

    }

    private fun mapPrefs(prefs: Preferences): Prefs {
        val user = mapUserPrefs(prefs)
        val matchingInfo = mapMatchingInfo(prefs)
        return Prefs(user, matchingInfo!!)
    }

    private fun mapUserPrefs(preferences: Preferences): String? {
        return preferences[TOKEN]
    }

    private fun mapMatchingInfo(preferences: Preferences): OnboardInfo? {
        return App.gson.fromJson(preferences[MATCHING_INFO], OnboardInfo::class.java)
    }
}