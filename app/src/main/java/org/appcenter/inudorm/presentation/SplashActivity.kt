package org.appcenter.inudorm.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
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
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.usecase.LoginRefresh
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.OkDialog
import kotlin.system.exitProcess

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

    private val appUpdateManager: AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    private fun restartApplication() {
        val packageManager = packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        startActivity(mainIntent)
        exitProcess(0)
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                // 업데이트가 중단됐을 때
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        UPDATE_REQ_CODE
                    )
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQ_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    OkDialog("업데이트가 완료됐습니다. 앱을 재실행합니다.", onOk = {
                        restartApplication()
                    }).show(this)
                }
                RESULT_CANCELED -> {
                    OkDialog("업데이트 후 이용하시기 바랍니다.", onOk = {
                        finish()
                        exitProcess(0)
                    }).show(this)
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    OkDialog("업데이트에 실패했습니다. 앱을 재실행합니다.", onOk = {
                        restartApplication()
                    }).show(this)
                }
            }
        }
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

    private var isLoginSuccess = false
    private var isRemoteConfigReady = false
    private var isUpdateCheckSuccess = false
    private val remoteConfig: FirebaseRemoteConfig by lazy {
        Firebase.remoteConfig
    }

    private fun checkLogin() {
        lifecycleScope.launch {
            loginRefresh().catch { exception ->
                Log.e("Error reading preferences: ", exception.toString())
                isLoginSuccess = true
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
                isLoginSuccess = true
            }
        }
    }

    private fun initFRC(onSuccess: () -> Unit) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.fetch(0)
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_default)
        remoteConfig.activate().addOnCompleteListener {
            isRemoteConfigReady = true
            if (it.isSuccessful) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    remoteConfig.all.forEach { (t, u) ->
                        IDormLogger.d(this@SplashActivity, "$t : $u")
                    }
                }
                App.isMatchingPeriod = remoteConfig.getBoolean("isMatchingPeriod")
                App.whenMatchingStarts = remoteConfig.getString("whenMatchingStarts")
                onSuccess()
            } else if (it.isCanceled) {
                App.isMatchingPeriod = false
                App.whenMatchingStarts = "null"
            }
        }
    }

    private fun checkUpdate(onSuccess: () -> Unit) {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnCompleteListener { result ->
            isUpdateCheckSuccess = true
            if (result.isSuccessful) {
                val appUpdateInfo = result.result
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this@SplashActivity,
                        UPDATE_REQ_CODE
                    )
                } else {
                    onSuccess()
                }
            } else if (result.isCanceled || result.exception != null) {
                if (result.exception !is com.google.android.play.core.appupdate.internal.zzy) {
                    OkDialog("앱 업데이트 체크에 실패했어요. 앱을 종료할게요.", onOk = { exitProcess(1) }).show(this)
                } else {
                    onSuccess()
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        setContentView(R.layout.activity_splash)
        askNotificationPermission()

        //  Todo: 우선 콜백지옥..
        checkUpdate {
            initFRC {
                checkLogin()
            }
        }

        val content = findViewById<View>(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (isLoginSuccess && isRemoteConfigReady && isUpdateCheckSuccess) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            }
        )
        prefsRepository = PrefsRepository(applicationContext)


    }
}