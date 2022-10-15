package org.appcenter.inudorm.repository

import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.appcenter.inudorm.networking.ResponseWrapper
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.usecase.RegisterParams
import org.appcenter.inudorm.usecase.SendAuthCodeParams
import org.appcenter.inudorm.usecase.UserInputParams

class UserRepository {
    val gson = Gson()

    suspend fun login(params: UserInputParams?): ResponseWrapper<Nothing> {
        val str = gson.toJson(params)
        return RetrofitInstance.service.login(createJsonRequestBody(str))
    }

    suspend fun sendAuthCode(params: SendAuthCodeParams): Nothing? {
        val str = gson.toJson(params)
        return RetrofitInstance.service.sendRegisterEmail(createJsonRequestBody(str)).data
    }

    suspend fun register(params: RegisterParams): Boolean {
        return flow {
            delay(1000)
            emit(true)
        }.first()
    }
}