package org.appcenter.inudorm.presentation.calendar

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWriteTeamScheduleBinding
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.model.TeamScheduleReq
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.adapter.TeamProfileAdapter
import org.appcenter.inudorm.presentation.matching.FILTER_RESULT_CODE
import org.appcenter.inudorm.presentation.matching.MatchingFragment
import org.appcenter.inudorm.presentation.mypage.matching.MyMatchingProfileActivity
import org.appcenter.inudorm.repository.CalendarRepository
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.OkDialog
import org.appcenter.inudorm.util.State

enum class TeamSchedulePurpose {
    Create,
    Edit
}

class WriteTeamScheduleActivity : LoadingActivity() {

    private lateinit var viewModel: WriteTeamScheduleViewModel
    private val binding: ActivityWriteTeamScheduleBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_write_team_schedule)
    }
    private val prefsRepository by lazy {
        PrefsRepository(this)
    }

    var roomMateTeam = arrayListOf<TeamProfile>()
    private val calendarRepository by lazy {
        CalendarRepository()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_team_schedule)

        val purpose = intent.getSerializableExtra("purpose") as TeamSchedulePurpose
        val teamCalendarId = intent.getIntExtra("id", 0)

        viewModel = ViewModelProvider(
            viewModelStore,
            TeamScheduleViewModelFactory(purpose)
        )[WriteTeamScheduleViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.teamProfileRecycler.adapter = TeamProfileAdapter(arrayListOf(), true)

        lifecycleScope.launch {
            viewModel.roomMateTeam.collect {
                setLoadingState(it)
                if (it is State.Success) {
                    roomMateTeam.clear()
                    roomMateTeam.addAll(it.data?.members?.map { member ->
                        member.copy(
                            hasInvitedToSchedule = false
                        )
                    }!!)
                }
            }
        }

        viewModel.getRoomMates()


        binding.deleteButton.setOnClickListener {
            //ToDo : 일정 삭제 연결
        }

        binding.doneButton.setOnClickListener {
            val title = binding.title.text.toString()
            val content = binding.content.text.toString()
            viewModel.submit(
                TeamScheduleReq(
                    content = content,
                    endDate = "2023-08-20",
                    endTime = "16:00:00",
                    startDate = "2023-08-20",
                    startTime = "15:00:00",
                    targets = (binding.teamProfileRecycler.adapter as TeamProfileAdapter).dataSet
                        .filter { it.hasInvitedToSchedule == true }
                        .map { it.memberId!! },
                    title = title
                )
            )
        }



        lifecycleScope.launch {
            viewModel.teamScheduleMutationEvent.collect() {
                if (roomMateTeam.isNotEmpty())
                    when (it) {
                        is TeamScheduleMutationEvent.CreateTeamSchedule -> {
                            if (it.mutation.state.isSuccess()) {
                                finish()
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

                                        else -> {
                                            OkDialog(
                                                e.error.message,
                                                onOk = { },
                                                cancelable = false
                                            ).show(this@WriteTeamScheduleActivity)
                                        }
                                    }
                                }
                            }
                        }

                        is TeamScheduleMutationEvent.EditTeamSchedule -> {
                            if (it.mutation.state.isSuccess()) {
                                OkDialog("일정이 수정되었습니다.", onOk = {
                                    val intent =
                                        Intent(applicationContext, CalendarFragment::class.java)
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

                                        else -> {
                                            OkDialog(
                                                e.error.message,
                                                onOk = { },
                                                cancelable = false
                                            ).show(this@WriteTeamScheduleActivity)
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

    private suspend fun initData(purpose: TeamSchedulePurpose, teamCalendarId: Long) {
        if (purpose == TeamSchedulePurpose.Edit) {
            val teamScheduleData = calendarRepository.getTeamSchedule(teamCalendarId)
            binding.title.setText(binding.title.text)
            binding.content.setText(binding.content.text)
            IDormLogger.i(this, binding.endDate.toString() + "눌러짐")

        }
    }

}