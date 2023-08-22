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
import org.appcenter.inudorm.util.State
import java.time.LocalDate


sealed class TeamMutationEvent(open val mutation: TeamMutation<*, *>)

data class TeamMutation<P, R>(val request: P, val state: State<R>)


class WriteTeamScheduleViewModel(private val purpose: TeamSchedulePurpose) : ViewModel() {
    private val _teamScheduleMutationEvent: MutableStateFlow<TeamScheduleMutationEvent?> = MutableStateFlow(null)
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


    fun getRoomMates() {
        viewModelScope.launch {
            if (roomMateTeam.value is State.Loading) return@launch
            _roomMateTeam.emit(State.Loading())
            _roomMateTeam.emit(GetRoomMateTeam().run(null))
        }
    }

    fun getTeamSchedule(teamCalendarId : Int) {
        viewModelScope.launch() {
            if (teamSchedule.value is State.Loading) return@launch
            _teamSchedule.emit(State.Loading())
            _teamSchedule.emit(GetTeamSchedule().run(teamCalendarId))
        }
    }

    fun submit(teamScheduleReq: TeamScheduleReq ){
        val teamScheduleParams = TeamScheduleParams(purpose, teamScheduleReq)
        viewModelScope.launch {
            _teamScheduleMutationEvent.emit(
                TeamScheduleMutationEvent.CreateTeamSchedule(
                TeamMutation(teamScheduleParams, ModifyTeamSchedule().run(teamScheduleParams))
            ))
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