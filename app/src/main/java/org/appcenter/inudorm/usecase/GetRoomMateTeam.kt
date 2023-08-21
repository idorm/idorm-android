package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.RoomMateTeamResponseDto


class GetRoomMateTeam : ResultUseCase<Nothing?, RoomMateTeamResponseDto>() {
    override suspend fun onExecute(params: Nothing?): RoomMateTeamResponseDto {
        return App.roomMateTeamRepository.getMyRoomMates()
    }
}