package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.App.Companion.roomMateRepository
import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.EmailVerifyResponseDto
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.IDormError
import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import kotlin.reflect.KSuspendFunction1


class AddDislikedMatchingInfo : UseCase<Int, Unit>() {
    override suspend fun onExecute(params: Int): Unit {
        return roomMateRepository.addDislikedMatchingInfo(params)
    }
}