package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemCalendarBinding
import org.appcenter.inudorm.model.Schedule

class CalendarAdapter(
    private var _dataSet: ArrayList<Schedule>,
    private val onClicked: (Schedule) -> Unit,
) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var dataSet: ArrayList<Schedule>
        get() = _dataSet
        set(value) {
            _dataSet = value
        }

    inner class ViewHolder(
        var viewBinding: ItemCalendarBinding,
        onClicked: (Schedule) -> Unit,
    ) :
        RecyclerView.ViewHolder(viewBinding.root) {}


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = DataBindingUtil.inflate<ItemCalendarBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_calendar,
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
            viewHolder.viewBinding.calendar = it
            if (it.startTime.isNullOrEmpty() && it.endTime.isNullOrEmpty()) {
                viewHolder.viewBinding.time.visibility = View.GONE
            }
            if (it.startDate.isNullOrEmpty() && it.endDate.isNullOrEmpty()) {
                viewHolder.viewBinding.time.visibility = View.GONE
            }
            if (it.content.isNullOrEmpty()) viewHolder.viewBinding.content.visibility = View.GONE
            viewHolder.viewBinding.executePendingBindings()
        }
        viewHolder.viewBinding.checkButton.setOnClickListener {
            onClicked(_dataSet[position])
        }
        viewHolder.viewBinding.calendarParent.setOnClickListener {
            onClicked(_dataSet[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}