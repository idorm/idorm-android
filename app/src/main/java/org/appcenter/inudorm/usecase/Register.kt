package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.repository.UserRepository
import java.lang.Exception

data class RegisterParams(val email: String, val password: String)

class Register(
    private val userRepository: UserRepository,
) : UseCase<RegisterParams, Boolean>() {
    override suspend fun onExecute(params: RegisterParams): Boolean {
        if (params.email != "none" && params.password != "none")
            return userRepository.register(params)
        else
            throw Exception()
    }

}