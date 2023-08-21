package org.appcenter.inudorm

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.sdk.common.KakaoSdk
import org.appcenter.inudorm.model.OnboardInfo
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.repository.*
import org.appcenter.inudorm.util.IDormLogger

// 사용되는 파일에서 클래스 밖에 선언하면 싱글톤으로 사용 가능합니다.
private val Context.dataStore by preferencesDataStore(name = "prefs")

class App : Application() {
    companion object {
        var token: String? = null
        var savedUser: User? = null
        var isMatchingPeriod: Boolean = false
        var whenMatchingStarts: String = "null"
        val gson: Gson =
            GsonBuilder()
                .registerTypeAdapter(Post::class.java, Post.NICKNAME_DESERIALIZER)
                .registerTypeAdapter(Comment::class.java, Comment.NICKNAME_DESERIALIZER)
                .create()

        val userRepository = UserRepository()
        val roomMateRepository = RoomMateRepository()
        val matchingInfoRepository = MatchingInfoRepository()
        val communityRepository = CommunityRepository()
        val calendarRepository = CalendarRepository()
        val roomMateTeamRepository = RoomMateTeamRepository()
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakaoAppKey))
    }


}

data class Prefs(val token: String?, val matchingInfo: OnboardInfo)
