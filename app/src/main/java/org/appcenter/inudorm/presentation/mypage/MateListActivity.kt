package org.appcenter.inudorm.presentation.mypage

import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.FlowCollector
import org.appcenter.inudorm.presentation.adapter.RoomMateAdapter
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.IDormLogger

abstract class MateListActivity : AppCompatActivity() {

    lateinit var mateAdapter: RoomMateAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    var prevState = MateListState("addedAtDesc", UiState())
    lateinit var mRadioButton1: RadioButton
    lateinit var mRadioButton2: RadioButton

    fun initView(
        recyclerViewToSetup: RecyclerView,
        radioButton1: RadioButton,
        radioButton2: RadioButton,
        onDetailOpen: () -> Unit
    ) {
        mRadioButton1 = radioButton1
        mRadioButton2 = radioButton2
        mateAdapter = RoomMateAdapter(ArrayList(), onDetailOpen)
        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewToSetup.apply {
            adapter = mateAdapter
            layoutManager = mLayoutManager
        }
    }

    fun changeSort(sortBy: String) {
        IDormLogger.i(this@MateListActivity, "sort changing")
        mRadioButton1.isChecked = (sortBy == "addedAtDesc")
        mRadioButton2.isChecked = (sortBy == "addedAtAsc")
        mateAdapter.dataSet.sortBy { it.addedAt }
        if (sortBy == "addedAtDesc") mateAdapter.dataSet.reverse()
        mateAdapter.notifyDataSetChanged()
    }

    val collector = FlowCollector<MateListState> { value ->
        if (value.mates.data != null && prevState.mates != value.mates) {
            mateAdapter.dataSet.clear()
            mateAdapter.dataSet.addAll(value.mates.data ?: ArrayList())
            mateAdapter.notifyDataSetChanged()
        } else if (value.mates.data == null && value.mates.error != null) {
            // Todo: Handle Error
            IDormLogger.e(this@MateListActivity, value.toString())
            CustomDialog(
                value.mates.error?.message ?: "알 수 없는 에러가 발생했습니다.",
                positiveButton = DialogButton("확인")
            ).show(this@MateListActivity)
        }
        prevState = value
    }
}