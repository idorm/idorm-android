package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import org.appcenter.inudorm.repository.UserRepository


class SendAuthCode(private val codeType: EmailPromptPurpose,) : UseCase<String, String?>() {
    private val userRepository = UserRepository()
    override suspend fun onExecute(params: String): String {
        return when (codeType) {
            EmailPromptPurpose.Register -> sendForRegister(params)
            EmailPromptPurpose.FindPass -> sendForFindAccount(params)
        }
    }

    // Todo: 코드 전송 유형 별로 네트워크 요청 분기

    private suspend fun sendForFindAccount(email: String): String {
        return userRepository.sendAuthCode(email)
    }

    private suspend fun sendForRegister(email: String): String {
        return userRepository.sendAuthCode(email)
    }

}