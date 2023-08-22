package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App


class DeleteMate : ResultUseCase<Int, Unit>() {
    override suspend fun onExecute(params: Int) {
        return App.roomMateTeamRepository.kickRoomMateMember(params)
    }
}

