package org.appcenter.inudorm.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.usecase.DeleteDislikedMatchingInfo
import org.appcenter.inudorm.usecase.GetDisLikedMates
import org.appcenter.inudorm.usecase.ReportMatchingInfo
import org.appcenter.inudorm.usecase.UseCase
import org.appcenter.inudorm.util.IDormLogger

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