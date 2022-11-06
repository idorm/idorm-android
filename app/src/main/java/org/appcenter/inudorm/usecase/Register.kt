package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.networking.Data
import org.appcenter.inudorm.userRepository
import java.lang.Exception

data class RegisterParams(val email: String, val password: String)

class Register : UseCase<RegisterParams, Data<Boolean>>() {
    override suspend fun onExecute(params: RegisterParams): Data<Boolean> {
        if (params.email != "none" && params.password != "none")
            return userRepository.register(params)
        else
            throw Exception()
    }

}