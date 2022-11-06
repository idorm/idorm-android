package org.appcenter.inudorm.util.bindingadapter

import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import org.appcenter.inudorm.model.Dorm

object EmailFieldBinding {
    @JvmStatic
    @BindingAdapter("email")
    fun TextInputEditText.setEmail(email: String?) {
        Log.i("EmailFieldBinding", "$email")
        val prevEmail = text
        if (email != null) {
            if (email != text.toString()) {
                setText(email)
            }
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "email", event = "app:emailAttrChanged")
    fun TextInputEditText.getEmail() : String?{
        Log.i("TAG", "Email Changed: $text")
        if (text?.length!! > 2 && text!![text?.length!! - 1] == '@') {
            Log.i("TAG", "Email AutoCompleted: ${text}inu.ac.kr")
            setText("${text}inu.ac.kr")
            setSelection(text?.length!! - 10)
        }
        return text.toString()
    }

    @JvmStatic
    @BindingAdapter(value = ["app:emailAttrChanged"], requireAll = false)
    fun TextInputEditText.setOnEmailChanged(attrChanged: InverseBindingListener?) {
        addTextChangedListener {
            attrChanged?.onChange()
        }
    }
}