package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemCalendarBinding
import org.appcenter.inudorm.databinding.ItemPostListBinding
import org.appcenter.inudorm.model.Calendar
import org.appcenter.inudorm.model.board.Post

class CalendarAdapter(
    private var _dataSet: ArrayList<Calendar>,
    private val onClicked: (Calendar) -> Unit,
) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var dataSet: ArrayList<Calendar>
        get() = _dataSet
        set(value) {
            _dataSet = value
        }

    inner class ViewHolder(
        var viewBinding: ItemCalendarBinding,
        onClicked: (Calendar) -> Unit,
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
            viewHolder.viewBinding.executePendingBindings()
        }
        viewHolder.viewBinding.checkButton.setOnClickListener {
            onClicked(_dataSet[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}