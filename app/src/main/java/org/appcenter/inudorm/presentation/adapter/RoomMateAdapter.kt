package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemMatchingBinding
import org.appcenter.inudorm.model.MatchingInfo
import java.util.*

class RoomMateAdapter(
    private val _dataSet: ArrayList<MatchingInfo>,
    private val onMoreClicked: (Int) -> Unit,
) :
    RecyclerView.Adapter<RoomMateAdapter.RoomMateViewHolder>() {

    var dataSet: ArrayList<MatchingInfo>
        get() = _dataSet
        set(value) {
            value
        }

    inner class RoomMateViewHolder(
        var viewBinding: ItemMatchingBinding,
        onMoreClicked: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(viewBinding.root) {}


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RoomMateViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = DataBindingUtil.inflate<ItemMatchingBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_matching,
            viewGroup,
            false
        )

        return RoomMateViewHolder(binding) {
            onMoreClicked(it)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RoomMateViewHolder, position: Int) {
        _dataSet[position].let {
            viewHolder.viewBinding.mate = it
            viewHolder.viewBinding.executePendingBindings()
        }
        viewHolder.viewBinding.more.setOnClickListener {
            onMoreClicked(_dataSet[position].memberId)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}