package org.appcenter.inudorm.presentation.mypage

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.usecase.DeleteLikedMatchingInfo
import org.appcenter.inudorm.usecase.GetLikedMates

data class MateListState(var sortBy: String, var mates: UiState<ArrayList<MatchingInfo>>)

class LikedMateListViewModel : MateListViewModel() {
    override val getUseCase = GetLikedMates()

    override fun deleteMate(id: Int) {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                DeleteLikedMatchingInfo().run(id)
            }.getOrNull()
            _userMutationState.emit(
                UserMutationEvent.DeleteLikedMatchingInfo(id, result != null)
            )
        }
    }
}