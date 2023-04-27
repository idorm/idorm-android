package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.matchingInfoRepository
import org.appcenter.inudorm.model.OnbaordInfo

class CreateOnboard : UseCase<OnbaordInfo, OnbaordInfo> () {

    override suspend fun onExecute(params: OnbaordInfo): OnbaordInfo {
        return matchingInfoRepository.saveMatchingInfo(params)
    }
}