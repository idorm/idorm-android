package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.model.Mate
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.repository.RoomMateRepository

// Todo: MyInfo보다 다른 이름이 더 의미상 어울릴 것 같아요.
class GetRoomMates(private val repository: RoomMateRepository) : UseCase<MatchingInfo, ArrayList<Mate>>() {
    override suspend fun onExecute(params: MatchingInfo): ArrayList<Mate> {
        return repository.fetchRoomMates(params)
    }
}