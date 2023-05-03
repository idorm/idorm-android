package org.appcenter.inudorm.presentation.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.OnboardInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.presentation.matching.BaseInfoMutationEvent
import org.appcenter.inudorm.presentation.matching.LoadMode
import org.appcenter.inudorm.presentation.matching.MatchingState
import org.appcenter.inudorm.presentation.matching.Mutation
import org.appcenter.inudorm.usecase.UserOnboard
import org.appcenter.inudorm.usecase.OnboardParams
import org.appcenter.inudorm.util.OkDialog
import org.appcenter.inudorm.util.State

data class BaseInfoState(
    var isLoading: Boolean = false,
    var error: Throwable? = null,
    var loadMode: LoadMode = LoadMode.Prepare,
)

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

    private val _baseInfoState: MutableStateFlow<BaseInfoState> = MutableStateFlow(
        BaseInfoState()
    )
    val baseInfoState: StateFlow<BaseInfoState>
        get() = _baseInfoState

    fun submit(onboardInfo : OnboardInfo){
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