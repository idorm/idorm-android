package org.appcenter.inudorm.presentation.mypage.matching

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.presentation.matching.Mutation
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.usecase.DeleteLikeOrDislikeMatchingInfo
import org.appcenter.inudorm.usecase.GetDisLikedMates
import org.appcenter.inudorm.usecase.MutateFavoriteRequestDto
import org.appcenter.inudorm.util.State

class DisLikedMateListViewModel : MateListViewModel() {
    override val getUseCase = GetDisLikedMates()

    override fun deleteMate(id: Int) {
        viewModelScope.launch {
            val params = MutateFavoriteRequestDto(id, false)

            _userMutationState.emit(
                UserMutationEvent.DeleteDislikedMatchingInfo(
                    Mutation(params, DeleteLikeOrDislikeMatchingInfo().run(params))
                )
            )
        }
    }

}