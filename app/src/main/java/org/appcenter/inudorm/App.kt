package org.appcenter.inudorm

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.appcenter.inudorm.model.SavedUser
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.repository.LocalFilterRepository
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.repository.RoomMateRepository
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.usecase.Login
import org.appcenter.inudorm.usecase.LoginRefresh
import org.appcenter.inudorm.util.IDormLogger

// 사용되는 파일에서 클래스 밖에 선언하면 싱글톤으로 사용 가능합니다.
private val Context.dataStore by preferencesDataStore(name = "prefs")

class App : Application() {
    companion object {
        var token: String? = null
        var savedUser: User? = null
        val gson = Gson()

        val userRepository = UserRepository()
        val roomMateRepository = RoomMateRepository()
        val localFilterRepository = LocalFilterRepository()
    }


}

data class Prefs(val token: String?)
