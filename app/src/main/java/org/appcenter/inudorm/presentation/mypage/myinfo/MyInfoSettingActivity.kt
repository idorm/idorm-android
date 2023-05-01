package org.appcenter.inudorm.presentation.mypage.myinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityMyInformationSettingBinding
import org.appcenter.inudorm.presentation.account.LoginActivity
import org.appcenter.inudorm.presentation.account.WithdrawalActivity
import org.appcenter.inudorm.presentation.mypage.NotificationSettingActivity
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.WindowUtil.setStatusBarColor
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