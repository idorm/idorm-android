package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState

class AcceptInvitation : ResultUseCase<Int, Unit>() {
    override suspend fun onExecute(params: Int) {
        return App.roomMateTeamRepository.acceptInvitation(params)
    }
}