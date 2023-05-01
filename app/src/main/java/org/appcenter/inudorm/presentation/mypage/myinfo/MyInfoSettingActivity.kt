package org.appcenter.inudorm.presentation.mypage.myinfo

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityMyInformationSettingBinding
import org.appcenter.inudorm.model.board.Photo
import org.appcenter.inudorm.presentation.account.LoginActivity
import org.appcenter.inudorm.presentation.account.WithdrawalActivity
import org.appcenter.inudorm.presentation.mypage.NotificationSettingActivity
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.ImageUri
import org.appcenter.inudorm.util.WindowUtil.setStatusBarColor
import java.io.File
import kotlin.reflect.KClass


class MyInfoSettingActivity : LoadingActivity() {

    private val viewModel: MyInfoSettingViewModel by viewModels()
    private val binding: ActivityMyInformationSettingBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_my_information_setting)
    }
    private val prefsRepository: PrefsRepository by lazy {
        PrefsRepository(this)
    }


    private inline fun <reified T : AppCompatActivity> navigateTo(activity: KClass<T>) {
        val intent = Intent(this, activity.java)
        startActivity(intent)
    }

    fun changeNickName() {
        navigateTo(ChangeNickNameActivity::class)
    }

    fun changePassword() {
        navigateTo(ChangePasswordActivity::class)
    }

    fun settingNotification() {
        var intent: Intent? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //TODO [OS 가 오레오 이상인 경우]
                intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //TODO [OS 가 롤리팝 이상인 경우]
                intent = Intent("android.settings.APP_NOTIFICATION_SETTINGS")
                intent.putExtra("app_package", packageName)
                intent.putExtra("app_uid", applicationInfo.uid)
            } else {
                intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:$packageName")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            overridePendingTransition(0, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private val launcher = registerImagePicker { images ->
        // Selected images are ready to use
        try {
            if (images.isNotEmpty()) {
                val image = images[0]
                val photo = Photo(
                    image,
                    File(
                        ImageUri.getFullPathFromUri(this@MyInfoSettingActivity, image.uri)
                            ?: throw RuntimeException()
                    ),
                    photoUrl = image.uri.toString()
                )
                viewModel.updateProfilePhoto(photo)
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            Toast.makeText(this@MyInfoSettingActivity, "이미지를 불러오지 못헀습니다.", Toast.LENGTH_SHORT)
                .show()

        }
    }

    fun selectProfileImage() {
        // Todo: 수정 모드에서 이미 선택된 사진  불러오기
        val config = ImagePickerConfig(
            statusBarColor = "#ffffff",
            isLightStatusBar = true,
            toolbarColor = "#ffffff",
            toolbarTextColor = "#000000",
            toolbarIconColor = "#000000",
            imageTitle = "모든 사진",
            backgroundColor = "#ffffff",
            isMultipleMode = false,
            doneTitle = "완료",
            maxSize = 10,
            isShowNumberIndicator = true
        )

        launcher.launch(config)
    }

    fun terms() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://idorm.notion.site/e5a42262cf6b4665b99bce865f08319b")
        )
        startActivity(intent)
    }

    fun withdraw() {
        navigateTo(WithdrawalActivity::class)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(ContextCompat.getColor(this, R.color.white))
    }

    fun signOut() {
        lifecycleScope.launch {
            prefsRepository.setUserToken("")
            App.token = ""
        }.invokeOnCompletion {
            navigateTo(LoginActivity::class)
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.viewModel = viewModel
        binding.activity = this
        binding.lifecycleOwner = this
        viewModel.getUser()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            android.R.id.home -> { // 메뉴 버튼, 사실상 백버튼으로 취급하면 됩니다!
                this.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}