package org.appcenter.inudorm.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemBoardProfileBinding

data class BoardProfileData(val nickname: String, val createdAt: String, val profileUrl: String)

class BoardProfile(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private lateinit var mNickName: String
    private lateinit var mCreatedAt: String
    private lateinit var mProfileUrl: String
    lateinit var binding: ItemBoardProfileBinding

    init {
        _initAttrs(context, attrs)
        _initView()
    }

    private fun _initAttrs(context: Context, attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BoardProfile,
            0, 0
        )
        val errText = "정보가 없는 것 같아요."
        try {
            mNickName = a.getString(R.styleable.BoardProfile_profileNickname) ?: ""
            mProfileUrl = a.getString(R.styleable.BoardProfile_profileUrl) ?: ""
            mCreatedAt = a.getString(R.styleable.BoardProfile_timeElapsed) ?: ""
        } finally {
            a.recycle()
        }
    }

    fun _initView() {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.item_board_profile,
            this, true
        )
        binding.profile = BoardProfileData(
            mNickName,
            mCreatedAt,
            mProfileUrl
        )
    }

}