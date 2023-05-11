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
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.Gender
import org.appcenter.inudorm.model.JoinPeriod
import org.appcenter.inudorm.model.OnboardInfo
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.account.OnboardActivity
import org.appcenter.inudorm.presentation.onboard.BaseInfoPurpose
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.*

class MyMatchingProfileActivity : LoadingActivity() {
    val binding: ActivityMyMatchingProfileBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_my_matching_profile)
    }
    val viewModel: MyMatchingProfileViewModel by viewModels()

    private val prefsRepository by lazy {
        PrefsRepository(this)
    }

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
                        prefsRepository,
                        it.error
                    ) { e ->
                        when (e.error) {
                            ErrorCode.MATCHINGINFO_NOT_FOUND -> {
                                CustomDialog(
                                    titleText = "매칭 이미지가 아직 없어요. \uD83D\uDE05",
                                    text = "룸메이트 매칭을 위해\n우선 매칭 이미지를 만들어 주세요.",
                                    positiveButton = DialogButton(
                                        "프로필 이미지 만들기",
                                        ButtonType.Filled,
                                        onClick = {
                                            createImage()
                                        }),
                                ).show(this@MyMatchingProfileActivity)
                            }
                            else -> {
                                OkDialog(getString(R.string.unknownError)).show(this@MyMatchingProfileActivity)
                            }
                        }
                    }
                } else if (!it.loading) {
                    val onboardInfo = OnboardInfo(
                        dormCategory = it.data?.dormCategory ?: Dorm.DORM1,
                        gender = it.data?.gender ?: Gender.MALE,
                        joinPeriod = it.data?.joinPeriod ?: JoinPeriod.WEEK16,
                        isSnoring = it.data?.isSnoring ?: false,
                        isGrinding = it.data?.isGrinding ?: false,
                        isSmoking = it.data?.isSmoking ?: false,
                        isAllowedFood = it.data?.isAllowedFood ?: false,
                        isWearEarphones = it.data?.isWearEarphones ?: false,
                        age = it.data?.age ?: 20,
                        wakeupTime = it.data?.wakeUpTime ?: "",
                        cleanUpStatus = it.data?.cleanUpStatus ?: "",
                        showerTime = it.data?.showerTime ?: "",
                        openKakaoLink = it.data?.openKakaoLink ?: "",
                        mbti = it.data?.mbti ?: "",
                        wishText = it.data?.wishText ?: "",
                    )
                    prefsRepository.setMatchingInfo(onboardInfo)
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
        intent.putExtra("purpose", BaseInfoPurpose.Edit)
        startActivity(intent)
    }
}