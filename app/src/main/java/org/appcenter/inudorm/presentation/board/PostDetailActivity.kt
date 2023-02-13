package org.appcenter.inudorm.presentation.board

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityPostDetailBinding
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.adapter.CommentAdapter
import org.appcenter.inudorm.presentation.adapter.ImageViewAdapter
import org.appcenter.inudorm.presentation.component.ImageViewPager
import org.appcenter.inudorm.util.IDormLogger


/**
 * 커뮤니티 글 보기 페이지에서 눌러서 들어가는 페이지. PostList
 */
class PostDetailActivity : LoadingActivity() {

    private val binding: ActivityPostDetailBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_post_detail)
    }
    private val viewModel: PostDetailViewModel by viewModels()
    private val imm: InputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun writeComment() {
        imm.hideSoftInputFromWindow(binding.commentInput.windowToken, 0)
        viewModel.writeComment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postId = intent.getIntExtra("id", -9999)
        binding.viewModel = viewModel
        binding.activity = this
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
        binding.images.adapter = ImageViewAdapter(arrayListOf()) { idx, imageUrl ->
            val intent = Intent(this, ImageViewPager::class.java)
            intent.putStringArrayListExtra(
                "images",
                viewModel.postDetailState.value.data?.photoUrls
            )
            intent.putExtra("initialPosition", idx)
            startActivity(intent)
        }

        lifecycleScope.launch {
            viewModel.postDetailState.collect {
                if (!it.loading && it.error == null && it.data != null) {
                    binding.comments.adapter =
                        CommentAdapter(
                            it.data.comments ?: ArrayList(),
                            onCommentInteractionOpened = { comment ->
                                val menus = arrayListOf(
                                    SelectItem(
                                        "신고하기",
                                        "report",
                                        desc = "idorm의 커뮤니티 가이드라인에 위배되는 게시글"
                                    ),
                                )
                                if (viewModel.userState.value.data?.memberId == comment.memberId)
                                    menus.add(
                                        0, SelectItem("댓글 삭제", "delete", R.drawable.ic_delete),
                                    )

                                ListBottomSheet(menus) {
                                    IDormLogger.i(this, it.value)
                                }.show(supportFragmentManager, "TAG")
                            },
                            onWriteSubCommentClicked = { idx, comment ->
                                viewModel.setParentComment(comment.commentId)
                                binding.commentInput.requestFocus()
                                imm.showSoftInput(
                                    binding.commentInput,
                                    InputMethodManager.HIDE_IMPLICIT_ONLY
                                )
                                binding.comments.smoothScrollToPosition(idx)
                            }
                        )
                }
            }
        }
        lifecycleScope.launch {
            viewModel.commentWriteResult.collect {
                when (it) {
                    is State.Success<*> -> {
                        setLoadingState(false)
                        // FIXME: 자식 RecyclerView 를 찾아가고, 해당 RecyclerView 에만 notify 해줘 최적화할 수 있습니다.
                        viewModel.getPost(viewModel.postDetailState.value.data?.postId!!)
                    }
                    is State.Error -> {
                        setLoadingState(false)
                    }
                    is State.Loading -> {
                        setLoadingState(true)
                    }
                    else -> {}
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