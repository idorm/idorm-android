package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App
import org.appcenter.inudorm.model.SchedulesRequestDto
import org.appcenter.inudorm.model.TeamSchedule
import java.time.LocalDate

class GetTeamSchedules : ResultUseCase<LocalDate, List<TeamSchedule>>() {
    override suspend fun onExecute(params: LocalDate): List<TeamSchedule> {
        val month = params.monthValue.toString().padStart(2, '0')
        val param = SchedulesRequestDto("${params.year}-${month}")
        return App.calendarRepository.getMonthlyTeamSchedules(param)
    }
}