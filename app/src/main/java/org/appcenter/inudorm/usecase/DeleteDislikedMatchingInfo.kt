package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.roomMateRepository


class DeleteDislikedMatchingInfo : UseCase<Int, Unit>() {
    override suspend fun onExecute(params: Int): Unit {
        return roomMateRepository.deleteDislikedMatchingInfo(params)
    }
}