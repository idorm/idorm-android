package org.appcenter.inudorm.presentation.mypage

import SelectItem
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityLikedMateListBinding
import org.appcenter.inudorm.presentation.ListBottomSheet


class LikedMateListActivity : MateListActivity() {
    private val binding: ActivityLikedMateListBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_liked_mate_list)
    }

    private val viewModel: LikedMateListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(binding.mateList, binding.radioButton, binding.radioButton2) { id ->
            // Todo: Option modal
            val items = arrayListOf(
                SelectItem("룸메이트와 채팅하기", "chat", R.drawable.ic_chat),
                SelectItem("좋아요한 룸메에서 삭제", "delete", R.drawable.ic_delete),
                SelectItem("신고하기", "report", desc = "idorm의 커뮤니티 가이드라인에 위배되는 게시글")
            )
            val bottomSheet = ListBottomSheet(items) {
                when (it.value) {
                    "chat" -> {
                        //Todo: Chat, OnClick Listener 파라미터를 item 자체로 변경해서 카톡링크 받기
                    }
                    "delete" -> {
                        viewModel.deleteLikedMate(id)
                    }
                    "report" -> {
                        viewModel.reportMate(id)
                    }
                }
            }.show(
                supportFragmentManager,
                ListBottomSheet.TAG
            )
        }
        binding.lifecycleOwner = this
        binding.toolbarText.text = "좋아요한 룸메"
        binding.radioButton.setOnClickListener {
            changeSort("addedAtDesc")
        }
        binding.radioButton2.setOnClickListener {
            changeSort("addedAtAsc")
        }
        lifecycleScope.launch {
            viewModel.mateListState.collect(collector)
        }
        viewModel.getLikedMates()
    }
}