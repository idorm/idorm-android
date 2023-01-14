package org.appcenter.inudorm.presentation.mypage

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


class LikedMateListActivity : MateListActivity() {
    private val binding: ActivityLikedMateListBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_liked_mate_list)
    }

    private val viewModel : LikedMateListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(binding.mateList, binding.radioButton, binding.radioButton2) {
            // Todo: Option modal
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