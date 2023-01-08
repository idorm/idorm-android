package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository
import java.lang.Exception

data class RegisterParams(val email: String,val password: String, val nickname: String, )

class Register : UseCase<RegisterParams, Boolean>() {
    override suspend fun onExecute(params: RegisterParams): Boolean {
        if (params.email != "none" && params.password != "none") {
            val registerResult = userRepository.register(params)
            return registerResult.email == params.email
        } else
            throw Exception()
    }

}