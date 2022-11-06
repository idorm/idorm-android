package org.appcenter.inudorm.util

import android.content.res.ColorStateList
import android.text.TextUtils
import android.util.Log
import android.view.View

import android.widget.TextView

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.*
import org.appcenter.inudorm.R
import org.appcenter.inudorm.presentation.account.PasswordState

object ValidatorBinding {

    private fun View.isSecondPWFieldFocused(): Boolean {
        val focusedView = rootView.findFocus()
        return focusedView is TextInputEditText
                && focusedView.id == rootView.findViewById<TextInputEditText>(R.id.passwordCheckField).id
    }

    @JvmStatic
    @BindingAdapter("passwordValidator")
    fun TextInputLayout.passwordInputValidator(pwState: PasswordState) {
        val iDormBlueTint = ColorStateList.valueOf(resources.getColor(R.color.iDorm_blue, null))
        val iDormGrayTint = ColorStateList.valueOf(resources.getColor(R.color.iDorm_gray_400, null))
        fun setSuccessIcon() {
            setEndIconTintList(iDormBlueTint)
            setEndIconDrawable(R.drawable.ic_textinput_check_true)
            endIconMode = END_ICON_CUSTOM
        }

        fun setPasswordToggleIcon() {
            setEndIconTintList(iDormGrayTint)
            endIconMode = END_ICON_PASSWORD_TOGGLE
        }

        fun setError() {
            Log.d("validator", pwState.password)
            if (TextUtils.isEmpty(pwState.password)) {
                error = null
                return
            }
            error = if (!passwordValidator(editText?.text.toString())) {
                " "
            } else null
        }

        if (editText?.hasFocus() == false) {
            if (passwordValidator(pwState.password))
                setSuccessIcon()
            else setError()
        } else {
            error = null
            setPasswordToggleIcon()
        }
    }

    @JvmStatic
    @BindingAdapter("passwordLengthValidator")
    fun TextView.passwordLengthValidator(pwState: PasswordState) {
        if (isSecondPWFieldFocused()) {
            if (pwState.password.length in 8..15) {
                setTextColor(resources.getColor(R.color.iDorm_blue, null))
            } else {
                setTextColor(resources.getColor(R.color.iDorm_red, null))
            }
        } else setTextColor(resources.getColor(R.color.iDorm_gray_400, null))
    }

    @JvmStatic
    @BindingAdapter("passwordCharactersValidator")
    fun TextView.passwordValidator(pwState: PasswordState) {
        if (isSecondPWFieldFocused()) {
            if (passwordCharacterValidator(pwState.password)) {
                setTextColor(resources.getColor(R.color.iDorm_blue, null))
            } else {
                setTextColor(resources.getColor(R.color.iDorm_red, null))
            }
        } else setTextColor(resources.getColor(R.color.iDorm_gray_400, null))
    }

}