package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.roomMateRepository


class DeleteLikeOrDislikeMatchingInfo : UseCase<MutateFavoriteRequestDto, Unit>() {
    override suspend fun onExecute(params: MutateFavoriteRequestDto): Unit {
        return roomMateRepository.deleteLikeOrDislikeMatchingInfo(params)
    }
}