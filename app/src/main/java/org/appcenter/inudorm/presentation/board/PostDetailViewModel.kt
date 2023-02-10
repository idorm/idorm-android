package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.mypage.UiState
import org.appcenter.inudorm.presentation.mypage.runCatch
import org.appcenter.inudorm.repository.CommunityRepository
import org.appcenter.inudorm.usecase.GetSinglePost
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(private val communityRepository: CommunityRepository) :
    ViewModel() {
    val _postDetailState = MutableStateFlow(UiState<Post>())
    val postDetailState: StateFlow<UiState<Post>>
        get() = _postDetailState


    fun getPost(id: Int) {
        viewModelScope.launch {
            _postDetailState.update {
                it.copy(loading = true)
            }
            runCatch(
                this@PostDetailViewModel::_postDetailState,
                GetSinglePost(communityRepository)::run,
                id
            )
        }
    }
}