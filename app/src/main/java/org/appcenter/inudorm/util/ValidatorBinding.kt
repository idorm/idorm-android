package org.appcenter.inudorm.util

import android.text.TextUtils
import android.util.Log

import android.widget.EditText

import androidx.databinding.BindingAdapter


object ValidatorBinding {
    @JvmStatic
    @BindingAdapter("passwordValidator")
    fun passwordInputValidator(editText: EditText, password: String?) {
        // ignore infinite loops
        val minimumLength = 5
        Log.d("validator", password!!)
        if (TextUtils.isEmpty(password)) {
            editText.error = null
            return
        }
        if (!passwordValidator(editText.text.toString())) {
            editText.error = "Password must be minimum $minimumLength length"
        } else editText.error = null
    }
}