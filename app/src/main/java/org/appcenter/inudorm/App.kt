package org.appcenter.inudorm

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.repository.*

// 사용되는 파일에서 클래스 밖에 선언하면 싱글톤으로 사용 가능합니다.
private val Context.dataStore by preferencesDataStore(name = "prefs")

@HiltAndroidApp
class App : Application() {

    companion object {
        var token: String? = null
        var savedUser: User? = null
        val gson = Gson()

        val userRepository = UserRepository()
        val roomMateRepository = RoomMateRepository()
        val localFilterRepository = LocalFilterRepository()
        val matchingInfoRepository = MatchingInfoRepository()
    }


}

data class Prefs(val token: String?)
