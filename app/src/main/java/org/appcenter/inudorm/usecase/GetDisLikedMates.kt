package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.roomMateRepository
import org.appcenter.inudorm.model.MatchingInfo

class GetDisLikedMates() : UseCase<Nothing?, ArrayList<MatchingInfo>>() {
    override suspend fun onExecute(params: Nothing?): ArrayList<MatchingInfo> {
        return roomMateRepository.getDisLikedMatchingMates()
    }
}