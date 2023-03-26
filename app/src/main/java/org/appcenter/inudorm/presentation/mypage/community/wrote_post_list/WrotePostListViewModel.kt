package org.appcenter.inudorm.presentation.mypage.community.wrote_post_list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.presentation.mypage.community.PostListViewModel
import org.appcenter.inudorm.usecase.GetWrotePosts

class WrotePostListViewModel : PostListViewModel() {

    override fun getPosts() {
        viewModelScope.launch {
            postListState.update {
                GetWrotePosts().run(null)
            }
        }
    }
}