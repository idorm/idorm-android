package org.appcenter.inudorm.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.appcenter.inudorm.usecase.LoginParams
import org.appcenter.inudorm.usecase.RegisterParams
import org.appcenter.inudorm.usecase.StoredUser
import org.appcenter.inudorm.usecase.UserInputParams

class UserRepository {
     suspend fun login(params: LoginParams?) : Boolean {
        return flow {
            delay(1000)
            emit(false)
        }.first()
    }
    suspend fun sendAuthCode(email: String) : String {
        return flow {
            delay(1000)
            emit("22lZgsZGjJeaH5vUMFmTd1j0SMAlZYsQ76uyoBC6DHM=") // 432849
        }.first()
    }
    suspend fun register(params: RegisterParams): Boolean {
        return flow {
            delay(1000)
            emit(true)
        }.first()
    }
}