package org.appcenter.inudorm.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.appcenter.inudorm.App
import org.appcenter.inudorm.R
import org.appcenter.inudorm.presentation.account.LoginActivity
import org.appcenter.inudorm.presentation.account.onError
import org.appcenter.inudorm.presentation.account.onExpectedError
import org.appcenter.inudorm.presentation.board.PostDetailActivity
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState
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

        val tokenTask = FirebaseMessaging.getInstance().token
        tokenTask.await()
        if (!tokenTask.isSuccessful || tokenTask.result == null) {
            emit(false)
        }
        val fcmToken = tokenTask.result
        if (App.token != null) {
            // 토큰을 가져와 앱 상태에 저장된 후에 실행.
            kotlin.runCatching {
                val refreshResult = LoginRefresh().run(null)
                App.userRepository.updateFcmToken(fcmToken)
                refreshResult
            }.onSuccess {
                App.savedUser = it
                emit(true)
            }.onError {
                emit(false)
            }.onExpectedError {
                emit(false)
            }
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

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
            Toast.makeText(this, "아이돔의 유용한 알림들을 받지 않아요. 언제든 다시 활성활 할 수 있어요.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    private fun handleFallback() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        return
    }

    private fun moveWithMainStack(intent: Intent) {
        startActivity(Intent(this, MainActivity::class.java))
        startActivity(intent)
        finish()
        return
    }

    private fun handleLink() {
        val uri = intent.data
        if (uri == null) { // 올바르지 않은 Uri Fallback
            handleFallback()
            return
        }
        val path = uri.getQueryParameter("path")
        val contentId = uri.getQueryParameter("contentId")
        if (path.isNullOrEmpty() || contentId.isNullOrEmpty()) {
            handleFallback()
            return
        }

        IDormLogger.d(this, "$path |  $contentId")
        when (path) {
            "post" -> {
                val intent = Intent(this, PostDetailActivity::class.java)
                intent.putExtra("id", contentId.toIntOrNull())
                moveWithMainStack(intent)
            }
        }

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
                        handleLink()
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