package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.roomMateRepository

data class MutateFavoriteRequestDto(val id: Int, val isLiked: Boolean)

class AddLikedOrDislikedMatchingInfo : UseCase<MutateFavoriteRequestDto, Unit>() {
    override suspend fun onExecute(params: MutateFavoriteRequestDto): Unit {
        return roomMateRepository.addLikedOrDislikedMatchingInfo(params)
    }
}