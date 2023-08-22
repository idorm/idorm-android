package org.appcenter.inudorm.presentation.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.RoomMateTeamResponseDto
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.model.TeamSchedule
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.presentation.matching.Mutation
import org.appcenter.inudorm.presentation.matching.MutationEvent
import org.appcenter.inudorm.presentation.matching.MyInfoMutationEvent
import org.appcenter.inudorm.presentation.matching.RoomMutationEvent
import org.appcenter.inudorm.usecase.DeleteMate
import org.appcenter.inudorm.usecase.DeleteProfilePhoto
import org.appcenter.inudorm.usecase.DeleteSchedule
import org.appcenter.inudorm.usecase.GetCalendars
import org.appcenter.inudorm.usecase.GetRoomMateTeam
import org.appcenter.inudorm.usecase.GetRoomMates
import org.appcenter.inudorm.usecase.GetTeamSchedules
import org.appcenter.inudorm.usecase.LoginRefresh
import org.appcenter.inudorm.usecase.ResultUseCase
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.State
import java.time.LocalDate
import kotlin.reflect.KFunction1


class CalendarViewModel : ViewModel() {
    private val _selectedDay = MutableLiveData(LocalDate.now())
    val selectedDay: LiveData<LocalDate> get() = _selectedDay


    val schedules: MutableStateFlow<State<List<TeamSchedule>>> =
        MutableStateFlow(State.Initial())

    private val _roomMateTeam: MutableStateFlow<State<RoomMateTeamResponseDto>> =
        MutableStateFlow(State.Initial())
    val roomMateTeam: StateFlow<State<RoomMateTeamResponseDto>>
        get() = _roomMateTeam


    private val _officialSchedules: MutableStateFlow<State<List<Schedule>>> =
        MutableStateFlow(State.Initial())
    val officialSchedules: StateFlow<State<List<Schedule>>>
        get() = _officialSchedules

    fun selectDay(date: LocalDate) {
        _selectedDay.value = date
    }

    private val _userState: MutableStateFlow<State<User>> = MutableStateFlow(State.Initial())
    val userState: StateFlow<State<User>>
        get() = _userState

    fun getUser() {
        viewModelScope.launch {
            if (_userState.value is State.Loading) return@launch

            _userState.emit(State.Loading())
            _userState.emit(kotlin.runCatching { State.Success(LoginRefresh().run(null)) }
                .getOrElse { State.Error(it) })
        }
    }

    fun getSchedules(date: LocalDate) {
        viewModelScope.launch {
            if (schedules.value is State.Loading) return@launch
            schedules.emit(State.Loading())
            schedules.emit(GetTeamSchedules().run(date))
        }
    }

    fun getRoomMates() {
        viewModelScope.launch {
            if (roomMateTeam.value is State.Loading) return@launch
            _roomMateTeam.emit(State.Loading())
            _roomMateTeam.emit(GetRoomMateTeam().run(null))
        }
    }

    fun getOfficialSchedules(date: LocalDate) {
        viewModelScope.launch {
            if (roomMateTeam.value is State.Loading) return@launch
            _officialSchedules.emit(State.Loading())
            _officialSchedules.emit(GetCalendars().run(date))
        }
    }

    private val _roomMutationEvent: MutableStateFlow<RoomMutationEvent?> =
        MutableStateFlow(null)
    val roomMutationEvent: StateFlow<RoomMutationEvent?>
        get() = _roomMutationEvent


    fun deleteMate(memberId: Int) {
        viewModelScope.launch {
            if (_roomMutationEvent.value?.mutation?.state is State.Loading) return@launch

            _roomMutationEvent.emit(
                RoomMutationEvent.DeleteMate(
                    Mutation(
                        memberId,
                        State.Loading()
                    )
                )
            )
            _roomMutationEvent.emit(
                RoomMutationEvent.DeleteMate(
                    Mutation(memberId, DeleteMate().run(memberId))
                )
            )
        }
    }

    fun deleteSchedule(scheduleId: Int) {
        viewModelScope.launch {
            if (_roomMutationEvent.value?.mutation?.state is State.Loading) return@launch

            _roomMutationEvent.emit(
                RoomMutationEvent.DeleteSchedule(
                    Mutation(
                        scheduleId,
                        State.Loading()
                    )
                )
            )
            _roomMutationEvent.emit(
                RoomMutationEvent.DeleteSchedule(
                    Mutation(scheduleId, DeleteSchedule().run(scheduleId))
                )
            )
        }
    }

    fun leave() {
        viewModelScope.launch {
            if (userState.value !is State.Success) return@launch
            if (_roomMutationEvent.value?.mutation?.state is State.Loading) return@launch

            _roomMutationEvent.emit(
                RoomMutationEvent.Leave(
                    Mutation(
                        null,
                        State.Loading()
                    )
                )
            )
            _roomMutationEvent.emit(
                RoomMutationEvent.Leave(
                    Mutation(
                        null,
                        DeleteMate().run((userState.value as State.Success<User>).data?.memberId!!)
                    )
                )
            )
        }
    }

//    fun onDateChanged(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
//        IDormLogger.i(this, "${selected} | ${date.day}")
//        if (selected) {
//            _selectedDay.value = date.day
//            _selectedDay.postValue(date.day)
//        }
//    }
}
