package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.EmailVerifyResponseDto
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.IDormError
import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import kotlin.reflect.KSuspendFunction1

data class CodeVerifyParams(
    val checkEmailPurpose: EmailPromptPurpose,
    val email: String,
    val code: String
)

private val verifyRepos: Map<EmailPromptPurpose, KSuspendFunction1<CodeVerifyParams, EmailVerifyResponseDto>> =
    mapOf(
        EmailPromptPurpose.Register to userRepository::verifyRegisterCode,
        EmailPromptPurpose.FindPass to userRepository::verifyPasswordAuthCode
    )

class CheckIfCodeCorrect : UseCase<CodeVerifyParams, Boolean>() {
    override suspend fun onExecute(params: CodeVerifyParams): Boolean {
        val emailVerifyResponse = verifyRepos[params.checkEmailPurpose]?.invoke(params)
        if (emailVerifyResponse != null) return true
        else throw IDormError(ErrorCode.UNKNOWN_ERROR)

    }
}