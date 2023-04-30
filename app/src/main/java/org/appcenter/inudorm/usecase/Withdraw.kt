package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.repository.PrefsRepository

class Withdraw(private val prefsRepository: PrefsRepository) : UseCase<Nothing?, Boolean>() {
    override suspend fun onExecute(params: Nothing?): Boolean {
        userRepository.withdraw()
        prefsRepository.signOut()
        return true
    }
}