package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.roomMateRepository

data class MutateFavoriteRequestDto(val id: Int, val isLiked: Boolean)

class AddLikedOrDislikedMatchingInfo : ResultUseCase<MutateFavoriteRequestDto, Boolean>() {
    override suspend fun onExecute(params: MutateFavoriteRequestDto): Boolean {
        roomMateRepository.addLikedOrDislikedMatchingInfo(params)
        return true
    }
}