package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.calendarRepository
import org.appcenter.inudorm.model.TeamSchedule


class GetTeamSchedule : ResultUseCase<Int, TeamSchedule>() {
    override suspend fun onExecute(params: Int): TeamSchedule {
        return calendarRepository.getTeamSchedule(params)
    }
}

