package org.appcenter.inudorm.presentation.mypage.matching

import CheckableItem
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityLikedMateListBinding
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.adapter.RoomMateAdapter
import org.appcenter.inudorm.presentation.board.Content
import org.appcenter.inudorm.presentation.board.RadioButtonListBottomSheet
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState
import org.appcenter.inudorm.util.*

abstract class MateListActivity : AppCompatActivity() {

    abstract var mateAdapter: RoomMateAdapter
    private val mLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this).apply {
            orientation = RecyclerView.VERTICAL
        }
    }
    abstract val viewModel: MateListViewModel
    val binding: ActivityLikedMateListBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_liked_mate_list)
    }
    abstract val title: String
    fun handleMemberReport(memberId: Int) {
        val reasonTexts = resources.getStringArray(R.array.reportReasons1_text)
        val reasonValues = resources.getStringArray(R.array.reportReasons1_value)
        val reportReasons: ArrayList<CheckableItem> = arrayListOf()

        reasonValues.forEachIndexed { idx, value ->
            reportReasons.add(CheckableItem(value, reasonTexts[idx], false, false, ""))
        }
        RadioButtonListBottomSheet(reportReasons) { reasonType, reason ->
            if (reason.isNullOrEmpty()) return@RadioButtonListBottomSheet
            viewModel.reportMate(
                memberId,
                reason,
                reasonType ?: "",
                Content.MEMBER
            )
        }.show(
            this@MateListActivity.supportFragmentManager,
            ListBottomSheet.TAG
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mateList.apply {
            adapter = mateAdapter
            layoutManager = mLayoutManager
        }

        lifecycleScope.launch {
            viewModel.mateListState.collect(collector)
        }
        lifecycleScope.launch {
            viewModel.userMutationState.collect(userMutationCollector)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbarText.text = title


        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.getMates()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            android.R.id.home -> { // 메뉴 버튼, 사실상 백버튼으로 취급하면 됩니다!
                this.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val collector = FlowCollector<Sortable<UiState<ArrayList<MatchingInfo>>>> { value ->
        if (value.data.data == null && value.data.error != null) {
            // Todo: Handle Error
            IDormLogger.e(this@MateListActivity, value.toString())
            CustomDialog(
                value.data.error?.message ?: getString(R.string.unknownError),
                positiveButton = DialogButton("확인")
            ).show(this@MateListActivity)
        }
    }

    private val userMutationCollector = FlowCollector<UserMutationEvent> {
        IDormLogger.i(this, it.toString())
        when (it) {
            is UserMutationEvent.DeleteDislikedMatchingInfo,
            is UserMutationEvent.DeleteLikedMatchingInfo,
            -> {
                if (it.mutation.state.isSuccess()) viewModel.getMates()
                else if (it.mutation.state is State.Error) {
                    OkDialog((it.mutation.state as State.Error).error.message ?: "알 수 없는 오류입니다.")
                }
            }
            is UserMutationEvent.ReportMatchingInfo -> {}
            else -> {}
        }
    }

    fun openKakaoTalk(link: String) {
        CustomDialog(
            text = "상대의 카카오톡 오픈채팅으로 이동합니다.",
            positiveButton = DialogButton(
                "카카오톡으로 이동",
                icon = R.drawable.ic_kakaotalk_logo,
                onClick = {
                    IDormLogger.i(this, "Open link: $link")
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        startActivity(intent)
                    } catch (_: ActivityNotFoundException) {
                        Toast.makeText(
                            this,
                            getString(R.string.kakaoLinkNotFound),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                buttonType = ButtonType.Filled
            )
        ).show(this)
    }

}