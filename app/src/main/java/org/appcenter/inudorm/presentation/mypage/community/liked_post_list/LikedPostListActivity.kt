package org.appcenter.inudorm.presentation.mypage.community.liked_post_list

import android.os.Bundle
import androidx.activity.viewModels
import org.appcenter.inudorm.presentation.adapter.PostAdapter
import org.appcenter.inudorm.presentation.mypage.community.PostListActivity
import org.appcenter.inudorm.presentation.mypage.community.wrote_post_list.WrotePostListViewModel

class LikedPostListActivity : PostListActivity() {
    val viewModel: LikedPostListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.toolbarText.text = "공감한 글"
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.postList.adapter = PostAdapter(arrayListOf()) {
            // Todo: navigate to post detail

        }
        viewModel.getPosts()

    }
}