package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.board.Photo

class UpdateProfilePhoto : ResultUseCase<Photo?, Unit>() {
    override suspend fun onExecute(params: Photo?) {
        return userRepository.updateProfilePhoto(params)
    }
}