package org.appcenter.inudorm.util.bindingadapter

import android.content.res.ColorStateList
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.Log
import android.view.View

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.color

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.*
import org.appcenter.inudorm.R
import org.appcenter.inudorm.presentation.account.prompt.PasswordState
import org.appcenter.inudorm.util.passwordCharacterValidator
import org.appcenter.inudorm.util.passwordValidator

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
    @BindingAdapter(value = ["value", "sizeRange"], requireAll = true)
    fun TextView.lengthValidator(value: String, sizeRange: String) {
        if (sizeRange.matches("[0-9]-[0-9]".toRegex())) {
            val lengths = sizeRange.split("-").map { it.toInt() }

            if (value.length in lengths[0]..lengths[1]) {
                setTextColor(resources.getColor(R.color.iDorm_blue, null))
            } else {
                setTextColor(resources.getColor(R.color.iDorm_red, null))
            }
        }
    }

    @JvmStatic
    @BindingAdapter("noSpaceValidator")
    fun TextView.noSpaceValidator(value: String) {
        if (!value.contains(" ")) setTextColor(resources.getColor(R.color.iDorm_blue, null))
        else setTextColor(resources.getColor(R.color.iDorm_red, null))
    }

    @JvmStatic
    @BindingAdapter("onlyKoEnNumValidator")
    fun TextView.onlyKoEnNumValidator(value: String) {
        if (value.matches("^[A-Za-zㄱ-ㅎ가-힣0-9]*$".toRegex())) setTextColor(
            resources.getColor(
                R.color.iDorm_blue,
                null
            )
        )
        else setTextColor(resources.getColor(R.color.iDorm_red, null))
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

    @JvmStatic
    @BindingAdapter(value = ["textCount", "maxSize"], requireAll = true)
    fun TextView.textCounter(textCount: Int, maxSize: Int) {
        val span = SpannableStringBuilder()
            .color(
                ContextCompat.getColor(
                    this.context,
                    R.color.iDorm_blue
                )
            ) { append(textCount.toString()) }
            .append(" / $maxSize pt")
        text = span

    }



}