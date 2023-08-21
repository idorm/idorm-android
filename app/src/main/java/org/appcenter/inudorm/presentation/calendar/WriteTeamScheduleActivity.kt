package org.appcenter.inudorm.presentation.calendar

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWriteTeamScheduleBinding
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.model.TeamScheduleReq
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.matching.FILTER_RESULT_CODE
import org.appcenter.inudorm.presentation.matching.MatchingFragment
import org.appcenter.inudorm.presentation.mypage.matching.MyMatchingProfileActivity
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.OkDialog
import org.appcenter.inudorm.util.State

enum class TeamSchedulePurpose {
    Create,
    Edit
}

class WriteTeamScheduleActivity : LoadingActivity() {

    private lateinit var viewModel : WriteTeamScheduleViewModel
    private val binding: ActivityWriteTeamScheduleBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_write_team_schedule)
    }
    private val prefsRepository by lazy {
        PrefsRepository(this)
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

        lifecycleScope.launch {
            viewModel.teamScheduleMutationEvent.collect(){
                when(it) {
                    is TeamScheduleMutationEvent.CreateTeamSchedule -> {
                        if (it.mutation.state.isSuccess()) {
                            OkDialog("일정이 등록되었습니다.", onOk = {
                                val intent = Intent(applicationContext, CalendarFragment::class.java)
                                startActivity(intent)
                            })
                        }
                        if (it.mutation.state.isError()) {
                            UIErrorHandler.handle(
                                this@WriteTeamScheduleActivity,
                                prefsRepository,
                                (it.mutation.state as State.Error).error
                            ) { e ->
                                when (e.error) {
                                    ErrorCode.FIELD_REQUIRED -> {
                                        OkDialog(
                                            e.error.message,
                                            onOk = { },
                                            cancelable = false
                                        ).show(this@WriteTeamScheduleActivity)
                                    }
                                    ErrorCode.DUPLICATE_MATCHINGINFO -> {
                                        OkDialog(
                                            e.error.message,
                                            onOk = { },
                                            cancelable = false
                                        ).show(this@WriteTeamScheduleActivity)
                                    }
                                    else -> {
                                        OkDialog(getString(R.string.unknownError)).show(
                                            this@WriteTeamScheduleActivity
                                        )
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        OkDialog("알 수 없는 에러가 발생했습니다.")
                    }
                }
            }
        }
    }

}