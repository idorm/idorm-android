package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nguyenhoanglam.imagepicker.model.Image
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemBoardImageBinding
import org.appcenter.inudorm.databinding.ItemPostListBinding
import org.appcenter.inudorm.databinding.ItemQuestionListBinding
import org.appcenter.inudorm.model.board.Post

class ImageViewAdapter(
    var imageList: ArrayList<Image>,
    private val onClicked: (Image) -> Unit,
) : RecyclerView.Adapter<ImageViewAdapter.ViewHolder>() {


    inner class ViewHolder(
        var binding: ItemBoardImageBinding,
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

        return ViewHolder(binding) {
            onClicked(imageList[it])
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ImageViewAdapter.ViewHolder, position: Int) {
        imageList[position].let {
            viewHolder.binding.image = it.uri.toString()
            viewHolder.binding.executePendingBindings()
        }
        viewHolder.binding.imageView.setOnClickListener {
            onClicked(imageList[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = imageList.size
}