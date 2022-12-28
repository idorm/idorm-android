package org.appcenter.inudorm.util.bindingadapter

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.yuyakaido.android.cardstackview.CardStackView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.presentation.LoadMode
import org.appcenter.inudorm.presentation.MatchingState
import org.appcenter.inudorm.presentation.adapter.RoomMateAdapter
import org.appcenter.inudorm.util.IDormLogger

object MatchingBinding {
    private val TAG = "MatchingBinding"

    @JvmStatic
    @BindingAdapter("matchingState")
    fun CardStackView.bindData(uiState: MatchingState?) {
        if (uiState != null)
            if (adapter is RoomMateAdapter && !uiState.isLoading && uiState.errorMessage == null) {
                val a = adapter as RoomMateAdapter
                // 리스트에 추가하고
                val start = a.itemCount
                IDormLogger.i(this@MatchingBinding, "state mutated")
                if (uiState.loadMode == LoadMode.Paging) {
                    a.dataSet.addAll(uiState.mates.subList(start, uiState.mates.size))
                    // 추가됐다고 알려주기!
                    a.notifyItemRangeInserted(start, uiState.mates.size - start)
                } else if (uiState.loadMode == LoadMode.Update) {
                    a.dataSet.clear()
                    a.dataSet.addAll(uiState.mates)
                    a.notifyDataSetChanged()
                }

            }
    }

    @JvmStatic
    @BindingAdapter("matchingState")
    fun TextView.bindData(uiState: MatchingState?) {
        if (uiState != null) {
            visibility = if (uiState.isLoading) { // 로딩중
                View.GONE
            } else {
                View.VISIBLE
            }
            text = if (uiState.errorMessage != null) { // 에러 발생
                uiState.errorMessage
            } else { // 빈 데이터
                context.getString(R.string.noMoreRoomMates)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("matchingState")
    fun CircularProgressIndicator.bindData(uiState: MatchingState?) {
        if (uiState != null)
            visibility = if (uiState.isLoading) { // 로딩중
                View.VISIBLE
            } else {
                View.GONE
            }
    }

}