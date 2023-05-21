package org.appcenter.inudorm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.Calendar
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.usecase.*
import org.appcenter.inudorm.util.State

class HomeViewModel : ViewModel() {

    private val _topPostState = MutableStateFlow<State<ArrayList<Post>>>(State.Initial())
    val topPostState: StateFlow<State<ArrayList<Post>>>
        get() = _topPostState

    private val _calendarState = MutableStateFlow<State<ArrayList<Calendar>>>(State.Initial())
    val calendarState: StateFlow<State<ArrayList<Calendar>>>
        get() = _calendarState

    // Todo: 단순 fetching: loading -> fetch -> success
    fun getTopPosts() {
        viewModelScope.launch {
            if (_topPostState.value is State.Loading) return@launch
            _topPostState.emit(State.Loading())
            val dorm = kotlin.runCatching {
                GetMatchingInfo().run(null).dormCategory
            }.getOrNull()
            _topPostState.emit(
                ResultGetPosts().run(
                    GetPostParams(
                        BoardType.Top,
                        dorm ?: Dorm.DORM1, // 매칭정보가 없는 경우 DORM1로 fallback
                        0
                    )
                )
            )
        }
    }

    fun getCalendars() {
        viewModelScope.launch {
            if (_calendarState.value is State.Loading) return@launch

            _calendarState.emit(State.Loading())
            _calendarState.emit(
                GetCalendars().run(null)
            )
        }
    }
}