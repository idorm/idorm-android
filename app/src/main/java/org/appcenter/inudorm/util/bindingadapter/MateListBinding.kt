package org.appcenter.inudorm.util.bindingadapter

import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import org.appcenter.inudorm.presentation.account.prompt.PasswordState
import org.appcenter.inudorm.presentation.mypage.MateListState

object MateListBinding {
    @JvmStatic
    @BindingAdapter(value = ["mateListState", "value"], requireAll = true)
    fun RadioButton.setState(mateListState: MateListState?, field: String?) {
        // Todo: sortBy에 따라 지정
        this.isChecked = mateListState?.sortBy == field
    }
}