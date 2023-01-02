package org.appcenter.inudorm.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App
import org.appcenter.inudorm.R
import org.appcenter.inudorm.presentation.account.LoginActivity
import org.appcenter.inudorm.presentation.account.onError
import org.appcenter.inudorm.presentation.account.onExpectedError
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.usecase.LoginRefresh
import org.appcenter.inudorm.util.IDormLogger

class SplashActivity : AppCompatActivity() {

    private lateinit var prefsRepository: PrefsRepository

    private fun loginRefresh() = flow {
        IDormLogger.d(this, "로그인 리프레시 진입")

        // 토큰을 가져오면서 block!
        App.token = prefsRepository.getUserToken().first()
        IDormLogger.d(this, App.token ?: "토큰 없어 떼잉")
        if (App.token != null) {
            // 토큰을 가져와 앱 상태에 저장된 후에 실행.
            kotlin.runCatching {
                LoginRefresh().run(null)
            }.onSuccess {
                App.savedUser = it
                emit(true)
            }.onError {
                emit(false)
            }.onExpectedError {
                emit(false)
            }

            /*
                val loginRefreshResult = LoginRefresh().run(null)
                if (loginRefreshResult.loginToken != null) {
                    emit(true)
                } else {
                    emit(false)
                }
*/
        } else {
            emit(false)
        }
    }

    private fun goLogin() {
        Handler(Looper.getMainLooper()).postDelayed({
            //H** val intent = Intent(this@SplashActivity, MainActivity::class.java)
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }

    private fun goHome() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        prefsRepository = PrefsRepository(applicationContext)

        lifecycleScope.launch {
            loginRefresh().catch { exception ->
                Log.e("Error reading preferences: ", exception.toString())
                emit(false)
            }.collect { isSuccess ->
                IDormLogger.i(this, "성공여부: $isSuccess")
                if (isSuccess) {
                    IDormLogger.i(this, "${App.savedUser} loaded!!!")
                    if (App.savedUser != null) {
                        goHome()
                    } else {
                        goLogin()
                    }
                } else {
                    goLogin()
                }
            }
        }
    }
}