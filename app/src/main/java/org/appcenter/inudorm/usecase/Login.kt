package org.appcenter.inudorm.usecase

import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.tasks.await
import org.appcenter.inudorm.App
import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.IDormError
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.IDormLogger
import java.io.IOException

abstract class LoginParams {
    abstract val email: String
}

data class UserInputParams(override val email: String, val password: String) : LoginParams()

class Login(private val prefsRepository: PrefsRepository) :
    UseCase<UserInputParams, UiState<Boolean>>() {

    // At the top level of your kotlin file:
    override suspend fun onExecute(params: UserInputParams): UiState<Boolean> {
        return loginWithInput(params)
    }

    private suspend fun loginWithInput(params: UserInputParams): UiState<Boolean> {
        return kotlin.runCatching {
            val tokenTask = FirebaseMessaging.getInstance().token
            try {
                tokenTask.await()
                if (!tokenTask.isSuccessful || tokenTask.result == null) {
                    UiState(false)
                }
                val fcmToken = tokenTask.result
                val token = userRepository.login(params, fcmToken).headers()["AUTHORIZATION"]
                IDormLogger.i(this, "token: $token")
                prefsRepository.setUserToken(token)
                App.token = token
                UiState(true)

            } catch (e: IOException) {
                e.printStackTrace()
                // SERVICE_NOT_AVAILABLE
                UiState(error = e)
            }
        }.getOrElse {
            UiState(error = it)
        }
    }

}

class LoginRefresh : UseCase<Nothing?, User>() {
    override suspend fun onExecute(params: Nothing?): User {
        return userRepository.loginRefresh()
    }

}