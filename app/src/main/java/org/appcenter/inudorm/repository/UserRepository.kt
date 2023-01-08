package org.appcenter.inudorm.repository

import org.appcenter.inudorm.App.Companion.gson
import org.appcenter.inudorm.model.EmailVerifyResponseDto
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.usecase.CodeVerifyParams
import org.appcenter.inudorm.usecase.RegisterParams
import org.appcenter.inudorm.usecase.SendAuthCodeParams
import org.appcenter.inudorm.usecase.UserInputParams

class UserRepository {
    suspend fun login(params: UserInputParams?): User {
        val str = gson.toJson(params)
        return RetrofitInstance.service.login(createJsonRequestBody(str))
    }


    suspend fun loginRefresh() : User {
        return RetrofitInstance.service.loginRefresh()
    }

    suspend fun sendRegisterAuthCode(params: SendAuthCodeParams) {
        val str = gson.toJson(params)
        return RetrofitInstance.service.sendRegisterEmail(createJsonRequestBody(str))
    }

    suspend fun sendPasswordAuthCode(params: SendAuthCodeParams) {
        val str = gson.toJson(params)
        return RetrofitInstance.service.sendForgotPWEmail(createJsonRequestBody(str))
    }

    suspend fun verifyRegisterCode(params: CodeVerifyParams): EmailVerifyResponseDto {
        val str = gson.toJson(params)
        return RetrofitInstance.service.verifyRegisterCode(params.email, createJsonRequestBody(str))
    }

    suspend fun verifyPasswordAuthCode(params: CodeVerifyParams): EmailVerifyResponseDto {
        val str = gson.toJson(params)
        return RetrofitInstance.service.verifyForgotPWEmail(params.email, createJsonRequestBody(str))
    }

    suspend fun register(params: RegisterParams): User {
        val str = gson.toJson(params)
        return RetrofitInstance.service.register(createJsonRequestBody(str))
    }
}