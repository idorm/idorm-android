package org.appcenter.inudorm.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemMatchingBinding
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.Gender
import org.appcenter.inudorm.model.JoinPeriod
import org.appcenter.inudorm.model.MatchingInfo

class MatchingCard(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    var binding: ItemMatchingBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.item_matching,
            this, true
        )
        binding.mate = MatchingInfo(
            dormNum = Dorm.DORM1,
            joinPeriod = JoinPeriod.WEEK16,
            gender = Gender.MALE,
            age = 20,
            isSnoring = false,
            isGrinding = false,
            isSmoking = false,
            isAllowedFood = false,
            isWearEarphones = false,
            wishText = "mWishText",
            mbti = "mMbti",
            wakeUpTime = "mWakeUpTime",
            showerTime = "mShowerTime",
            cleanUpStatus = "mCleanUpStatus",
            isMatchingInfoPublic = true,
            matchingInfoId = 9999,
            memberId = 9999,
            openKakaoLink = "openKakaoLink"
        )
    }


}