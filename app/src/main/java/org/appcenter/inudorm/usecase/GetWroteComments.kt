package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState

class GetWroteComments : UseCase<Nothing?, UiState<ArrayList<Comment>>>() {
    override suspend fun onExecute(params: Nothing?): UiState<ArrayList<Comment>> {
        return kotlin.runCatching {
            UiState(loading = false, data = App.communityRepository.getWroteComments())
        }.getOrElse { UiState(loading = false, error = it) }
    }
}