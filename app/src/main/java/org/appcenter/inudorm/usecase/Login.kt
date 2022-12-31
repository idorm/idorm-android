package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.IDormLogger

abstract class LoginParams {
    abstract val email: String
}

data class UserInputParams(override val email: String, val password: String) : LoginParams()
class StoredUser(override val email: String, val token: String) : LoginParams()

object LoginResponseCode {
    const val SUCCESS = "SUCCESS"
}

class Login(private val prefsRepository: PrefsRepository) : UseCase<UserInputParams, Boolean>() {

    // At the top level of your kotlin file:
    override suspend fun onExecute(params: UserInputParams): Boolean {
        return loginWithInput(params)
    }

    private suspend fun loginWithInput(params: UserInputParams) : Boolean {
        val user = userRepository.login(params)
        val token = user.loginToken
        // Todo: 토큰 저장
        IDormLogger.i(this, "token: $token")
        return if (token != null) {
            prefsRepository.setUserToken(token)
            App.token = token
            true
        } else {
            false
        }
    }
}

class LoginRefresh : UseCase<Nothing?, User>() {
    override suspend fun onExecute(params: Nothing?): User {
        return userRepository.loginRefresh()
    }

}