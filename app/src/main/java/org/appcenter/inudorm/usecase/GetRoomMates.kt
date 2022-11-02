package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.model.Mate
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.repository.RoomMateRepository
import org.appcenter.inudorm.roomMateRepository

// Todo: MyInfo보다 다른 이름이 더 의미상 어울릴 것 같아요.
class GetRoomMates() : UseCase<RoomMateFilter, ArrayList<Mate>>() {
    override suspend fun onExecute(params: RoomMateFilter): ArrayList<Mate> {
        return roomMateRepository.fetchRoomMates(params)
    }
}