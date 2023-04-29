package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.roomMateRepository


class DeleteLikeOrDislikeMatchingInfo : ResultUseCase<MutateFavoriteRequestDto, Boolean>() {
    override suspend fun onExecute(params: MutateFavoriteRequestDto): Boolean {
        roomMateRepository.deleteLikeOrDislikeMatchingInfo(params)
        return true
    }
}