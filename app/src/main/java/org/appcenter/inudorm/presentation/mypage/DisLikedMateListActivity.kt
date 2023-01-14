package org.appcenter.inudorm.presentation.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityLikedMateListBinding
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton

class DisLikedMateListActivity : MateListActivity() {
    private val binding: ActivityLikedMateListBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_liked_mate_list)
    }

    private val viewModel: DisLikedMateListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView(binding.mateList) {
            // Todo: Option modal
        }

        binding.lifecycleOwner = this

        lifecycleScope.launch {
            viewModel.mateListState.collect(collector)
        }
        viewModel.getLikedMates()

    }

}