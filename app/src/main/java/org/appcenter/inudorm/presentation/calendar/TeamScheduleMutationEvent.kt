package org.appcenter.inudorm.presentation.calendar

import org.appcenter.inudorm.model.TeamScheduleReq
import org.appcenter.inudorm.usecase.TeamScheduleParams


sealed class TeamScheduleMutationEvent(override val mutation: TeamMutation<*,*>) : TeamMutationEvent(mutation){
    class CreateTeamSchedule(override val mutation: TeamMutation<TeamScheduleParams, TeamScheduleReq>) : TeamScheduleMutationEvent(mutation)
    class EditTeamSchedule(override val mutation: TeamMutation<TeamScheduleParams, TeamScheduleReq>) : TeamScheduleMutationEvent(mutation)
}