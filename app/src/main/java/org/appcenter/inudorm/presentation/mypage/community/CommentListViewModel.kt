package org.appcenter.inudorm.presentation.mypage.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.presentation.mypage.matching.Sortable
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState
import org.appcenter.inudorm.usecase.GetWroteComments


class CommentListViewModel : ViewModel() {
    private val _commentListState =
        MutableStateFlow(Sortable("addedAtDesc", UiState<ArrayList<Comment>>()))
    val commentListState: StateFlow<Sortable<UiState<ArrayList<Comment>>>>
        get() = _commentListState


    fun setSortBy(sortBy: String) {
        _commentListState.update {
            it.copy(sortBy = sortBy)
        }
    }

    fun getComments() {
        viewModelScope.launch {
            _commentListState.update {
                //GetWroteComments().run(null)
                it.copy(
                    data = UiState(
                        data = arrayListOf(
                            Comment(
                                1,
                                "댓글1",
                                "2023-03-01 03:02:22",
                                false,
                                3,
                                "닉네임",
                                null, null, null
                            ),
                            Comment(
                                2,
                                "댓글2",
                                "2023-03-01 03:02:22",
                                false,
                                3,
                                "닉네임",
                                null, null, null
                            ),
                            Comment(
                                3,
                                "댓글3",
                                "2023-03-01 03:02:22",
                                false,
                                3,
                                "닉네임",
                                null, null, null
                            ),
                            Comment(
                                4,
                                "댓글4",
                                "2023-03-01 03:02:22",
                                false,
                                3,
                                "닉네임",
                                null, null, null
                            ),
                        )
                    )
                )

            }
        }
    }


}