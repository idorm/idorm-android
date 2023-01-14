package org.appcenter.inudorm.presentation.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityLikedMateListBinding
import org.appcenter.inudorm.util.WindowUtil.setStatusBarColor

class DisLikedMateListActivity : MateListActivity() {
    private val binding: ActivityLikedMateListBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_liked_mate_list)
    }

    private val viewModel: DisLikedMateListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(binding.mateList, binding.radioButton, binding.radioButton2) {
            // Todo: Option modal
        }


        binding.lifecycleOwner = this

        lifecycleScope.launch {
            viewModel.mateListState.collect(collector)
        }
        viewModel.getLikedMates()
        binding.radioButton.setOnClickListener {
            changeSort("addedAtDesc")
        }
        binding.radioButton2.setOnClickListener {
            changeSort("addedAtAsc")
        }

    }

}