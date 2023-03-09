package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.mypage.matching.Sortable
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState

class GetLikedPosts : UseCase<Nothing?, Sortable<UiState<ArrayList<Post>>>>() {
    override suspend fun onExecute(params: Nothing?): Sortable<UiState<ArrayList<Post>>> {
        return kotlin.runCatching {
            Sortable(
                "addedAtDesc",
                UiState(loading = false, data = App.communityRepository.getLikedPosts())
            )
        }.getOrElse { Sortable("addedAtDesc", UiState(loading = false, error = it)) }
    }
}