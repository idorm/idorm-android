package org.appcenter.inudorm.presentation.calendar

import org.appcenter.inudorm.model.TeamSchedule
import org.appcenter.inudorm.usecase.TeamScheduleParams


sealed class TeamScheduleMutationEvent(override val mutation: TeamMutation<*,*>) : TeamMutationEvent(mutation){
    class CreateTeamSchedule(override val mutation: TeamMutation<TeamScheduleParams, TeamSchedule>) : TeamScheduleMutationEvent(mutation)
    class EditTeamSchedule(override val mutation: TeamMutation<TeamScheduleParams, TeamSchedule>) : TeamScheduleMutationEvent(mutation)
}