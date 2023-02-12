package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.board.WriteCommentDto
import org.appcenter.inudorm.presentation.mypage.UiState
import org.appcenter.inudorm.presentation.mypage.runCatch
import org.appcenter.inudorm.usecase.GetSinglePost
import org.appcenter.inudorm.usecase.WriteComment

class PostDetailViewModel : ViewModel() {
    private val _postDetailState = MutableStateFlow(UiState<Post>())
    val postDetailState: StateFlow<UiState<Post>>
        get() = _postDetailState

    private val _commentState = MutableStateFlow(WriteCommentDto())
    val commentState: StateFlow< WriteCommentDto>
        get() = _commentState


    fun getPost(id: Int) {
        viewModelScope.launch {
            _postDetailState.update {
                it.copy(loading = true)
            }
            runCatch(this@PostDetailViewModel::_postDetailState, GetSinglePost()::run, id)
        }
    }

    fun toggleAnonymous() {
        _commentState.update {
            it.copy(anonymous = !it.anonymous)
        }
    }

    fun writeComment() {
        viewModelScope.launch {
            kotlin.runCatching {
                WriteComment().run(_commentState.value.copy(postId = _postDetailState.value.data?.postId))
            }.onSuccess {

            }
        }

    }
}