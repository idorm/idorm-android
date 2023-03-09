package org.appcenter.inudorm.util.bindingadapter

import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.adapter.CommentListAdapter
import org.appcenter.inudorm.presentation.adapter.PostAdapter
import org.appcenter.inudorm.presentation.adapter.RoomMateAdapter
import org.appcenter.inudorm.presentation.component.MatchingCard
import org.appcenter.inudorm.presentation.mypage.matching.Sortable
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState

object MateListBinding {
    @JvmStatic
    @BindingAdapter(value = ["sortBy", "field"], requireAll = true)
    fun RadioButton.setState(
        sortBy: String,
        field: String?,
    ) {
        // Todo: sortBy에 따라 지정
        this.isChecked = sortBy == field
    }

    @JvmStatic
    @BindingAdapter("mateListState")
    fun RecyclerView.setMatchingItems(mateListState: Sortable<UiState<ArrayList<MatchingInfo>>>?) {
        if (mateListState != null && adapter is RoomMateAdapter && !mateListState.data.loading && mateListState.data.error == null) {
            val a = adapter as RoomMateAdapter
            a.dataSet.clear()
            a.dataSet.addAll(mateListState.data.data ?: arrayListOf())
            a.dataSet.sortBy { it.addedAt }
            if (mateListState.sortBy == "addedAtDesc") a.dataSet.reverse()
            a.notifyDataSetChanged()
        }
    }


    @JvmStatic
    @BindingAdapter("mate")
    fun MatchingCard.setMate(mate: MatchingInfo?) {
        if (mate != null)
            binding.mate = mate
    }
}