package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.User
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.WriteCommentDto
import org.appcenter.inudorm.presentation.mypage.UiState
import org.appcenter.inudorm.presentation.mypage.runCatch
import org.appcenter.inudorm.usecase.*

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

    private val _commentWriteResult = MutableStateFlow<State?>(null)
    val commentWriteResult: StateFlow<State?>
        get() = _commentWriteResult

    private val _commentDeleteResult = MutableStateFlow<State?>(null)
    val commentDeleteResult: StateFlow<State?>
        get() = _commentDeleteResult

    private val _commentReportResult = MutableStateFlow<State?>(null)
    val commentReportResult: StateFlow<State?>
        get() = _commentReportResult

    private val _postLikeResult = MutableStateFlow<State?>(null)
    val postLikeResult: StateFlow<State?>
        get() = _postLikeResult

    private val _postReportResult = MutableStateFlow<State?>(null)
    val postReportResult: StateFlow<State?>
        get() = _postReportResult

    private val _postDeleteResult = MutableStateFlow<State?>(null)
    val postDeleteResult: StateFlow<State?>
        get() = _postDeleteResult


    fun setCommentInput(s: CharSequence, start: Int, before: Int, count: Int) {
        _commentState.update {
            it.copy(content = s.toString())
        }
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

    fun setParentComment(id: Int) {
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
            val state =
                WriteComment().run(
                    _commentState.value.copy(
                        postId = _postDetailState.value.data?.postId,
                        content = content
                    )
                )
            _commentWriteResult.emit(state)
        }

    }

    fun deletePost() {
        viewModelScope.launch {
            val state = DeletePost().run(postDetailState.value.data?.postId!!)
            _postDeleteResult.emit(state)
        }
    }

    fun reportPost() {
        viewModelScope.launch {
            val state = ReportPost().run(postDetailState.value.data?.postId!!)
            _postReportResult.emit(state)
        }
    }

    fun deleteComment(commentId: Int, postId: Int) {
        viewModelScope.launch {
            val state = DeleteComment().run(DeleteComment.Param(commentId, postId))
            _commentDeleteResult.emit(state)
        }
    }

    fun reportComment(id: Int) {
        viewModelScope.launch {
            val state = ReportComment().run(id)
            _commentReportResult.emit(state)
        }
    }

}