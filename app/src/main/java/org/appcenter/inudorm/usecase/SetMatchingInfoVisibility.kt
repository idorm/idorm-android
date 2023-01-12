package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.roomMateRepository
import org.appcenter.inudorm.model.SetMatchingInfoVisibilityDto


class SetMatchingInfoVisibility : UseCase<Boolean, Unit>() {
    override suspend fun onExecute(params: Boolean): Unit {
        return roomMateRepository.setMatchingInfoVisibility(params)
    }
}