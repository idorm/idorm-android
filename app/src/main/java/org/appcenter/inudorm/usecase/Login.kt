package org.appcenter.inudorm.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.*
import org.appcenter.inudorm.repository.UserRepository
import java.io.IOException

abstract class LoginParams(val email: String)
class UserInputParams(email: String, val password: String) : LoginParams(email)
class StoredUser(email: String, val token: String) : LoginParams(email)

class Login(
    private val userDataStore: DataStore<Preferences>,
) : UseCase<UserInputParams?, Boolean>() {
    private val emailKey = stringPreferencesKey("USER_EMAIL")
    private val tokenKey = stringPreferencesKey("USER_TOKEN")
    private val userRepository = UserRepository()

    // At the top level of your kotlin file:
    override suspend fun onExecute(params: UserInputParams?): Boolean {
        return if (params != null) {
            loginWithInput(params)
        } else { // 입력이 없으면 (자동로그인 시도)
            loginWithSavedCredentials()
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

    private suspend fun loginWithInput(params: UserInputParams): Boolean {
        return userRepository.login(params)
    }

    private suspend fun loginWithSavedCredentials() : Boolean {
        val storedUser = userFromStorage().first()
        return userRepository.login(storedUser)
    }

}