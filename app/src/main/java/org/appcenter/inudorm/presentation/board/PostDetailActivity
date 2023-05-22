package org.appcenter.inudorm.presentation.board

import CheckableItem
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.FeedTemplate
import kotlinx.coroutines.launch
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.OnSnackBarCallListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityPostDetailBinding
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.adapter.CommentAdapter
import org.appcenter.inudorm.presentation.adapter.ImageViewAdapter
import org.appcenter.inudorm.presentation.board.WritePostActivity.Companion.EDITOR_FINISHED
import org.appcenter.inudorm.presentation.component.ImageViewPager
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.*
import org.joda.time.LocalDateTime

// Todo: 대댓글 작성 그림자 빼기
private val Context.dataStore by preferencesDataStore(name = "prefs")

/**
 * 커뮤니티 글 보기 페이지에서 눌러서 들어가는 페이지. PostList
 */
class PostDetailActivity : LoadingActivity(), OnSnackBarCallListener {

    private val binding: ActivityPostDetailBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_post_detail)
    }
    private val viewModel: PostDetailViewModel by viewModels()
    private val imm: InputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private lateinit var bottomSheet: RadioButtonListBottomSheet

    fun writeComment() {
        imm.hideSoftInputFromWindow(binding.commentInput.windowToken, 0)
        val commentLength = viewModel.commentState.value.content.length
        if (commentLength in 1..50) { // Todo: SQL Injection Prevent
            viewModel.writeComment()
        } else {
            OkDialog("댓글 내용은 1~50자로 입력해주세요.", onOk = {
                if (commentLength > 50)
                    binding.commentInput.setText(binding.commentInput.text?.substring(0, 50))
            }, cancelable = false).show(this)
        }
    }

    private fun goBackAndRefresh() {
        setResult(DETAIL_FINISHED)
        finish()
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
        if (sort == "asc") {
            comments.reverse()
            comments.forEach {
                it.subComments?.reverse()
            }
        }
        return comments

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            EDITOR_FINISHED -> {
                viewModel.getPost(
                    viewModel.postDetailState.value.data?.postId
                        ?: throw java.lang.RuntimeException("올바르지 않은 게시글 번호.")
                )
            }
        }
    }

    private val handleReportComment = { comment: Comment ->
        // Todo: 신고 사유 모달
        val reasonTexts = resources.getStringArray(R.array.reportReasons1_text)
        val reasonValues = resources.getStringArray(R.array.reportReasons1_value)
        val reportReasons: ArrayList<CheckableItem> = arrayListOf()

        reasonValues.forEachIndexed { idx, value ->
            reportReasons.add(CheckableItem(value, reasonTexts[idx], false, false, ""))
        }
        bottomSheet = RadioButtonListBottomSheet(reportReasons) { reasonType, reason ->
            if (reason.isNullOrEmpty()) return@RadioButtonListBottomSheet
            viewModel.report(comment.commentId, Content.COMMENT, reasonType ?: "", reason)
        }
        bottomSheet.show(
            this@PostDetailActivity.supportFragmentManager,
            ListBottomSheet.TAG
        )
    }

    private val handleReportPost = { post: Post ->
        val reasonTexts = resources.getStringArray(R.array.reportReasons1_text)
        val reasonValues = resources.getStringArray(R.array.reportReasons1_value)
        val reportReasons: ArrayList<CheckableItem> = arrayListOf()

        reasonValues.forEachIndexed { idx, value ->
            reportReasons.add(CheckableItem(value, reasonTexts[idx], false, false, ""))
        }
        bottomSheet = RadioButtonListBottomSheet(reportReasons) { reasonType, reason ->
            if (reason.isNullOrEmpty()) return@RadioButtonListBottomSheet
            viewModel.report(post.postId, Content.POST, reasonType ?: "", reason)
        }
        bottomSheet.show(
            this@PostDetailActivity.supportFragmentManager,
            ListBottomSheet.TAG
        )

    }

    private val handleCommentMenuClicked = { comment: Comment ->
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
                "report" -> handleReportComment(comment)
                "delete" -> CustomDialog(
                    "댓글을 삭제하시겠습니까?",
                    positiveButton = DialogButton("확인", onClick = {
                        viewModel.deleteComment(
                            comment.commentId,
                            postId!!
                        )
                    }),
                    negativeButton = DialogButton("취소")
                ).show(this@PostDetailActivity)
            }
        }.show(supportFragmentManager, "TAG")
    }

    private var postId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = intent.getIntExtra("id", -9999)
        IDormLogger.d(this, postId.toString())
        binding.viewModel = viewModel
        binding.activity = this
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        if (postId == null) {
            OkDialog("글을 불러올 수 없습니다.", "오류", {
                finish()
            }, false)
            return
        }

        viewModel.getPost(postId!!)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getPost(postId!!)
        }
        binding.images.adapter = ImageViewAdapter(arrayListOf()) { idx, imageUrl ->
            val intent = Intent(this, ImageViewPager::class.java)
            intent.putStringArrayListExtra(
                "images",
                viewModel.postDetailState.value.data?.postPhotos?.map { it.photoUrl } as java.util.ArrayList<String>?
            )
            intent.putExtra("initialPosition", idx)
            startActivity(intent)
        }

        // FIXME: 워매 이딴 코드는 뭐여 ;;
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
                            onCommentInteractionOpened = handleCommentMenuClicked,
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
            viewModel.postDeleteResult.collect {
                handleState(it, true)
            }
        }
        lifecycleScope.launch {
            viewModel.commentDeleteResult.collect {
                handleState(it)
            }
        }
        lifecycleScope.launch {
            viewModel.postLikeResult.collect {
                handleState(it)
            }
        }
        lifecycleScope.launch {
            viewModel.postReportResult.collect {
                if (it.isSuccess()) {
                    OkDialog("신고가 완료됐어요. 불편을 드려서 죄송해요.", onOk = {
                        bottomSheet.dismiss()
                    }).show(this@PostDetailActivity)
                } else if (it is org.appcenter.inudorm.util.State.Error) {
                    OkDialog(it.error.message ?: "신고에 실패했어요. ", onOk = {
                        bottomSheet.dismiss()
                    }).show(this@PostDetailActivity)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.commentReportResult.collect {
                if (it.isSuccess()) {
                    OkDialog("신고가 완료됐어요. 불편을 드려서 죄송해요.", onOk = {
                        bottomSheet.dismiss()
                    }).show(this@PostDetailActivity)
                } else if (it is org.appcenter.inudorm.util.State.Error) {
                    OkDialog(it.error.message ?: "신고에 실패했어요. ", onOk = {
                        bottomSheet.dismiss()
                    }).show(this@PostDetailActivity)
                }
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


    private fun handleState(state: State?, goBack: Boolean = false) {
        when (state) {
            is State.Success<*> -> {
                setLoadingState(false)
                // FIXME: 자식 RecyclerView 를 찾아가고, 해당 RecyclerView 에만 notify 해줘 최적화할 수 있습니다.
                if (goBack) goBackAndRefresh()
                else viewModel.getPost(viewModel.postDetailState.value.data?.postId!!)

            }

            is State.Error -> {
                setLoadingState(false)
                // FIXME: 큰일남. 의존성 주입으로 PrefsRepository를 액세스해야 할 것 같음.
                UIErrorHandler.handle(this, PrefsRepository(this), state.error, handleIDormError = {
                    Toast.makeText(this@PostDetailActivity, it.message, Toast.LENGTH_SHORT).show()
                    if (it.error == ErrorCode.CANNOT_LIKED_SELF) viewModel.getPost(
                        postId ?: viewModel.postDetailState.value.data?.postId ?: -999
                    )
                })
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
                goBackAndRefresh()
                return true
            }

            R.id.postMenu -> {
                val menus = arrayListOf(
                    SelectItem("공유하기", "share", R.drawable.ic_share),
                    SelectItem("신고하기", "report", desc = "idorm의 커뮤니티 가이드라인에 위배되는 게시글"),
                )
                if (viewModel.userState.value.data?.memberId == viewModel.postDetailState.value.data?.memberId) {
                    menus.addAll(
                        1, arrayListOf(
                            SelectItem("게시글 삭제", "delete", R.drawable.ic_delete),
                            SelectItem("게시글 수정", "edit", R.drawable.ic_edit),
                        )
                    )
                }
                ListBottomSheet(
                    menus
                ) {
                    IDormLogger.i(this, it.value)
                    when (it.value) {
                        "share" -> {
                            val templateId = 93479L
                            val post = viewModel.postDetailState.value.data!!
                            val defaultProfileImage =
                                "https://idorm-static.s3.ap-northeast-2.amazonaws.com/profileImage.png"
                            val defaultThumbnailImage =
                                "https://idorm-static.s3.ap-northeast-2.amazonaws.com/nadomi.png"
                            val photo =
                                if (post.postPhotos == null || post.postPhotos.size < 1) defaultThumbnailImage
                                else post.postPhotos[0].photoUrl
                            val templateArgs = mapOf(
                                "title" to post.title,
                                "nickname" to (post.nickname ?: "탈퇴한 회원"),
                                "contentId" to post.postId.toString(),
                                "likeCount" to post.likesCount.toString(),
                                "summarizedContent" to post.content,
                                "thumbnail" to photo,
                                "userProfile" to (post.profileUrl ?: defaultProfileImage),
                                "commentCount" to post.commentsCount.toString()
                            )
                            if (ShareClient.instance.isKakaoTalkSharingAvailable(this)) {
                                // 카카오톡으로 카카오톡 공유 가능

                                ShareClient.instance.shareCustom(
                                    this,
                                    templateId,
                                    templateArgs = templateArgs
                                ) { sharingResult, error ->
                                    if (error != null) {
                                        IDormLogger.e(this, "카카오톡 공유 실패: ${error}")
                                    } else if (sharingResult != null) {
                                        IDormLogger.i(this, "카카오톡 공유 성공 ${sharingResult.intent}")
                                        startActivity(sharingResult.intent)

                                        // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                                        IDormLogger.d(
                                            this,
                                            "Warning Msg: ${sharingResult.warningMsg}"
                                        )
                                        IDormLogger.d(
                                            this,
                                            "Argument Msg: ${sharingResult.argumentMsg}"
                                        )
                                    }
                                }
                            } else {
                                val sharerUrl =
                                    WebSharerClient.instance.makeCustomUrl(templateId, templateArgs)
                                try {
                                    KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
                                } catch (e: UnsupportedOperationException) {
                                    // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
                                    try {
                                        KakaoCustomTabsClient.open(this, sharerUrl)
                                    } catch (e: ActivityNotFoundException) {
                                        // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
                                    }
                                }

                            }
                        }

                        "delete" -> {
                            if (viewModel.userState.value.data?.memberId == viewModel.postDetailState.value.data?.memberId) {
                                OkCancelDialog("게시글을 삭제할까요?") {
                                    viewModel.deletePost()
                                }.show(this@PostDetailActivity)
                            } else
                                OkDialog("게시글을 삭제할 권한이 없어요.").show(this@PostDetailActivity)
                        }

                        "edit" -> {
                            val intent = Intent(
                                this@PostDetailActivity,
                                EditPostActivity::class.java
                            )
                            intent.putExtra("post", viewModel.postDetailState.value.data)
                            startActivityForResult(intent, EDITOR_OPEN)
                        }

                        "report" -> {
                            handleReportPost(viewModel.postDetailState.value.data!!)
                        }

                    }
                }.show(supportFragmentManager, "TAG")
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val DETAIL_FINISHED = 3485
        const val EDITOR_OPEN = 7661
    }

    override fun onSnackBarCalled(message: String, duration: Int) {
        Snackbar.make(binding.root, message, duration)
            .apply {
                anchorView = binding.root
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).textAlignment =
                    View.TEXT_ALIGNMENT_CENTER
            }
            .show()
    }
}