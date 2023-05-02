package org.appcenter.inudorm.presentation.mypage.matching

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityMyMatchingProfileBinding
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.account.OnboardActivity
import org.appcenter.inudorm.presentation.onboard.BaseInfoPurpose
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.OkDialog

class MyMatchingProfileActivity : LoadingActivity() {
    val binding: ActivityMyMatchingProfileBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_my_matching_profile)
    }
    val viewModel: MyMatchingProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getMatchingInfo()

        lifecycleScope.launch {
            viewModel.myPageState.collect {
                setLoadingState(it.loading)
                if (!it.loading && it.error != null) {
                    UIErrorHandler.handle(
                        this@MyMatchingProfileActivity,
                        PrefsRepository(this@MyMatchingProfileActivity),
                        it.error,
                    ) { e ->
                        when (e.error) {
                            ErrorCode.MATCHINGINFO_NOT_FOUND -> {
                                OkDialog(
                                    e.error.message,
                                    onOk = { createImage() },
                                    cancelable = false
                                ).show(this@MyMatchingProfileActivity)
                            }
                            else -> {
                                OkDialog(getString(R.string.unknownError)).show(this@MyMatchingProfileActivity)
                            }
                        }
                    }
                }
            }
        }

    }


    fun createImage() {
        val intent = Intent(this, OnboardActivity::class.java)
        intent.putExtra("purpose", BaseInfoPurpose.Create)
        startActivity(intent)
        finish()
    }

    fun editImage() {
        val intent = Intent(this, OnboardActivity::class.java)
        intent.putExtra("purpose", BaseInfoPurpose.Create)
        startActivity(intent)
    }
}