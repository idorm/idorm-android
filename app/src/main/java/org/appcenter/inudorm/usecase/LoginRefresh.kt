package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.User

class LoginRefresh : UseCase<Nothing?, User>() {
    override suspend fun onExecute(params: Nothing?): User {
        return App.userRepository.loginRefresh()
    }

}