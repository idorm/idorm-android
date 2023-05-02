package org.appcenter.inudorm.presentation.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.OnboardInfo
import org.appcenter.inudorm.presentation.matching.BaseInfoMutationEvent
import org.appcenter.inudorm.presentation.matching.Mutation
import org.appcenter.inudorm.usecase.UserOnboard
import org.appcenter.inudorm.usecase.OnboardParams
import org.appcenter.inudorm.util.OkDialog
import org.appcenter.inudorm.util.State



enum class LoadMode {
    Prepare,
    Paging,
    Update
}

data class Mutation<P, R>(val request: P, val state: State<R>)

class BaseInformationViewModel(private val purpose: BaseInfoPurpose) : ViewModel() {
    private val _baseInfoMutationEvent: MutableStateFlow<BaseInfoMutationEvent?> = MutableStateFlow(null)
    val baseInfoMutationEvent: StateFlow<BaseInfoMutationEvent?>
        get() = _baseInfoMutationEvent

    fun submit(onboardInfo : OnboardInfo){
        //Todo: 필수 항목 체크
        val onboardParams = OnboardParams(purpose, onboardInfo)
        viewModelScope.launch {
            _baseInfoMutationEvent.emit(BaseInfoMutationEvent.CreateBaseInfo(
                Mutation(onboardParams, UserOnboard().run(OnboardParams(purpose, onboardInfo)))
            ))
        }
    }
}

class BaseInfoViewModelFactory(private val purpose: BaseInfoPurpose) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseInformationViewModel::class.java)) {
            return BaseInformationViewModel(purpose) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}