package org.appcenter.inudorm.presentation.mypage.community.liked_post_list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.presentation.mypage.community.PostListViewModel
import org.appcenter.inudorm.usecase.GetLikedPosts

class LikedPostListViewModel : PostListViewModel() {
    override fun getPosts() {
        viewModelScope.launch {
            postListState.update {
                GetLikedPosts().run(null)
            }
        }
    }
}