package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.SchedulesRequestDto
import org.appcenter.inudorm.model.TeamSchedule
import java.time.LocalDate

class ConfirmTeamDeleted : ResultUseCase<Nothing?, Unit>() {
    override suspend fun onExecute(params: Nothing?) {
        return App.roomMateTeamRepository.assumeTeamDisorganization()
    }
}