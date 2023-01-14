package org.appcenter.inudorm.presentation.mypage

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
    private var prevState = MateListState("addedAtDesc", UiState())


    fun setupRecyclerView(recyclerViewToSetup: RecyclerView, onDetailOpen: () -> Unit) {
        mateAdapter = RoomMateAdapter(ArrayList(), onDetailOpen)
        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewToSetup.apply {
            adapter = mateAdapter
            layoutManager = mLayoutManager
        }
    }

    fun changeSort(sortBy: String) {
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
    }
}