package org.appcenter.inudorm.presentation.mypage.community.wrote_post_list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.presentation.adapter.PostAdapter
import org.appcenter.inudorm.presentation.mypage.community.PostListActivity

class WrotePostListActivity : PostListActivity() {
    val viewModel: WrotePostListViewModel by viewModels()
    override val title: String = "내가 쓴 글"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.postList.adapter = PostAdapter(arrayListOf()) {
            // Todo: navigate to post detail

        }
        viewModel.getPosts()

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getPosts()
        }

        lifecycleScope.launch {
            viewModel.postListState.collect { sortable ->
                setLoadingState(sortable.data.loading)
            }
        }

    }

}