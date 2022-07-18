package org.appcenter.inudorm.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.usecase.Login

class SplashActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val userRepository = UserRepository()
        lifecycleScope.launch {
            // Try to login
            kotlin.runCatching {
                Login(dataStore).run(null)
            }.onSuccess { isSuccess ->
                if (isSuccess) {
                    // 기기에 있는 정보로 로그인 성공. MainActivity로 보냅니다.
                    Handler().postDelayed({
                        //H** val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 3000)
                } else {
                    // 기기에 정보가 없거나 로그인에 실패. LoginActivity로 보냅니다.
                    Handler().postDelayed({
                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 3000)
                }
            }.onFailure {
                // 기기에 정보가 없거나 로그인에 실패. LoginActivity로 보냅니다.
                Handler().postDelayed({
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 3000)
            }
        }
    }
}