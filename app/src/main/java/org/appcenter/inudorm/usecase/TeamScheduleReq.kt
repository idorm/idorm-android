package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.calendarRepository
import org.appcenter.inudorm.model.TeamSchedule
import org.appcenter.inudorm.model.TeamScheduleReq
import org.appcenter.inudorm.presentation.calendar.TeamSchedulePurpose
import kotlin.reflect.KSuspendFunction1

data class TeamScheduleParams(val codeType: TeamSchedulePurpose, val teamSchedule: TeamScheduleReq)

private val sendRepos : Map<TeamSchedulePurpose, KSuspendFunction1<TeamScheduleParams, TeamSchedule>> = mapOf(
    TeamSchedulePurpose.Create to calendarRepository::createTeamSchedule,
    TeamSchedulePurpose.Edit to calendarRepository::editTeamSchedule
)

class ModifyTeamSchedule : ResultUseCase<TeamScheduleParams, TeamSchedule> () {

    override suspend fun onExecute(params: TeamScheduleParams): TeamSchedule {
        return sendRepos[params.codeType]?.invoke(params)!!
    }
}