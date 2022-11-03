package org.appcenter.inudorm.usecase

import com.google.gson.Gson
import org.appcenter.inudorm.networking.Data
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.userRepository
import kotlin.reflect.KSuspendFunction1

data class SendAuthCodeParams(val codeType: EmailPromptPurpose, val email:String)

private val sendRepos : Map<EmailPromptPurpose, KSuspendFunction1<SendAuthCodeParams, Data<Boolean>>> = mapOf(
    EmailPromptPurpose.Register to userRepository::sendRegisterAuthCode,
    EmailPromptPurpose.FindPass to userRepository::sendPasswordAuthCode
)

class SendAuthCode : UseCase<SendAuthCodeParams, Data<Boolean>>() {
    override suspend fun onExecute(params: SendAuthCodeParams): Data<Boolean> {
        return sendRepos[params.codeType]?.invoke(params)!!
    }
}