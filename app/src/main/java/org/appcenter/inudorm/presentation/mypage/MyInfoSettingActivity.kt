package org.appcenter.inudorm.presentation.mypage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityMyInformationSettingBinding
import org.appcenter.inudorm.presentation.account.LoginActivity
import org.appcenter.inudorm.presentation.account.WithdrawalActivity
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.WindowUtil.setStatusBarColor
import kotlin.reflect.KClass

class MyInfoSettingActivity : AppCompatActivity() {

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
        navigateTo(NotificationSettingActivity::class)
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
        setStatusBarColor(ContextCompat.getColor(this, R.color.iDorm_blue).toString())
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
        binding.viewModel = viewModel
        binding.activity = this
        binding.lifecycleOwner = this
        viewModel.getUser()
    }


}