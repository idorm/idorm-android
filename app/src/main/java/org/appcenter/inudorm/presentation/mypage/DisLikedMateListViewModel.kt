package org.appcenter.inudorm.presentation.mypage

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.usecase.DeleteDislikedMatchingInfo
import org.appcenter.inudorm.usecase.GetDisLikedMates

class DisLikedMateListViewModel : MateListViewModel() {
    override val getUseCase = GetDisLikedMates()

    override fun deleteMate(id: Int) {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                DeleteDislikedMatchingInfo().run(id)
            }.getOrNull()
            _userMutationState.emit(
                UserMutationEvent.DeleteDislikedMatchingInfo(id, result != null)
            )
        }
    }

}