package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemCalendarBinding
import org.appcenter.inudorm.databinding.ItemTeamCalendarBinding
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.model.TeamSchedule

class TeamScheduleAdapter(
    private var _dataSet: ArrayList<TeamSchedule>,
    private val onClicked: (TeamSchedule) -> Unit,
) :
    RecyclerView.Adapter<TeamScheduleAdapter.ViewHolder>() {

    var dataSet: ArrayList<TeamSchedule>
        get() = _dataSet
        set(value) {
            _dataSet = value
        }

    inner class ViewHolder(
        var viewBinding: ItemTeamCalendarBinding,
        onClicked: (TeamSchedule) -> Unit,
    ) :
        RecyclerView.ViewHolder(viewBinding.root) {}


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = DataBindingUtil.inflate<ItemTeamCalendarBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_team_calendar,
            viewGroup,
            false
        )

        return ViewHolder(binding) {
            onClicked(it)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        _dataSet[position].let {
            viewHolder.viewBinding.teamSchedule = it
            if (it.startDate.isNullOrEmpty() && it.endDate.isNullOrEmpty()) {
                viewHolder.viewBinding.date.visibility = View.GONE
            }
            if (it.content.isNullOrEmpty()) viewHolder.viewBinding.content.visibility = View.GONE
            viewHolder.viewBinding.executePendingBindings()
        }
        viewHolder.viewBinding.checkButton.setOnClickListener {
            onClicked(_dataSet[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}