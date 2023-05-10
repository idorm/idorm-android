package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.calendarRepository
import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.Calendar
import org.appcenter.inudorm.model.CalendarsRequestDto
import org.appcenter.inudorm.model.board.Photo
import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat

class DeleteProfilePhoto : ResultUseCase<Nothing?, Unit>() {
    override suspend fun onExecute(params: Nothing?) {
        return userRepository.deleteProfilePhoto()
    }
}