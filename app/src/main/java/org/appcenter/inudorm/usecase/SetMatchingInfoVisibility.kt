package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.roomMateRepository

class SetMatchingInfoVisibility : ResultUseCase<Boolean, Boolean>() {
    override suspend fun onExecute(params: Boolean): Boolean {
        roomMateRepository.setMatchingInfoVisibility(params)
        return true
    }
}