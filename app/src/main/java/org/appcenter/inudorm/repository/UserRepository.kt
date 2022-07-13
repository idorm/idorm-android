package org.appcenter.inudorm.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.appcenter.inudorm.usecase.LoginParams
import org.appcenter.inudorm.usecase.StoredUser
import org.appcenter.inudorm.usecase.UserInputParams

class UserRepository {
     suspend fun login(params: LoginParams?) : Boolean {
        return flow {
            delay(1000)
            emit(true)
        }.first()
    }
}