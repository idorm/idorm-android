package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository
import java.lang.Exception

class Withdraw : UseCase<Nothing?, Boolean>() {
    override suspend fun onExecute(params: Nothing?): Boolean {
        return userRepository.withdraw()
    }
}