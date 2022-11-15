package org.appcenter.inudorm.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.*
import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.SavedUser
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.networking.Data
import org.appcenter.inudorm.networking.ResponseWrapper
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.util.IDormLogger
import java.io.IOException

abstract class LoginParams {
    abstract val email: String
}

data class UserInputParams(override val email: String, val password: String) : LoginParams()
class StoredUser(override val email: String, val token: String) : LoginParams()

object LoginResponseCode {
    const val SUCCESS = "SUCCESS"
}

class Login(private val prefsRepository: PrefsRepository) : UseCase<UserInputParams, Data<Boolean>>() {

    // At the top level of your kotlin file:
    override suspend fun onExecute(params: UserInputParams): Data<Boolean> {
        return loginWithInput(params)
    }

    private suspend fun loginWithInput(params: UserInputParams): Data<Boolean> {
        val user = userRepository.login(params)
        val token = user.data?.loginToken
        // Todo: 토큰 저장
        IDormLogger.i(this, "token: $token")
        return if (token != null) {
            prefsRepository.setUserToken(token)
            Data(data = true)
        } else {
            Data(data = false)
        }
    }
}

class LoginRefresh : UseCase<Nothing?, Data<User>>() {
    override suspend fun onExecute(params: Nothing?): Data<User> {
        return userRepository.loginRefresh()
    }

}