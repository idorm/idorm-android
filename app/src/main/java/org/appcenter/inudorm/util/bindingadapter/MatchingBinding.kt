package org.appcenter.inudorm.util.bindingadapter

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.yuyakaido.android.cardstackview.CardStackView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.model.Taste
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

    @JvmStatic
    @BindingAdapter(value = ["feature", "have"], requireAll = true)
    fun Chip.setDislikedFeature(feature: String?, have: Boolean?) {
        if (feature != null && have != null) {
            val taste = Taste.fromKey(feature)
            val red = ContextCompat.getColor(context, R.color.iDorm_red)
            val blue = ContextCompat.getColor(context, R.color.iDorm_blue)
            val booleanText = if (have) taste?.isFit?.trueText else taste?.isFit?.falseText
            val alteredHave = if (feature == "isWearEarphones") !have else have
            val booleanColor = if (alteredHave) red else blue

            text = buildCardText(taste?._name!!, booleanText!!, booleanColor)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["field", "fieldContent"], requireAll = true)
    fun Chip.setMyInfo(
        field: String?,
        fieldContent: String?
    ) {
        if (field != null && fieldContent != null) {
            //val fieldString = context?.getString(field)!!
            text = buildCardText(field, fieldContent, Color.BLACK)
        }
    }

    @JvmStatic
    fun buildCardText(
        name: String,
        value: String,
        valueColor: Int = Color.BLACK
    ): SpannableStringBuilder {
        return SpannableStringBuilder()
            .bold { append(name) }
            .color(valueColor) { append(" $value") }
    }
}

