package org.appcenter.inudorm

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.SavedUser
import org.appcenter.inudorm.repository.LocalFilterRepository
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.repository.RoomMateRepository
import org.appcenter.inudorm.repository.UserRepository

// 사용되는 파일에서 클래스 밖에 선언하면 싱글톤으로 사용 가능합니다.
private val Context.dataStore by preferencesDataStore(name = "prefs")

val gson = Gson()

val userRepository = UserRepository()
val roomMateRepository = RoomMateRepository()
val localFilterRepository = LocalFilterRepository()

class App : Application() {
    companion object {
        var prefs: Prefs = Prefs(null)
    }

    override fun onCreate() {
        MainScope().launch {
            kotlin.runCatching {
                PrefsRepository(applicationContext).setUser(
                    SavedUser(
                        "ckm0728wash@gmail.com",
                        "sdfasdfdsf",
                        "최경민"
                    )
                )
            }.onSuccess {
                Log.d("[prefs]", "prefs write completed")
            }
            kotlin.runCatching {
                Log.d("[prefs]", "starting to get prefs")
                PrefsRepository(applicationContext).getPrefs().first()
            }.onSuccess {
                prefs = it
                Log.d("[prefs]", "getting prefs finished")
                Log.d("[prefs]", prefs.toString())
                super.onCreate()
            }.onFailure {
                Log.d("[prefs]", it.message!!)
            }
        }
    }
}

data class Prefs(val savedUser: SavedUser?)
