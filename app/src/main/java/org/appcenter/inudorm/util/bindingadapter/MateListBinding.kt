package org.appcenter.inudorm.util.bindingadapter

import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import org.appcenter.inudorm.presentation.account.prompt.PasswordState
import org.appcenter.inudorm.presentation.adapter.RoomMateAdapter
import org.appcenter.inudorm.presentation.matching.LoadMode
import org.appcenter.inudorm.presentation.mypage.MateListState
import org.appcenter.inudorm.util.IDormLogger

object MateListBinding {
    @JvmStatic
    @BindingAdapter(value = ["mateListState", "field"], requireAll = true)
    fun RadioButton.setState(mateListState: MateListState?, field: String?) {
        // Todo: sortBy에 따라 지정
        this.isChecked = mateListState?.sortBy == field
    }

    @JvmStatic
    @BindingAdapter("mateListState")
    fun RecyclerView.setMatchingItems(mateListState: MateListState?) {
        if (mateListState != null && adapter is RoomMateAdapter && !mateListState.mates.loading && mateListState.mates.error == null) {
            val a = adapter as RoomMateAdapter
            a.dataSet.clear()
            a.dataSet.addAll(mateListState.mates.data ?: arrayListOf())
            a.dataSet.sortBy { it.addedAt }
            if (mateListState.sortBy == "addedAtDesc") a.dataSet.reverse()
            a.notifyDataSetChanged()
        }
    }
}