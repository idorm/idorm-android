package org.appcenter.inudorm.presentation.mypage.community

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState

abstract class PostListViewModel : ViewModel() {
    private val _postListState = MutableStateFlow(UiState<ArrayList<Post>>())
    val postListState: StateFlow<UiState<ArrayList<Post>>>
        get() = _postListState

    abstract fun getPosts(): Unit

}