package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.repository.UserRepository

enum class CodeType {
    Register,
    FindAccount
}

class SendAuthCode(private val codeType: CodeType) : UseCase<String, String?>() {
    private val userRepository = UserRepository()
    override suspend fun onExecute(params: String): String {
        return when (codeType) {
            CodeType.Register -> sendForRegister(params)
            CodeType.FindAccount -> sendForFindAccount(params)
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