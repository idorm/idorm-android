package org.appcenter.inudorm.presentation.mypage

import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.BuildConfig
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityMyInformationSettingBinding

class MyInfoSettingActivity : AppCompatActivity() {

    private val viewModel: MyInfoSettingViewModel by viewModels()
    private val binding: ActivityMyInformationSettingBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_my_information_setting)
    }

    fun withdraw() {
        Toast.makeText(this, "탈퇴클릭", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.activity = this
        binding.lifecycleOwner = this
        viewModel.getUser()
    }
}