package org.appcenter.inudorm.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.*
import org.appcenter.inudorm.networking.ResponseWrapper
import org.appcenter.inudorm.repository.UserRepository
import java.io.IOException

abstract class LoginParams{
    abstract val email:String
}

data class UserInputParams(override val email: String, val password: String) : LoginParams()
class StoredUser(override val email: String, val token: String) : LoginParams()

object LoginResponseCode {
    const val SUCCESS = "SUCCESS"
}

class Login(
    private val userDataStore: DataStore<Preferences>,
) : UseCase<UserInputParams?, ResponseWrapper<Nothing>>() {
    private val emailKey = stringPreferencesKey("USER_EMAIL")
    private val tokenKey = stringPreferencesKey("USER_TOKEN")
    private val userRepository = UserRepository()

    // At the top level of your kotlin file:
    override suspend fun onExecute(params: UserInputParams?): ResponseWrapper<Nothing> {
        return if (params != null) {
            loginWithInput(params)
        } else { // 입력이 없으면 (자동로그인 시도) Todo: 앱 실행시 토큰 검증 등 재확인 절차 존재 여부 및 방법 결정
            //loginWithSavedCredentials()
            ResponseWrapper("NOT_IMPLEMENTED", responseMessage = "적용되지 않은 기능입니다. 어떻게 들어오셨어요..?")
        }
    }

    private suspend fun userFromStorage(): Flow<StoredUser?> {
        return userDataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            // Todo: 유저 정보를 어떻게 저장할지 나오면 반영.
            if (it[emailKey] == null || it[tokenKey] == null) {
                null
            } else {
                StoredUser(it[emailKey]!!, it[tokenKey]!!)
            }
        }
    }

    private suspend fun loginWithInput(params: UserInputParams): ResponseWrapper<Nothing> {
        return userRepository.login(params)
    }

//    private suspend fun loginWithSavedCredentials() : Boolean {
//        val storedUser = userFromStorage().first()
//        return userRepository.login(storedUser)
//    }

}