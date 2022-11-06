package org.appcenter.inudorm.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import org.appcenter.inudorm.gson
import org.appcenter.inudorm.networking.Data
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.usecase.CodeVerifyParams
import org.appcenter.inudorm.usecase.RegisterParams
import org.appcenter.inudorm.usecase.SendAuthCodeParams
import org.appcenter.inudorm.usecase.UserInputParams

class UserRepository {
    suspend fun login(params: UserInputParams?): Data<Boolean> {
        val str = gson.toJson(params)
        return RetrofitInstance.service.login(createJsonRequestBody(str))
    }

    suspend fun sendRegisterAuthCode(params: SendAuthCodeParams): Data<Boolean> {
        val str = gson.toJson(params)
        return RetrofitInstance.service.sendRegisterEmail(createJsonRequestBody(str))
    }

    suspend fun sendPasswordAuthCode(params: SendAuthCodeParams): Data<Boolean> {
        val str = gson.toJson(params)
        return RetrofitInstance.service.sendForgotPWEmail(createJsonRequestBody(str))
    }

    suspend fun verifyRegisterCode(params: CodeVerifyParams): Data<Boolean> {
        val str = gson.toJson(params)
        return RetrofitInstance.service.verifyRegisterCode(params.email, createJsonRequestBody(str))
    }

    suspend fun verifyPasswordAuthCode(params: CodeVerifyParams): Data<Boolean> {
        val str = gson.toJson(params)
        return RetrofitInstance.service.verifyForgotPWEmail(params.email, createJsonRequestBody(str))
    }

    suspend fun register(params: RegisterParams): Data<Boolean> {
        val str = gson.toJson(params)
        return RetrofitInstance.service.register(createJsonRequestBody(str))
    }
}