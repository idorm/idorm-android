package org.appcenter.inudorm.presentation.mypage.community

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityCommentListBinding
import org.appcenter.inudorm.presentation.adapter.CommentListAdapter
import org.appcenter.inudorm.presentation.board.BoardFragment
import org.appcenter.inudorm.presentation.board.PostDetailActivity

/**
 * 내가 쓴 댓글 등 댓글 리스트를 보여주는 액티비티
 */
class CommentListActivity : LoadingActivity() {

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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbarText.text = "내가 쓴 댓글"

        binding.commentListRecycler.adapter = CommentListAdapter(arrayListOf()) {
            goDetail(it.postId)
        }
        viewModel.getComments()
        lifecycleScope.launch {
            viewModel.commentListState.collect { sortable ->
                setLoadingState(sortable.data.loading)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            android.R.id.home -> { // 메뉴 버튼, 사실상 백버튼으로 취급하면 됩니다!
                this.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}