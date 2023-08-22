package org.appcenter.inudorm.presentation.calendar

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.MonthScrollListener
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentCalendarBinding
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.LoadingFragment
import org.appcenter.inudorm.presentation.account.LoginActivity
import org.appcenter.inudorm.presentation.adapter.CalendarAdapter
import org.appcenter.inudorm.presentation.adapter.TeamProfileAdapter
import org.appcenter.inudorm.presentation.adapter.TeamScheduleAdapter
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.usecase.AcceptInvitation
import org.appcenter.inudorm.usecase.LoginRefresh
import org.appcenter.inudorm.util.ButtonType
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.ImageUri
import org.appcenter.inudorm.util.KakaoShare
import org.appcenter.inudorm.util.OkCancelDialog
import org.appcenter.inudorm.util.OkDialog
import org.appcenter.inudorm.util.State
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

val mateColors = listOf(
    R.color.iDorm_purple,
    R.color.iDorm_yellow3,
    R.color.iDorm_green2,
    R.color.iDorm_pink
)

class CalendarFragment : LoadingFragment() {

    companion object {
        fun newInstance() = CalendarFragment()
    }

    private lateinit var viewModel: CalendarViewModel
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var teamProfileAdapter: TeamProfileAdapter
    private lateinit var teamScheduleAdapter: TeamScheduleAdapter
    private lateinit var officialScheduleAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)

        initBind()
        binding.more.setOnClickListener {
            openRoomMenu()
        }
        binding.end.setOnClickListener {
            setManageMode(false)
        }
        return binding.root
    }

    private fun setManageMode(enabled: Boolean) {
        binding.more.visibility = if (enabled) View.GONE else View.VISIBLE
        binding.end.visibility = if (enabled) View.VISIBLE else View.GONE
        if (!enabled) {
            teamScheduleAdapter.setManageMode(false)
            teamProfileAdapter.setManageMode(false)
        }
    }

    private fun openRoomMenu() {
        ListBottomSheet(
            arrayListOf(
                SelectItem("친구 관리", "mateManage"),
                SelectItem("일정 관리", "scheduleManage"),
                SelectItem("룸메이트 초대해서 일정 공유하기", "invite"),
                SelectItem("일정 공유 캘린더 나가기", "leave"),
            )
        ) {
            when (it.value) {
                "mateManage" -> {
                    // Todo: Toggle mate manage mode to kick out
                    setManageMode(true)
                    teamProfileAdapter.setManageMode(true)
                }

                "scheduleManage" -> {
                    // Todo: Toggle schedule manage mode to delete schedules
                    setManageMode(true)
                    teamScheduleAdapter.setManageMode(true)
                }

                "invite" -> {
                    if ((viewModel.roomMateTeam.value as State.Success).data?.members?.size == 4) {
                        OkDialog("룸메이트는 최대 4명까지 일정을 공유할 수 있습니다.").show(requireContext())
                        return@ListBottomSheet
                    }
                    if (viewModel.userState.value is State.Success) {
                        CustomDialog(
                            text = "룸메이트 초대 링크를 보내시겠습니까?",
                            positiveButton = DialogButton(
                                "카카오톡으로 이동",
                                icon = R.drawable.ic_kakaotalk_logo,
                                onClick = {
                                    val user = (viewModel.userState.value as State.Success).data!!
                                    val templateId = 97215L
                                    val templateArgs = mapOf(
                                        "senderNickNm" to user.nickname,
                                        "userProfile" to (user.profilePhotoUrl
                                            ?: ImageUri.defaultProfileImage),
                                        "inviter" to user.memberId.toString()
                                    )
                                    KakaoShare.share(requireContext(), templateId, templateArgs)
                                },
                                buttonType = ButtonType.Filled
                            )
                        ).show(requireContext())


                    }
                }

                "leave" -> {
                    OkCancelDialog("일정 공유 캘린더에서 나갈 시 데이터가 모두 사라집니다.", onOk = {
                        // Todo: Leave

                    }).show(requireContext())
                }
            }
        }.show(childFragmentManager, "TAG")
    }

    private fun initBind() {
        teamProfileAdapter = TeamProfileAdapter(arrayListOf(), false) {
            OkCancelDialog(
                "룸메이트 목록에서 삭제하시겠습니까?",
                onOk = {
                    // Todo: delete
                },
            ).show(requireContext())
        }
        teamScheduleAdapter = TeamScheduleAdapter(arrayListOf(), {
            val intent = Intent(requireContext(), WriteTeamScheduleActivity::class.java)
            intent.putExtra("purpose", TeamSchedulePurpose.Edit)
            intent.putExtra("teamCalendarId", it.teamCalendarId)
            startActivity(intent)
        }) {
            OkCancelDialog(
                "일정을 삭제하시겠습니까?",
                onOk = {
                    // Todo: delete
                },
            ).show(requireContext())
        }
        officialScheduleAdapter = CalendarAdapter(arrayListOf()) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
        }

        binding.teamProfileRecycler.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)

        binding.teamCalendarRecycler.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

        binding.officialEvents.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

        with(binding) {
            teamProfileRecycler.adapter = teamProfileAdapter
            teamCalendarRecycler.adapter = teamScheduleAdapter
            officialEvents.adapter = officialScheduleAdapter
        }
    }

    private var menuExtended = false

    private fun setExtended(extended: Boolean) {
        if (extended) {
            binding.registerSleepover.show()
            binding.registerTeamSchedule.show()
            binding.registerSleepover.visibility = View.VISIBLE
            binding.registerTeamSchedule.visibility = View.VISIBLE
        } else {
            binding.registerSleepover.hide()
            binding.registerTeamSchedule.hide()
            binding.registerSleepover.visibility = View.GONE
            binding.registerTeamSchedule.visibility = View.GONE
        }
        menuExtended = extended
    }

    private fun toggleExtended() {
        setExtended(!menuExtended)
    }

    private fun setCurrentMonth(date: CalendarMonth) {
        val date = LocalDate.of(date.yearMonth.year, date.yearMonth.monthValue, 1)
        viewModel.getSchedules(date)
        viewModel.getOfficialSchedules(date)
        viewModel.getRoomMates()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        binding.lifecycleOwner = this.requireActivity()
        binding.viewModel = viewModel
        viewModel.selectedDay.observe(binding.lifecycleOwner!!) {
            IDormLogger.i(this, it.toString())
        }
        viewModel.getUser()

        val inviter = arguments?.getInt("inviter")
        IDormLogger.i(this, "초대자 id: $inviter 입니다!")
        lifecycleScope.launch {
            if (inviter != null) {
                setLoadingState(true)
                val memberId = kotlin.runCatching {
                    LoginRefresh().run(null).memberId
                }.onFailure {
                    PrefsRepository(requireContext()).signOut()
                    setLoadingState(false)
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }.getOrNull()
                if (memberId != null) {
                    val result = AcceptInvitation().run(memberId)
                    setLoadingState(false)
                    if (result.isSuccess()) {
                        OkDialog("초대가 수락되었습니다.").show(requireContext())
                    } else if (result.isError()) {
                        OkDialog((result as State.Error).error.message ?: "알 수 없는 오류입니다.").show(
                            requireContext()
                        )
                    }
                }

            }

            // Todo: 현재 날짜로 변경
            setCurrentMonth(CalendarMonth(YearMonth.now(), arrayListOf()))
            setExtended(false)
            binding.registerSchedule.setOnClickListener {
                toggleExtended()
            }
            binding.registerTeamSchedule.setOnClickListener {
                val intent = Intent(requireContext(), WriteTeamScheduleActivity::class.java)
                intent.putExtra("purpose", TeamSchedulePurpose.Create)
                startActivity(intent)
            }
            binding.registerSleepover.setOnClickListener {
                val intent = Intent(requireContext(), WriteSleepoverScheduleActivity::class.java)
                startActivity(intent)
            }
            lifecycleScope.launch {
                viewModel.userState.collect {
                    setLoadingState(it)
                }
            }
            lifecycleScope.launch {
                viewModel.schedules.collect {
                    setLoadingState(it)
                    /**
                     * yyyy-MM-dd -> TeamProfile[] 인 map. 날짜별 팀 해당하는 팀 멤버를 보관힙니다.
                     */
                    if (it is State.Success) {
                        val dateMap = mutableMapOf<String, List<TeamProfile>>()
                        it.data?.forEach { schedule ->
                            dateMap.merge(
                                schedule.startDate!!,
                                schedule.targets
                            ) { prev, curr -> prev + curr }
                        }
                        dateMap.forEach { (t, u) ->
                            dateMap[t] =
                                u.groupBy { member -> member.memberId }
                                    .map { entry -> entry.value[0] }
                        }

                        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
                            // Called only when a new container is needed.
                            override fun create(view: View) = DayViewContainer(view)

                            // Called every time we need to reuse a container.
                            override fun bind(container: DayViewContainer, data: CalendarDay) {
                                container.textView.text = data.date.dayOfMonth.toString()
                                if (data.position != DayPosition.MonthDate) {
                                    container.textView.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.iDorm_gray_200
                                        )
                                    )
                                } else {
                                    when (data.date) {
                                        LocalDate.now() -> container.view.setBackgroundResource(R.drawable.ic_today)
                                        viewModel.selectedDay.value!! -> {
                                            container.view.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.iDorm_blue
                                                )
                                            )
                                        }

                                        else
                                            // 아마 RecyclerView를 이용하는 특성상 몇개 캘린더를 재활용하는 형태라서 위에서 setBackgroundResource를 하면 4개월 주기로 다른 날에도 뜸
                                        -> container.view.setBackgroundColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.white
                                            )
                                        )
                                    }

                                    val teamProfiles =
                                        dateMap[data.date.toString()]?.sortedBy { profile -> profile.order }

                                    if (teamProfiles.isNullOrEmpty()) return
                                    val layout = LinearLayout(requireContext())
                                    layout.apply {
                                        val lp = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT
                                        )
                                        layoutParams = lp
                                        id = ViewCompat.generateViewId()
                                        gravity = Gravity.BOTTOM
                                        setHorizontalGravity(Gravity.CENTER_HORIZONTAL)

                                    }
                                    (container.view as ViewGroup).apply {
                                        addView(layout)
                                    }


                                    teamProfiles.forEach { teamProfile ->
                                        val dot = LinearLayout(requireContext())
                                        dot.apply {
                                            val lp = LinearLayout.LayoutParams(
                                                20, 20
                                            )
                                            lp.setMargins(3, 0, 3, 10)
                                            layoutParams = lp
                                            id = ViewCompat.generateViewId()
                                            backgroundTintList = ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    mateColors[teamProfile.order!!]
                                                )
                                            )
                                            setBackgroundResource(
                                                R.drawable.ic_dot_gray_400
                                            )
                                        }

                                        layout.addView(dot)

                                    }
                                }
                            }
                        }
                    }
                }
            }



            lifecycleScope.launch {
                viewModel.roomMateTeam.collect {
                    if (it is State.Success && it.data?.members?.size == 1) {
                        binding.invite.visibility = View.VISIBLE
                    }
                    setLoadingState(it)
                }
            }

            lifecycleScope.launch {
                viewModel.officialSchedules.collect {
                    setLoadingState(it)
                }
            }
            // Dummy 입니다!!! 무 조 건 데이터 들어오고 나서 새로 바인딩 하는 곳에다가 작업하세요!!
            binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
                // Called only when a new container is needed.
                override fun create(view: View) = DayViewContainer(view)

                // Called every time we need to reuse a container.
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.textView.text = data.date.dayOfMonth.toString()
                    if (data.position != DayPosition.MonthDate) {
                        container.textView.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.iDorm_gray_200
                            )
                        )
                    } else when (data.date) {
                        LocalDate.now() -> {
                            container.view.setBackgroundResource(R.drawable.ic_today)
                        }
                    }
                }
            }

            binding.calendarView.monthHeaderBinder =
                object : MonthHeaderFooterBinder<MonthViewContainer> {

                    override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                        val prev = container.titlesContainer.findViewById<ImageView>(R.id.prev)
                        val next = container.titlesContainer.findViewById<ImageView>(R.id.next)
                        val monthTitle =
                            container.titlesContainer.findViewById<TextView>(R.id.monthTitle)

                        prev.setOnClickListener {
                            binding.calendarView.scrollToMonth(data.yearMonth.previousMonth)
                        }
                        next.setOnClickListener {
                            binding.calendarView.scrollToMonth(data.yearMonth.nextMonth)
                        }
                        monthTitle.text = "${data.yearMonth.monthValue}월"

                        container.titlesContainer.findViewById<LinearLayout>(R.id.weekDayTitlesContainer).children
                            .map { it as TextView }
                            .forEachIndexed { index, textView ->
                                val dayOfWeek = daysOfWeek()[index]
                                val title =
                                    dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREA)
                                textView.text = title
                                if (index % 6 == 0)
                                    textView.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.iDorm_red
                                        )
                                    )
                            }
                    }

                    override fun create(view: View): MonthViewContainer = MonthViewContainer(view)


                }

            val currentMonth = YearMonth.now()
            val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
            val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
            val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
            binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
            binding.calendarView.scrollToMonth(currentMonth)
            binding.calendarView.monthScrollListener = object : MonthScrollListener {
                override fun invoke(month: CalendarMonth) {
                    setCurrentMonth(month)
                }
            }


        }

    }

}