package org.appcenter.inudorm.presentation.component

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import org.appcenter.inudorm.R
import org.appcenter.inudorm.util.bindingadapter.BoardBinding.bindTimeElapsed
import org.appcenter.inudorm.util.bindingadapter.BoardBinding.getElapsedTime


class BoardProfile(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var initialized = false

    var nickname: String? = null
        set(value) {
            if (initialized)
                findViewById<TextView>(R.id.nickname).text = value
            field = value
        }
    var timeElapsed: String? = null
        set(value) {
            if (initialized && !timeElapsed.isNullOrEmpty())
                findViewById<TextView>(R.id.timeElapsed).text = getElapsedTime(timeElapsed!!)
            field = value
        }
    var profileUrl: String? = null
        set(value) {
            field = value
            if (initialized && value != null)
                loadImage(findViewById(R.id.profileImage), value)
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BoardProfile,
            0, 0
        ).apply {
            try {
                nickname = getString(R.styleable.BoardProfile_nickname) ?: ""
                timeElapsed = getString(R.styleable.BoardProfile_timeElapsed) ?: ""
                profileUrl = getString(R.styleable.BoardProfile_profileUrl) ?: ""
            } finally {
                recycle()
            }
        }
        inflate(context, R.layout.item_board_profile, this)
        val nicknameView = findViewById<TextView>(R.id.nickname)
        val timeElapsedView = findViewById<TextView>(R.id.timeElapsed)
        nicknameView.text = nickname
        if (!timeElapsed.isNullOrEmpty())
            timeElapsedView.text = getElapsedTime(timeElapsed!!)
        initialized = true
    }

    fun loadImage(imageView: ImageView, url: String) {
        Glide.with(this.context)
            .load(url)
            .into(imageView)

    }

}