package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.mypage.UiState
import org.appcenter.inudorm.presentation.mypage.runCatch
import org.appcenter.inudorm.usecase.GetSinglePost

class PostDetailViewModel : ViewModel() {
    val _postDetailState = MutableStateFlow(UiState<Post>())
    val postDetailState: StateFlow<UiState<Post>>
        get() = _postDetailState


    fun getPost(id: Int) {
        viewModelScope.launch {
            runCatch(this@PostDetailViewModel::_postDetailState, GetSinglePost()::run, id)
        }
    }
}