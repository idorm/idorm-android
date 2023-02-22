package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState

class GetWrotePosts : UseCase<Nothing?, UiState<ArrayList<Post>>>() {
    override suspend fun onExecute(params: Nothing?): UiState<ArrayList<Post>> {
        return kotlin.runCatching {
            UiState(loading = false, data = App.communityRepository.getWrotePosts())
        }.getOrElse { UiState(loading = false, error = it) }
    }
}