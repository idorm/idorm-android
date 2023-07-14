package org.appcenter.inudorm.presentation.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentCalendarBinding
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.presentation.adapter.TeamProfileAdapter

class CalendarFragment : Fragment() {

    companion object {
        fun newInstance() = CalendarFragment()
    }

    private lateinit var viewModel: CalendarViewModel
    private lateinit var binding: FragmentCalendarBinding
    lateinit var adapter: TeamProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)

        initBind()

        return binding.root
    }

    private fun initBind() {
        //테스트~~ 컬러는 안 해 봤어여~

        val nickName1 = TeamProfile("나도미")
        val nickName2 = TeamProfile("나도미나도미")
        val nickName3 = TeamProfile("나도미나도")
        val nickName4 = TeamProfile("나도미나")

        val list = arrayListOf<TeamProfile>(
            nickName1,
            nickName2,
            nickName3,
            nickName4
        )

        adapter = TeamProfileAdapter(list)

        binding.teamProfileRecycler.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)

        with(binding) {
            teamProfileRecycler.adapter = adapter
        }



    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        // TODO: Use the ViewModel
    }

}