package org.appcenter.inudorm.presentation.onboard

import org.appcenter.inudorm.model.OnbaordInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.presentation.matching.LoadMode
import org.appcenter.inudorm.util.ViewModelWithEvent

data class OnboardState(
    var isLoading: Boolean = false,
    var error: Throwable? = null,
    var onboard: OnbaordInfo,
    var loadMode: LoadMode = LoadMode.Prepare,
)

enum class LoadMode {
    Prepare,
    Paging,
    Update
}

sealed class OnboardEvent(open val value: String)

class BaseInformationViewModel : ViewModelWithEvent() {


}