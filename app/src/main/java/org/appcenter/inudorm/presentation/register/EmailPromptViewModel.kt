package org.appcenter.inudorm.presentation.register

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.ViewModelWithEvent


class EmailPromptViewModel : ViewModelWithEvent() {
    val email = MutableLiveData("")

    fun showNotValidDialog() {
        val okButton = DialogButton("확인", Color.CYAN) {
            showToast("Toast Message")
        }
        showDialog("이메일이 올바르지 않습니다.", okButton , null)
    }

    fun submit() {
        this.mergeBundleWithPaging("email", email.value!!)
    }
}

