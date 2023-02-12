package org.appcenter.inudorm.presentation.component

import android.content.Context
import android.content.res.TypedArray
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
import org.appcenter.inudorm.util.IDormLogger


class MatchingCard(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var mDormNum: Dorm = Dorm.DORM1
    private var mJoinPeriod: JoinPeriod = JoinPeriod.WEEK16
    private var mIsSnoring: Boolean = false
    private var mIsGrinding: Boolean = false
    private var mIsSmoking: Boolean = false
    private var mIsAllowedFood: Boolean = false
    private var mIsWearEarphones: Boolean = false
    private var mWakeUpTime: String = ""
    private var mCleanUpStatus: String = ""
    private var mShowerTime: String = ""
    private var mMbti: String = ""
    private var mWishText: String = ""
    private var mGender: Gender = Gender.MALE
    private var mAge: Int = 20
    lateinit var binding: ItemMatchingBinding

    inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T): T {
        IDormLogger.i(this@MatchingCard, "$index, $default ${T::class.simpleName}")
        return getInt(index, -1).let {
            if (it >= 0) enumValues<T>()[it] else default
        }
    }

    init {
        _initAttrs(context, attrs)
        _initView()
    }

    fun _initAttrs(context: Context, attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MatchingCard,
            0, 0
        )
        val errText = "정보가 없는 것 같아요."
        try {
            IDormLogger.i(this, a.getInt(R.styleable.MatchingCard_dormNum, -1).toString())
        } finally {
            a.recycle()
        }
    }

    fun _initView() {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.item_matching,
            this, true
        )
       binding.mate = MatchingInfo(
            dormNum = mDormNum,
            joinPeriod = mJoinPeriod,
            gender = mGender,
            age = mAge,
            isSnoring = mIsSnoring,
            isGrinding = mIsGrinding,
            isSmoking = mIsSmoking,
            isAllowedFood = mIsAllowedFood,
            isWearEarphones = mIsWearEarphones,
            wishText = mWishText,
            mbti = mMbti,
            wakeUpTime = mWakeUpTime,
            showerTime = mShowerTime,
            cleanUpStatus = mCleanUpStatus,
            isMatchingInfoPublic = true,
            matchingInfoId = 9999,
            memberId = 9999,
            openKakaoLink = ""
        )
    }

}