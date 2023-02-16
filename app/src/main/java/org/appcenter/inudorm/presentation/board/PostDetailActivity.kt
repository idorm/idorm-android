package org.appcenter.inudorm.presentation.board

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityPostDetailBinding
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.adapter.CommentAdapter
import org.appcenter.inudorm.presentation.adapter.ImageViewAdapter
import org.appcenter.inudorm.presentation.component.ImageViewPager
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.IDormLogger
import org.joda.time.LocalDateTime

// Todo: 대댓글 작성 그림자 빼기
private val Context.dataStore by preferencesDataStore(name = "prefs")

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

    private fun sortComment(comments: ArrayList<Comment>, sort: String): ArrayList<Comment> {

        comments.sortByDescending {
            LocalDateTime.parse(it.createdAt)
        }
        comments.forEachIndexed { index, comment ->
            if (!comment.subComments.isNullOrEmpty()) {
                val subComments = comment.subComments!!
                subComments.sortByDescending { subComment ->
                    LocalDateTime.parse(subComment.createdAt)
                }
                comments[index] = comment.copy(subComments = subComments)
            }
        }
        if (sort == "asc")
            comments.reverse()
        return comments

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
                    if (it.data.isCommentEmpty()) {
                        binding.comments.visibility = View.GONE
                        binding.noComments.visibility = View.VISIBLE
                    } else {
                        binding.comments.visibility = View.VISIBLE
                        binding.noComments.visibility = View.GONE
                    }
                    val comments = if (it.data.isCommentEmpty()) ArrayList()
                    else sortComment(it.data.comments!!, viewModel.sortState.value)
                    binding.comments.adapter =
                        CommentAdapter(
                            comments,
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
                                    when (it.value) {
                                        "report" -> CustomDialog(
                                            "댓글을 신고하시겠습니까?",
                                            positiveButton = DialogButton("확인", onClick = {
                                                viewModel.reportComment(comment.commentId)
                                            }),
                                            negativeButton = DialogButton("취소")
                                        ).show(this@PostDetailActivity)

                                        "delete" -> CustomDialog(
                                            "댓글을 삭제하시겠습니까?",
                                            positiveButton = DialogButton("확인", onClick = {
                                                viewModel.deleteComment(
                                                    comment.commentId,
                                                    postId
                                                )
                                            }),
                                            negativeButton = DialogButton("취소")
                                        ).show(this@PostDetailActivity)
                                    }
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
                handleState(it)
            }
        }
        lifecycleScope.launch {
            viewModel.commentDeleteResult.collect {
                handleState(it)
            }
        }
        lifecycleScope.launch {
            viewModel.sortState.collect {
                val adapter = (binding.comments.adapter as CommentAdapter?)
                // Adapter가 null이 아니면 데이터 로드도 완료된 것
                if (adapter != null) {
                    adapter.dataSet = sortComment(adapter.dataSet, it)

                    adapter.notifyDataSetChanged()
                }

            }
        }

    }

    fun handleState(state: State?) {
        when (state) {
            is State.Success<*> -> {
                setLoadingState(false)
                // FIXME: 자식 RecyclerView 를 찾아가고, 해당 RecyclerView 에만 notify 해줘 최적화할 수 있습니다.
                viewModel.getPost(viewModel.postDetailState.value.data?.postId!!)
            }

            is State.Error -> {
                setLoadingState(false)
                // FIXME: 큰일남. 의존성 주입으로 PrefsRepository를 액세스해야 할 것 같음.
                UIErrorHandler.handle(this, PrefsRepository(this), state.error)
            }

            is State.Loading -> {
                setLoadingState(true)
            }

            else -> {}
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
                    when (it.value) {
                        "share" -> {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "텍스트 공유로 테스트. 앱링크로 대체해야 합니다.")
                                type = "text/plain"
                            }

                            val shareIntent = Intent.createChooser(sendIntent, null)
                            startActivity(shareIntent)

                        }

                        "delete" -> {
                            CustomDialog(
                                "게시글을 삭제하시겠습니까?",
                                positiveButton = DialogButton("확인", onClick = {
                                    viewModel.deletePost()
                                }),
                                negativeButton = DialogButton("취소")
                            ).show(this@PostDetailActivity)
                        }

                        "edit" -> {
                            val intent = Intent(
                                this@PostDetailActivity,
                                EditPostActivity::class.java
                            )
                            intent.putExtra("post", viewModel.postDetailState.value.data)
                            startActivity(intent)
                        }

                        "report" -> {
                            CustomDialog(
                                "게시글을 신고하시겠습니까?",
                                positiveButton = DialogButton("확인", onClick = {
                                    viewModel.reportPost()
                                }),
                                negativeButton = DialogButton("취소")
                            ).show(this@PostDetailActivity)
                        }

                    }
                }.show(supportFragmentManager, "TAG")
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }
}