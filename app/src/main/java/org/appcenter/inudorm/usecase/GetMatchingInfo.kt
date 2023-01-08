package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.matchingInfoRepository
import org.appcenter.inudorm.model.MatchingInfo

// Todo: MyInfo보다 다른 이름이 더 의미상 어울릴 것 같아요.
class GetMatchingInfo() : UseCase<Nothing?, MatchingInfo>() {
    override suspend fun onExecute(params: Nothing?): MatchingInfo {
        return matchingInfoRepository.fetchMatchingInfo()
    }
}