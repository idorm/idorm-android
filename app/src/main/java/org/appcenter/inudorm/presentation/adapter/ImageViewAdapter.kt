package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemBoardImageBinding

class ImageViewAdapter(
    var imageList: ArrayList<String>,
    private val removable: Boolean = false,
    private val onRemoveButtonClicked: ((Int, String) -> Unit)? = null,
    private val onClicked: (Int, String) -> Unit,
) : RecyclerView.Adapter<ImageViewAdapter.ViewHolder>() {


    inner class ViewHolder(
        var binding: ItemBoardImageBinding,
        onRemoveButtonClicked: (Int) -> Unit,
        onClicked: (Int) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {}

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
    ): ImageViewAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = DataBindingUtil.inflate<ItemBoardImageBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_board_image,
            viewGroup,
            false
        )

        return ViewHolder(binding,
            { idx -> onRemoveButtonClicked?.let { it(idx, imageList[idx]) } })
        { idx -> onClicked(idx, imageList[idx]) }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ImageViewAdapter.ViewHolder, position: Int) {
        imageList[position].let {
            viewHolder.binding.image = it
            viewHolder.binding.executePendingBindings()
        }
        viewHolder.binding.deleteButton.visibility = if (removable) {
            View.VISIBLE
        } else View.GONE
        viewHolder.binding.deleteButton.setOnClickListener {
            onRemoveButtonClicked?.let { it1 -> it1(position, imageList[position]) }
        }
        viewHolder.binding.imageView.setOnClickListener {
            onClicked(position, imageList[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = imageList.size
}