package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App


class GetTeamSchedule : ResultUseCase<Long, org.appcenter.inudorm.model.TeamSchedule>() {
    override suspend fun onExecute(params: Long): org.appcenter.inudorm.model.TeamSchedule {
        return App.calendarRepository.getTeamSchedule(params)
    }
}

