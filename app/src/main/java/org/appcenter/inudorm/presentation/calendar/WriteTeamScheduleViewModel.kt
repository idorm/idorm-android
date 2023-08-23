package org.appcenter.inudorm.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.RoomMateTeamResponseDto
import org.appcenter.inudorm.model.TeamSchedule
import org.appcenter.inudorm.model.TeamScheduleReq
import org.appcenter.inudorm.presentation.onboard.BaseInfoState
import org.appcenter.inudorm.usecase.*
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.State
import java.time.LocalDate


sealed class TeamMutationEvent(open val mutation: TeamMutation<*, *>)

data class TeamMutation<P, R>(val request: P, val state: State<R>)


class WriteTeamScheduleViewModel(private val purpose: TeamSchedulePurpose) : ViewModel() {
    private val _teamScheduleMutationEvent: MutableStateFlow<TeamScheduleMutationEvent?> =
        MutableStateFlow(null)
    val teamScheduleMutationEvent: StateFlow<TeamScheduleMutationEvent?>
        get() = _teamScheduleMutationEvent

    private val _roomMateTeam: MutableStateFlow<State<RoomMateTeamResponseDto>> =
        MutableStateFlow(State.Initial())
    val roomMateTeam: StateFlow<State<RoomMateTeamResponseDto>>
        get() = _roomMateTeam

    private val _teamSchedule: MutableStateFlow<State<TeamSchedule>> =
        MutableStateFlow(State.Initial())
    val teamSchedule: StateFlow<State<TeamSchedule>>
        get() = _teamSchedule


    private suspend fun getRoomMates() {
        if (roomMateTeam.value is State.Loading)
            _roomMateTeam.emit(State.Loading())

        val targets = _teamSchedule.value.data?.targets
        _roomMateTeam.emit(
            GetRoomMateTeam().run(null) {
                it.copy(
                    members = it.members.map { profile ->
                        val target =
                            targets?.find { target -> target.memberId == profile.memberId }

                        if (purpose == TeamSchedulePurpose.Create)
                            profile.copy(hasInvitedToSchedule = false)
                        else profile.copy(hasInvitedToSchedule = target != null)

                    }
                )
            }
        )
    }

    fun loadInitialPage(teamCalendarId: Int) {
        viewModelScope.launch {
            // 동기적으로 수행합니다.
            getTeamSchedule(teamCalendarId)
            getRoomMates()
        }
    }

    private suspend fun getTeamSchedule(teamCalendarId: Int) {
        if (teamSchedule.value is State.Loading)
            _teamSchedule.emit(State.Loading())
        _teamSchedule.emit(GetTeamSchedule().run(teamCalendarId))
    }


    fun submit(teamScheduleReq: TeamScheduleReq) {
        val teamScheduleParams = TeamScheduleParams(purpose, teamScheduleReq)
        viewModelScope.launch {
            _teamScheduleMutationEvent.emit(
                TeamScheduleMutationEvent.CreateTeamSchedule(
                    TeamMutation(teamScheduleParams, ModifyTeamSchedule().run(teamScheduleParams))
                )
            )
        }
    }

    fun delete(teamCalendarId: Int) {
        viewModelScope.launch {
            DeleteSchedule().run(teamCalendarId)
        }
    }


}

class TeamScheduleViewModelFactory(private val purpose: TeamSchedulePurpose) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WriteTeamScheduleViewModel::class.java)) {
            return WriteTeamScheduleViewModel(purpose) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}