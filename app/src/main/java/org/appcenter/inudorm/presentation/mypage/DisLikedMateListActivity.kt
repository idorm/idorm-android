package org.appcenter.inudorm.presentation.mypage

import SelectItem
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityLikedMateListBinding
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.adapter.RoomMateAdapter
import org.appcenter.inudorm.util.WindowUtil.setStatusBarColor

class DisLikedMateListActivity : MateListActivity() {
    override var mateAdapter: RoomMateAdapter = RoomMateAdapter(false, ArrayList()) { mate ->
        // Todo: Option modal
        val items = arrayListOf(
            SelectItem(getString(R.string.chatWithMate), "chat", R.drawable.ic_chat),
            SelectItem(getString(R.string.removeDislikedMate), "delete", R.drawable.ic_delete),
            SelectItem(getString(R.string.report), "report", desc = getString(R.string.matchingImageViolence))
        )
        ListBottomSheet(items) {
            when (it.value) {
                "chat" -> openKakaoTalk(mate.openKakaoLink)
                "delete" -> viewModel.deleteMate(mate.memberId)
                "report" -> viewModel.reportMate(mate.memberId)
            }
        }.show(
            supportFragmentManager,
            ListBottomSheet.TAG
        )
    }
    override val viewModel: DisLikedMateListViewModel by viewModels()
    override val title: String by lazy {
        getString(R.string.disLikedMates)
    }
}