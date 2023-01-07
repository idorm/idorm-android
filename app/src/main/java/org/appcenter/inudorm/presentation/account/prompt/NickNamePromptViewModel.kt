package org.appcenter.inudorm.presentation.account.prompt

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.update
import org.appcenter.inudorm.util.passwordValidator

class NickNamePromptViewModel : ViewModel() {
    val nickName = MutableLiveData("")
}