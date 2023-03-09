package org.appcenter.inudorm.presentation.mypage.community

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.mypage.matching.Sortable
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState

abstract class PostListViewModel : ViewModel() {
    val postListState = MutableStateFlow(Sortable("addedAtDesc", UiState<ArrayList<Post>>()))
    abstract fun getPosts(): Unit
    fun setSortBy(sortBy: String) {
        postListState.update {
            it.copy(sortBy = sortBy)

        }
    }

}