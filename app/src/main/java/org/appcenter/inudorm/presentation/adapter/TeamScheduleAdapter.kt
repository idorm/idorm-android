package org.appcenter.inudorm.presentation.adapter

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemCalendarBinding
import org.appcenter.inudorm.databinding.ItemTeamCalendarBinding
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.model.TeamProfile
import org.appcenter.inudorm.model.TeamSchedule
import org.appcenter.inudorm.presentation.calendar.mateColors
import org.appcenter.inudorm.util.IDormLogger

class TeamScheduleAdapter(
    private var _dataSet: ArrayList<TeamSchedule>,
    private val onClicked: (TeamSchedule) -> Unit,
    private val onDelete: ((Int) -> Unit)? = null,
) :
    RecyclerView.Adapter<TeamScheduleAdapter.ViewHolder>(), ManageableItemAdapter {

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
        IDormLogger.i(this, _dataSet.size.toString())
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
        IDormLogger.i(this, position.toString())
        _dataSet[position].let {
            viewHolder.viewBinding.teamSchedule = it
            if (it.startDate.isNullOrEmpty() && it.endDate.isNullOrEmpty()) {
                viewHolder.viewBinding.date.visibility = View.GONE
            }
            if (it.title.isNullOrEmpty()) viewHolder.viewBinding.content.visibility = View.GONE
            viewHolder.viewBinding.executePendingBindings()
            val teamProfiles = it.targets
            val context = viewHolder.itemView.context
            viewHolder.viewBinding.linearLayoutCompat.removeAllViews()
            if (teamProfiles.isNotEmpty())
                teamProfiles.forEach { teamProfile ->
                    IDormLogger.i(this, "$teamProfile")
                    val dot = LinearLayout(context)
                    dot.apply {
                        val lp = LinearLayout.LayoutParams(
                            20, 20
                        )
                        lp.setMargins(3, 0, 3, 10)
                        layoutParams = lp
                        id = ViewCompat.generateViewId()
                        backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                mateColors[teamProfile.order!!]
                            )
                        )
                        setBackgroundResource(
                            R.drawable.ic_dot_gray_400
                        )
                    }
                    viewHolder.viewBinding.linearLayoutCompat.addView(dot)
                }
            viewHolder.viewBinding.root.setOnClickListener {
                onClicked(_dataSet[position])
            }
            viewHolder.viewBinding.checkButton.setBackgroundResource(if (isManageModeEnabled) R.drawable.ic_trash_can else R.drawable.ic_right_arrow)
            viewHolder.viewBinding.checkButton.setOnClickListener { _ ->
                if (isManageModeEnabled) {
                    // Todo: set removable
                    onDelete?.invoke(it.teamCalendarId.toInt())
                } else {
                    onClicked(_dataSet[position])
                }
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
    private var isManageModeEnabled: Boolean = false
    override fun setManageMode(enabled: Boolean) {
        isManageModeEnabled = enabled
        notifyDataSetChanged()

    }

}