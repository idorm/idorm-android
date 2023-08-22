package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.TeamSchedule


class DeleteSchedule : ResultUseCase<Int, Unit>() {
    override suspend fun onExecute(params: Int) {
        return App.calendarRepository.deleteTeamSchedule(params.toLong())
    }
}

