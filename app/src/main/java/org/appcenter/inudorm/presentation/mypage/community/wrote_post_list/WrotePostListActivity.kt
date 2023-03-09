package org.appcenter.inudorm.presentation.mypage.community.wrote_post_list

import android.os.Bundle
import androidx.activity.viewModels
import org.appcenter.inudorm.presentation.adapter.PostAdapter
import org.appcenter.inudorm.presentation.mypage.community.PostListActivity

class WrotePostListActivity : PostListActivity() {
    val viewModel: WrotePostListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.postList.adapter = PostAdapter(arrayListOf()) {
            // Todo: navigate to post detail

        }
        viewModel.getPosts()

    }

}