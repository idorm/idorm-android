package org.appcenter.inudorm.presentation.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityMyMatchingProfileBinding
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.Gender
import org.appcenter.inudorm.model.JoinPeriod
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton

class MyMatchingProfileActivity : AppCompatActivity() {
    val binding: ActivityMyMatchingProfileBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_my_matching_profile)
    }
    val viewModel: MyMatchingProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this;
        binding.viewModel = viewModel;
        binding.lifecycleOwner = this;
        viewModel.getMatchingInfo()

        lifecycleScope.launch {
            viewModel.myPageState.collect {
                if (!it.loading && it.error != null) {
                    CustomDialog(
                        it.error.message ?: getString(R.string.unknownError),
                        positiveButton = DialogButton("확인")
                    ).show()
                }
            }
        }

    }

    fun editImage() {

    }
}