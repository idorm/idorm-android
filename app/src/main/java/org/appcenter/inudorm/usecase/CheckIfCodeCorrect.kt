package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.networking.Data
import org.appcenter.inudorm.networking.ResponseWrapper
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import org.appcenter.inudorm.userRepository
import kotlin.reflect.KSuspendFunction1

data class CodeVerifyParams(val checkEmailPurpose: EmailPromptPurpose, val email:String, val code: String)

private val verifyRepos : Map<EmailPromptPurpose, KSuspendFunction1<CodeVerifyParams, Data<Boolean>>> = mapOf(
    EmailPromptPurpose.Register to userRepository::verifyRegisterCode,
    EmailPromptPurpose.FindPass to userRepository::verifyPasswordAuthCode
)

class CheckIfCodeCorrect : UseCase<CodeVerifyParams, Data<Boolean>>() {
    override suspend fun onExecute(params: CodeVerifyParams): Data<Boolean> {
        return verifyRepos[params.checkEmailPurpose]?.invoke(params)!!
    }
}