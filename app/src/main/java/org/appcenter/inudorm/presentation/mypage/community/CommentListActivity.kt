package org.appcenter.inudorm.presentation.mypage.community

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityCommentListBinding
import org.appcenter.inudorm.presentation.adapter.CommentListAdapter
import org.appcenter.inudorm.presentation.board.BoardFragment
import org.appcenter.inudorm.presentation.board.PostDetailActivity

/**
 * 내가 쓴 댓글 등 댓글 리스트를 보여주는 액티비티
 */
class CommentListActivity : AppCompatActivity() {

    private val binding: ActivityCommentListBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_comment_list)
    }
    fun goDetail(id: Int) {
        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivityForResult(intent, BoardFragment.DETAIL_OPEN)
    }

    private val viewModel: CommentListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.commentListRecycler.adapter = CommentListAdapter(arrayListOf()) {
            goDetail(it.postId)
        }
        viewModel.getComments()



    }
}