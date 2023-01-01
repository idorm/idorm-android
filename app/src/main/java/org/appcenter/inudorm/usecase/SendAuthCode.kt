package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.presentation.account.prompt.EmailPromptPurpose
import kotlin.reflect.KSuspendFunction1

data class SendAuthCodeParams(val codeType: EmailPromptPurpose, val email:String)

private val sendRepos : Map<EmailPromptPurpose, KSuspendFunction1<SendAuthCodeParams, Unit>> = mapOf(
    EmailPromptPurpose.Register to userRepository::sendRegisterAuthCode,
    EmailPromptPurpose.FindPass to userRepository::sendPasswordAuthCode
)

class SendAuthCode : UseCase<SendAuthCodeParams, Unit>() {
    override suspend fun onExecute(params: SendAuthCodeParams) {
        return sendRepos[params.codeType]?.invoke(params)!!
    }
}