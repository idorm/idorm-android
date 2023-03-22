package org.appcenter.inudorm.presentation.mypage.matching

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState
import org.appcenter.inudorm.usecase.DeleteLikeOrDislikeMatchingInfo
import org.appcenter.inudorm.usecase.GetLikedMates
import org.appcenter.inudorm.usecase.MutateFavoriteRequestDto

class LikedMateListViewModel : MateListViewModel() {
    override val getUseCase = GetLikedMates()

    override fun deleteMate(id: Int) {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                DeleteLikeOrDislikeMatchingInfo().run(MutateFavoriteRequestDto(id, true))
            }.getOrNull()
            _userMutationState.emit(
                UserMutationEvent.DeleteLikedMatchingInfo(id, result != null)
            )
        }
    }
}