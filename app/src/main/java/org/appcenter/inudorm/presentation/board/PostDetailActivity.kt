package org.appcenter.inudorm.presentation.board

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityPostDetailBinding
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.presentation.ListBottomSheet
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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                super.onBackPressed()
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
            }
        }
        return super.onOptionsItemSelected(item)
    }
}