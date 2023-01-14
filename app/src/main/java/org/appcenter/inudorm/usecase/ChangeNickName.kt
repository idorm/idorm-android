package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.ChangeNickNameDto
import org.appcenter.inudorm.model.ChangePasswordDto
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.IDormLogger

class ChangeNickName : UseCase<ChangeNickNameDto, User>() {

    // At the top level of your kotlin file:
    override suspend fun onExecute(params: ChangeNickNameDto): User {
        return userRepository.changeNickName(params)
    }

}