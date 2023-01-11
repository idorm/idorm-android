package org.appcenter.inudorm.presentation.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityNotificationSettingBinding
import org.appcenter.inudorm.databinding.ActivityRegisterBinding

class NotificationSettingActivity : AppCompatActivity() {
    private val binding: ActivityNotificationSettingBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_notification_setting)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }
}