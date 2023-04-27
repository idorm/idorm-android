package org.appcenter.inudorm.presentation.mypage.matching

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
import org.appcenter.inudorm.presentation.adapter.RoomMateAdapter
import org.appcenter.inudorm.presentation.matching.UserMutationEvent
import org.appcenter.inudorm.presentation.mypage.myinfo.UiState
import org.appcenter.inudorm.util.ButtonType
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.IDormLogger

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
                viewModel.getMates()
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