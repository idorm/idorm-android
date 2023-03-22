package org.appcenter.inudorm.presentation.mypage.matching

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.usecase.DeleteLikeOrDislikeMatchingInfo
import org.appcenter.inudorm.usecase.GetDisLikedMates
import org.appcenter.inudorm.usecase.MutateFavoriteRequestDto

class DisLikedMateListViewModel : MateListViewModel() {
    override val getUseCase = GetDisLikedMates()

    override fun deleteMate(id: Int) {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                DeleteLikeOrDislikeMatchingInfo().run(MutateFavoriteRequestDto(id, false))
            }.getOrNull()
            _userMutationState.emit(
                UserMutationEvent.DeleteDislikedMatchingInfo(id, result != null)
            )
        }
    }

}