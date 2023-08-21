package org.appcenter.inudorm.presentation.calendar

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWriteTeamScheduleBinding
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.model.TeamScheduleReq
import org.appcenter.inudorm.presentation.matching.FilterViewModel
import org.appcenter.inudorm.presentation.matching.FilterViewModelFactory
import org.appcenter.inudorm.presentation.onboard.BaseInfoPurpose
import org.appcenter.inudorm.util.IDormLogger

enum class TeamSchedulePurpose {
    Create,
    Edit
}

class WriteTeamScheduleActivity : LoadingActivity() {

    private lateinit var viewModel : WriteTeamScheduleViewModel
    private val binding: ActivityWriteTeamScheduleBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_write_team_schedule)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_team_schedule)

        val purpose = intent.getSerializableExtra("purpose") as TeamSchedulePurpose

        viewModel = ViewModelProvider(
            viewModelStore,
            TeamScheduleViewModelFactory(purpose)
        )[WriteTeamScheduleViewModel::class.java]

        val teamProfile : List<TeamProfile> = mutableListOf(
            TeamProfile(
                order = 1,
                memberId = 12,
                nickname = "슈룹",
                profilePhotoUrl = null
            ))


        binding.doneButton.setOnClickListener{
            IDormLogger.d(this, "눌렸음2" + purpose.toString())

            viewModel.submit(
                TeamScheduleReq(
                    content = "방 청소만 하는게 아니라 화장실거울까지!",
                    endDate = "2023-08-27",
                    endTime = "16:00:00",
                    startDate = "2023-08-27",
                    startTime = "15:00:00",
                    targets = teamProfile,
                    title = "청소"

                )
            )
        }
    }

}