package org.appcenter.inudorm.presentation.calendar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWriteTeamScheduleBinding
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.model.TeamScheduleReq
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.adapter.TeamProfileAdapter
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.*
import java.util.*

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



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_team_schedule)

        val purpose = intent.getSerializableExtra("purpose") as TeamSchedulePurpose
        val teamCalendarId = intent.getIntExtra("teamCalendarId",0)


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

        //팀 조회
        lifecycleScope.launch {
            if(purpose == TeamSchedulePurpose.Edit){
                viewModel.teamSchedule.collect {
                    setLoadingState(it)
                    if(it is State.Success){
                        IDormLogger.i(this, "팀일정 조회 성공")
                        val startDate = it.data?.startDate
                        val endDate = it.data?.startDate


                        binding.title.setText(it.data?.title)
                        binding.content.setText(it.data?.content)
                        binding.startDate.text = startDate?.substring(5 until 7) + "월" + startDate?.substring(8 until 10) + "일"
                        binding.startTime.text = changeTime(it.data?.startTime.toString())
                        binding.endDate.text = endDate?.substring(5 until 7) + "월" + endDate?.substring(8 until 10) + "일"
                        binding.endTime.text = changeTime(it.data?.endTime.toString())
                    }
                    if(it is State.Error){
                        UIErrorHandler.handle(
                            this@WriteTeamScheduleActivity,
                            prefsRepository,
                            it.error
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
            }
        }

        viewModel.getTeamSchedule(teamCalendarId)



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


    private fun changeDateToDay(date : String?): String {
        val cal: Calendar = Calendar.getInstance()
        val dayNum: Int = cal.get(Calendar.DAY_OF_WEEK)
        val dateList = mutableListOf<String>("", "일", "월", "화", "수", "목", "금", "토")

        return dateList[dayNum]
    }

    private fun changeTime(time : String) : String {
        val hourString : String
        val minutesString : String
        val hour = time.substring(0..1).toInt()
        val minutes = time.substring(3..4).toInt()
        hourString = if(hour < 10) ("오전 0${hour}시 ")
                    else if(hour < 12) ("오전 ${hour}시 ")
                    else if(hour == 12) ("오후 12시 ")
                    else ("오후 ${hour-12}시 ")


        minutesString = if(minutes < 10) ("0${minutes}분")
        else ("${minutes}분")


        return hourString + minutesString
    }



}