package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository

class DeleteProfilePhoto : ResultUseCase<Nothing?, Unit>() {
    override suspend fun onExecute(params: Nothing?) {
        return userRepository.deleteProfilePhoto()
    }
}