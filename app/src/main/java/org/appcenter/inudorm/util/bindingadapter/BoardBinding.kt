package org.appcenter.inudorm.util.bindingadapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.adapter.ImageViewAdapter
import org.appcenter.inudorm.presentation.adapter.PopularPostAdapter
import org.appcenter.inudorm.presentation.adapter.PostAdapter
import org.appcenter.inudorm.presentation.board.InfinityScrollState
import org.appcenter.inudorm.presentation.board.UploadableImage
import org.appcenter.inudorm.presentation.component.BoardProfile
import org.appcenter.inudorm.presentation.component.BoardProfileData
import org.appcenter.inudorm.presentation.matching.LoadMode
import org.appcenter.inudorm.util.IDormLogger
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

object BoardBinding {
    @JvmStatic
    @BindingAdapter("board")
    fun RecyclerView.bindBoard(uiState: InfinityScrollState<ArrayList<Post>>?) {
        if (uiState != null)
            if (adapter is PostAdapter && !uiState.loading && uiState.error == null && uiState.data != null) {
                IDormLogger.d(this, "${uiState.data.size} 개 로드")

                val a = adapter as PostAdapter
                // 리스트에 추가하고
                val start = a.itemCount
                if (uiState.loadMode == LoadMode.Paging) {
                    a.dataSet.addAll(uiState.data)
                    // 추가됐다고 알려주기!
                    a.notifyItemRangeInserted(start, uiState.data.size)
                } else if (uiState.loadMode == LoadMode.Update) {
                    a.dataSet.clear()
                    a.dataSet.addAll(uiState.data)
                    a.notifyDataSetChanged()
                }
            }
    }

    @JvmStatic
    @BindingAdapter("boardImages")
    fun RecyclerView.bindBoardImages(images: ArrayList<String>?) {
        if (adapter != null && (images?.size ?: 0) > 0) {
            val a = adapter as ImageViewAdapter
            bindImages(
                a,
                object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int = a.itemCount
                    override fun getNewListSize(): Int = images?.size!!

                    override fun areItemsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int,
                    ): Boolean =
                        a.imageList[oldItemPosition] == images!![newItemPosition]

                    override fun areContentsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int,
                    ): Boolean =
                        a.imageList[oldItemPosition] == images!![newItemPosition]
                },
                images!!
            )
        }
    }


    @JvmStatic
    @BindingAdapter("uploadableImages")
    fun RecyclerView.bindUploadableImages(images: ArrayList<UploadableImage>?) {
        if (adapter != null && (images?.size ?: 0) > 0) {
            val a = adapter as ImageViewAdapter
            bindImages(
                a,
                object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int = a.itemCount
                    override fun getNewListSize(): Int = images?.size!!

                    override fun areItemsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int,
                    ): Boolean =
                        a.imageList[oldItemPosition] == images!![newItemPosition].image.uri.toString()

                    override fun areContentsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int,
                    ): Boolean =
                        a.imageList[oldItemPosition] == images!![newItemPosition].image.uri.toString()
                },
                images?.map { it.image.uri.toString() } as ArrayList<String>
            )
        }
    }

    fun RecyclerView.bindImages(
        adapter: ImageViewAdapter,
        diffCallback: DiffUtil.Callback,
        imageUrls: ArrayList<String>,
    ) {
        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(adapter)
        adapter.imageList = imageUrls
        visibility = View.VISIBLE

    }

    @JvmStatic
    @BindingAdapter("topboard")
    fun RecyclerView.bindTopBoard(uiState: InfinityScrollState<ArrayList<Post>>?) {
        if (uiState != null)
            if (adapter is PopularPostAdapter && !uiState.loading && uiState.error == null && uiState.data != null) {
                IDormLogger.d(this, "로딩: ${uiState.loading}")
                val a = adapter as PopularPostAdapter
                // 리스트에 추가하고
                val start = a.itemCount
                if (uiState.loadMode == LoadMode.Paging) {
                    a.dataSet.addAll(uiState.data.subList(start, uiState.data.size))
                    // 추가됐다고 알려주기!
                    a.notifyItemRangeInserted(start, uiState.data.size - start)
                } else if (uiState.loadMode == LoadMode.Update) {
                    a.dataSet.clear()
                    a.dataSet.addAll(uiState.data)
                    a.notifyDataSetChanged()
                }

            }
    }

    @JvmStatic
    @BindingAdapter("timeElapsed")
    fun TextView.bindTimeElapsed(timeText: String?) {
        if (timeText != null) {
            val time = timeText.replace("T", " ")
            val dateTime =
                LocalDateTime.parse(time, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
                    .toDateTime(DateTimeZone.UTC)
            val currentDateTime =
                LocalDateTime.now().toDateTime(DateTimeZone.forID("Asia/Seoul"))
            val dateString =
                LocalDateTime.parse(time, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
                    .plusHours(9).toString(DateTimeFormat.forPattern("MM월 dd일"))

            val elapsed = (currentDateTime.millis - dateTime.millis) / 1000
            if (elapsed < -10)
                assert(false) { "시간 계산 오류 $dateTime | $currentDateTime" }

            if (elapsed < 60) {
                // 1분 이하
                setText("${elapsed}초전")
                return
            } else if (elapsed < 60 * 60) {
                // 1시간 이하
                setText("${elapsed / 60}분전")
                return
            } else if (elapsed < 60 * 60 * 24) {
                // 1일 이하
                setText("${elapsed / 60 / 60}시간전")
                return
            } else if (elapsed < 60 * 60 * 24 * 7) {
                // 7일 이하
                setText("${elapsed / 60 / 60 / 24}일전")
                return
            } else if (elapsed < 60 * 60 * 24 * 31) {
                // 한달 이하
                setText("${elapsed / 60 / 60 / 24 / 7}주일전")
                return
            } else setText(dateString)
        }
    }

    @JvmStatic
    @BindingAdapter("post")
    fun BoardProfile.bindPost(post: Post?) {
        if (post != null) {
            binding.profile = BoardProfileData(
                post.nickname,
                post.createdAt,
                post.profileUrl ?: "",
            )
        }
    }


    @JvmStatic
    @BindingAdapter("comment")
    fun BoardProfile.bindComment(comment: Comment?) {
        if (comment != null) {
            binding.profile = BoardProfileData(
                comment.nickname,
                comment.createdAt,
                comment.profileUrl ?: "",
            )
        }
    }


    @JvmStatic
    @BindingAdapter("loading")
    fun SwipeRefreshLayout.setLoadingAction(loading: Boolean?) {
        isRefreshing = loading ?: false
    }
}