package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.ChangeNickNameDto
import org.appcenter.inudorm.model.User

class ChangeNickName : UseCase<ChangeNickNameDto, Unit>() {

    // At the top level of your kotlin file:
    override suspend fun onExecute(params: ChangeNickNameDto) {
        return userRepository.changeNickName(params)
    }

}