package org.appcenter.inudorm.usecase

import com.google.gson.Gson
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import org.appcenter.inudorm.repository.UserRepository

data class SendAuthCodeParams(val email:String)

class SendAuthCode(private val codeType: EmailPromptPurpose,) : UseCase<String, Boolean?>() {
    val gson = Gson()

    override suspend fun onExecute(params: String): Boolean {
        return when (codeType) {
            EmailPromptPurpose.Register -> sendForRegister(params)
            EmailPromptPurpose.FindPass -> sendForFindAccount(params)
        }
    }

    // Todo: 코드 전송 유형 별로 네트워크 요청 분기

    private suspend fun sendForFindAccount(email: String): Boolean {
        val body = gson.toJson({email})
        val jsonBody = createJsonRequestBody(body)
        return RetrofitInstance.service.sendForgotPWEmail(jsonBody).responseCode == "OK"
    }

    private suspend fun sendForRegister(email: String) : Boolean {
        val body = gson.toJson({email})
        val jsonBody = createJsonRequestBody(body)
        return RetrofitInstance.service.sendRegisterEmail(jsonBody).responseCode == "OK"
    }
}