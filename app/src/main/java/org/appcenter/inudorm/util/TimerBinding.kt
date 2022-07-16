package org.appcenter.inudorm.util

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button

import android.widget.EditText
import android.widget.TextView

import androidx.databinding.BindingAdapter


object TimerBinding {
    @JvmStatic
    @BindingAdapter("timerBehavior")
    fun setTimerBehavior(view: View, seconds: Int?) {
        Log.d("binding", seconds.toString())
        view.isEnabled = when (view) {
            is EditText -> {
                seconds!! > 0 // 만료됐느냐?
            }
            is TextView -> {
                seconds!! <= 0
            }
            else -> {
                true
            }
        }
    }
    @JvmStatic
    @BindingAdapter("seconds")
    fun setTimer(view: TextView, seconds: Int?) {
        val min = addZeroToMakeLength(seconds!! / 60, 2)
        val sec = addZeroToMakeLength(seconds % 60, 2)
        view.text = "$min:$sec"
    }
}