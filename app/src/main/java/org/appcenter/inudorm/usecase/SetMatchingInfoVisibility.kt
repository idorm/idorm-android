package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.roomMateRepository

class SetMatchingInfoVisibility : UseCase<Boolean, Unit>() {
    override suspend fun onExecute(params: Boolean) {
        return roomMateRepository.setMatchingInfoVisibility(params)
    }
}