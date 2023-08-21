package org.appcenter.inudorm.presentation.calendar

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.marginStart
import androidx.core.view.setMargins
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.MonthScrollListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentCalendarBinding
import org.appcenter.inudorm.model.Member
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.model.TeamSchedule
import org.appcenter.inudorm.presentation.LoadingFragment
import org.appcenter.inudorm.presentation.adapter.CalendarAdapter
import org.appcenter.inudorm.presentation.adapter.TeamProfileAdapter
import org.appcenter.inudorm.presentation.adapter.TeamScheduleAdapter
import org.appcenter.inudorm.presentation.mypage.matching.MyMatchingProfileActivity
import org.appcenter.inudorm.usecase.getCalendarDateFormat
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.State
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Date
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

        return binding.root
    }

    private fun initBind() {
        //테스트~~ 컬러는 안 해 봤어여~
        teamProfileAdapter = TeamProfileAdapter(arrayListOf())
        teamScheduleAdapter = TeamScheduleAdapter(arrayListOf()) {

        }
        officialScheduleAdapter = CalendarAdapter(arrayListOf()) {

        }

        binding.teamProfileRecycler.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)

        binding.teamCalendarRecycler.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        binding.officialEvents.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)

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
        val monthString = date.yearMonth.monthValue.toString().padStart(2, '0')
        val date = "${date.yearMonth.year}-${monthString}"
        IDormLogger.i(this, date)
        viewModel.getSchedules(date)
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

        // Todo: 현재 날짜로 변경
        setCurrentMonth(CalendarMonth(YearMonth.now(), arrayListOf()))
        setExtended(false)
        binding.registerSchedule.setOnClickListener {
            toggleExtended()
        }
        binding.registerTeamSchedule.setOnClickListener {
            val intent = Intent(requireContext(), WriteTeamScheduleActivity::class.java)
            startActivity(intent)
        }
        lifecycleScope.launch {
            viewModel.schedules.collect {
                setLoadingState(it)
                /**
                 * yyyy-MM-dd -> TeamProfile[] 인 map. 날짜별 팀 해당하는 팀 멤버를 보관힙니다.
                 */
                val dateMap = mutableMapOf<String, List<TeamProfile>>()
                if (it is State.Success) {
                    it.data?.forEach { schedule ->
                        dateMap.merge(
                            schedule.startDate!!,
                            schedule.targets
                        ) { prev, curr -> prev + curr }
                    }
                    dateMap.forEach { (t, u) ->
                        dateMap[t] =
                            u.groupBy { member -> member.memberId }.map { entry -> entry.value[0] }
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
                                if (data.date == LocalDate.now())
                                    container.view.setBackgroundResource(R.drawable.ic_today)
                                else
                                // 아마 RecyclerView를 이용하는 특성상 몇개 캘린더를 재활용하는 형태라서 위에서 setBackgroundResource를 하면 4개월 주기로 다른 날에도 뜸
                                    container.view.setBackgroundColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.white
                                        )
                                    )

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
                setLoadingState(it)
            }
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