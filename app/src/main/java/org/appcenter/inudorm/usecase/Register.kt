package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.networking.Data
import java.lang.Exception

data class RegisterParams(val email: String, val password: String)

class Register : UseCase<RegisterParams, Data<Boolean>>() {
    override suspend fun onExecute(params: RegisterParams): Data<Boolean> {
        if (params.email != "none" && params.password != "none") {
            val registerResult = userRepository.register(params)
            return if (registerResult.data != null && registerResult.data.email == params.email)
                Data(data = true) else Data(data = false)
        } else
            throw Exception()
    }

}