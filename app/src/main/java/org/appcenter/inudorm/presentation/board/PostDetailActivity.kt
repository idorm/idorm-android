package org.appcenter.inudorm.presentation.board

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityPostDetailBinding
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.adapter.CommentAdapter
import org.appcenter.inudorm.presentation.adapter.ImageViewAdapter
import org.appcenter.inudorm.util.IDormLogger


/**
 * 커뮤니티 글 보기 페이지에서 눌러서 들어가는 페이지. PostList
 */
class PostDetailActivity : AppCompatActivity() {

    private val binding: ActivityPostDetailBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_post_detail)
    }
    private val viewModel: PostDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postId = intent.getIntExtra("id", -9999)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        viewModel.getPost(postId)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getPost(postId)
        }
        binding.images.adapter = ImageViewAdapter(arrayListOf()) {

        }

        lifecycleScope.launch {
            viewModel.postDetailState.collect {
                if (!it.loading && it.error == null && it.data != null) {
                    binding.comments.adapter =
                        CommentAdapter(it.data.comments ?: ArrayList(), {
                            Toast.makeText(
                                this@PostDetailActivity,
                                "댓글 인터렉션 오픈",
                                Toast.LENGTH_SHORT
                            ).show()
                        }, {
                            Toast.makeText(
                                this@PostDetailActivity,
                                "답글 쓰기",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                IDormLogger.i(this, "back pressed")
                super.onBackPressed()
                return true

            }
            R.id.postMenu -> {
                ListBottomSheet(
                    arrayListOf(
                        SelectItem("공유하기", "share", R.drawable.ic_share),
                        SelectItem("게시글 삭제", "delete", R.drawable.ic_delete),
                        SelectItem("게시글 수정", "edit", R.drawable.ic_edit),
                        SelectItem("신고하기", "report", desc = "idorm의 커뮤니티 가이드라인에 위배되는 게시글"),
                    )
                ) {
                    IDormLogger.i(this, it.value)
                }.show(supportFragmentManager, "TAG")
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }
}