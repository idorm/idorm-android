package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.SchedulesRequestDto
import org.appcenter.inudorm.model.TeamSchedule

class GetTeamSchedules : ResultUseCase<String, List<TeamSchedule>>() {
    override suspend fun onExecute(params: String): List<TeamSchedule> {
        return App.calendarRepository.getMonthlyTeamSchedules(SchedulesRequestDto(params))
    }
}