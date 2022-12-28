package org.appcenter.inudorm.util.bindingadapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View

import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.appcenter.inudorm.util.addZeroToMakeLength


object CodePromptBinding {
    // Todo: 위험요소. 코드 프롬포트를 두번 이상 열었을 때 기존 값이 남아 있을 가능성 존재.
    var textBeforeChanged : String? = null

    @JvmStatic
    @BindingAdapter("timerBehavior")
    fun TextInputLayout.setTimerBehavior(seconds: Int?) {
        Log.d("binding", seconds.toString())
        isEnabled = seconds!! > 0
        val min = addZeroToMakeLength(seconds!! / 60, 2)
        val sec = addZeroToMakeLength(seconds % 60, 2)
        suffixText = "$min:$sec"
    }

    @JvmStatic
    @BindingAdapter("timerBehavior")
    fun TextView.setTimerBehavior(seconds: Int?) {
        Log.d("binding", seconds.toString())
        isEnabled = seconds!! <= 0
    }

    @JvmStatic
    @BindingAdapter("code")
    fun TextInputEditText.setCode(code: String?) {
        Log.i("CodePromptBinding", "$code")
        val prevCode = text
        if (code != null && code != prevCode.toString() ) {
            setText(code)
        }

    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "code", event = "app:authCodeAttrChanged")
    fun TextInputEditText.getCode() : String?{
        Log.i("TAG", "AuthCode Changed: $text")
        if ((textBeforeChanged ?: "").length < text?.length!!) {
            if (text?.length!! == 3) {
                Log.i("TAG", "AuthCode AutoCompleted: ${text}-")
                setText("${text}-")
                setSelection(text?.length!!)
            } else if(text?.length!! > 3 && text!![3] != '-') {
                val newText = text?.insert(3, "-")
                text = newText
                return newText.toString()
            }
        }
        return text.toString()
    }

    @JvmStatic
    @BindingAdapter(value = ["app:authCodeAttrChanged"], requireAll = false)
    fun TextInputEditText.setOnCodeChanged(attrChanged: InverseBindingListener?) {
        addTextChangedListener(object:TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textBeforeChanged = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                attrChanged?.onChange()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }
}