package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.EmailVerifyResponseDto
import org.appcenter.inudorm.networking.Data
import org.appcenter.inudorm.networking.ResponseWrapper
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import kotlin.reflect.KSuspendFunction1

data class CodeVerifyParams(
    val checkEmailPurpose: EmailPromptPurpose,
    val email: String,
    val code: String
)

private val verifyRepos: Map<EmailPromptPurpose, KSuspendFunction1<CodeVerifyParams, Data<EmailVerifyResponseDto>>> =
    mapOf(
        EmailPromptPurpose.Register to userRepository::verifyRegisterCode,
        EmailPromptPurpose.FindPass to userRepository::verifyPasswordAuthCode
    )

class CheckIfCodeCorrect : UseCase<CodeVerifyParams, Data<Boolean>>() {
    override suspend fun onExecute(params: CodeVerifyParams): Data<Boolean> {
        val emailVerifyResponse = verifyRepos[params.checkEmailPurpose]?.invoke(params)?.data
        return if (emailVerifyResponse == null) {
            Data(error = "인증번호 확인에 실패했습니다.")
        } else if (!emailVerifyResponse.check) {
            Data(error = "인증번호가 일치하지 않습니다.")
        } else if (emailVerifyResponse.join) {
            Data(error = "이미 가입된 이메일입니다.")
        } else {
            Data(data = true)
        }
    }
}