package org.appcenter.inudorm.presentation.mypage.community

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityPostListBinding
import org.appcenter.inudorm.presentation.adapter.PostAdapter

/**
 * 내가 쓴 글, 공감한 글 등 특정 조건을 충족하는 글들을 최신/과거순으로 보여주는 액티비티
 */
open class PostListActivity : AppCompatActivity() {
    val binding: ActivityPostListBinding by lazy {
        DataBindingUtil.setContentView(this@PostListActivity, R.layout.activity_post_list)
    }

    val viewModel: PostListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.postList.adapter = PostAdapter(arrayListOf()) {
            // Todo: navigate to post detail
        }

    }
}