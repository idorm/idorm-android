package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.ReportRequestDto
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.WriteCommentDto
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.IDormError
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState
import org.appcenter.inudorm.presentation.mypage.myinfo.runCatch
import org.appcenter.inudorm.usecase.*

enum class Content {
    POST,
    COMMENT,
    MEMBER
}

class PostDetailViewModel : ViewModel() {
    // Todo: MutableStateFlow를 Redux 처럼 쓸 수 있지 않을까요?!

    private val _postDetailState = MutableStateFlow(UiState<Post>())
    val postDetailState: StateFlow<UiState<Post>>
        get() = _postDetailState

    private val _commentState = MutableStateFlow(WriteCommentDto())
    val commentState: StateFlow<WriteCommentDto>
        get() = _commentState

    private val _userState = MutableStateFlow(UiState<User>())
    val userState: StateFlow<UiState<User>>
        get() = _userState

    private val _sortState = MutableStateFlow("asc")
    val sortState: StateFlow<String>
        get() = _sortState

    private val _commentWriteResult = MutableStateFlow<State?>(null)
    val commentWriteResult: StateFlow<State?>
        get() = _commentWriteResult

    private val _commentDeleteResult = MutableStateFlow<State?>(null)
    val commentDeleteResult: StateFlow<State?>
        get() = _commentDeleteResult

    private val _commentReportResult =
        MutableStateFlow<org.appcenter.inudorm.util.State<Boolean>>(org.appcenter.inudorm.util.State.Initial())
    val commentReportResult: StateFlow<org.appcenter.inudorm.util.State<Boolean>>
        get() = _commentReportResult

    private val _postLikeResult = MutableStateFlow<State?>(null)
    val postLikeResult: StateFlow<State?>
        get() = _postLikeResult

    private val _postReportResult =
        MutableStateFlow<org.appcenter.inudorm.util.State<Boolean>>(org.appcenter.inudorm.util.State.Initial())
    val postReportResult: StateFlow<org.appcenter.inudorm.util.State<Boolean>>
        get() = _postReportResult

    private val _postDeleteResult = MutableStateFlow<State?>(null)
    val postDeleteResult: StateFlow<State?>
        get() = _postDeleteResult


    fun setCommentInput(s: CharSequence, start: Int, before: Int, count: Int) {
        _commentState.update {
            it.copy(content = s.toString())
        }
    }

    fun setSort(sort: String) {
        if (sort != "asc" && sort != "desc") throw java.lang.RuntimeException("정렬 값 잘못됨")
        _sortState.update { sort }
    }

    fun getPost(id: Int) {
        viewModelScope.launch {
            _postDetailState.update {
                it.copy(loading = true)
            }
            runCatch(this@PostDetailViewModel::_userState, LoginRefresh()::run, null)
            runCatch(this@PostDetailViewModel::_postDetailState, GetSinglePost()::run, id)
        }
    }

    fun toggleAnonymous() {
        _commentState.update {
            it.copy(isAnonymous = !it.isAnonymous)
        }
    }

    fun setParentComment(id: Int?) {
        _commentState.update {
            it.copy(parentCommentId = id)
        }
    }

    fun writeComment() {
        val content = _commentState.value.content
        _commentState.update {
            it.copy(content = "")
        }
        viewModelScope.launch {
            _commentWriteResult.emit(State.Loading)
            val state =
                WriteComment().run(
                    _commentState.value.copy(
                        postId = _postDetailState.value.data?.postId,
                        content = content,
                    )
                )
            setParentComment(null)
            _commentWriteResult.emit(state)
        }

    }

    fun deletePost() {
        viewModelScope.launch {
            _postDeleteResult.emit(State.Loading)
            val state = DeletePost().run(postDetailState.value.data?.postId!!)
            _postDeleteResult.emit(state)
        }
    }

    fun deleteComment(commentId: Int, postId: Int) {
        viewModelScope.launch {
            _commentDeleteResult.emit(State.Loading)
            val state = DeleteComment().run(DeleteComment.Param(commentId, postId))
            _commentDeleteResult.emit(state)
        }
    }

    fun report(id: Int, contentType: Content, reasonType: String, reason: String) {
        viewModelScope.launch {
            _commentReportResult.emit(org.appcenter.inudorm.util.State.Loading())
            val state = Report().run(
                ReportRequestDto(
                    memberOrPostOrCommentId = id,
                    reason = reason,
                    reasonType = reasonType,
                    reportType = contentType
                )
            )
            _commentReportResult.emit(state)
        }
    }

    fun toggleLike() {
        viewModelScope.launch {
            if (userState.value.data?.memberId == postDetailState.value.data?.memberId) {
                _postLikeResult.emit(
                    State.Error(IDormError(ErrorCode.CANNOT_LIKED_SELF))
                )
                return@launch
            }
            _postLikeResult.emit(
                ToggleLikePost().run(
                    ToggleLikePost.Param(
                        postDetailState.value.data?.postId!!,
                        postDetailState.value.data?.isLiked!!
                    )
                )
            )
            _postDetailState.update {
                it.copy(
                    data = it.data?.copy(
                        isLiked = !it.data.isLiked,
                        likesCount = if (!it.data.isLiked) it.data.likesCount + 1 else it.data.likesCount - 1
                    )
                )
            }

        }
    }

}