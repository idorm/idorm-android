package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemBoardImageBinding

class ImageViewPagerAdapter(
    var imageList: ArrayList<String>,
) : RecyclerView.Adapter<ImageViewPagerAdapter.ViewHolder>() {


    inner class ViewHolder(
        var view: View,
        onClicked: (Int) -> Unit,
    ) :
        RecyclerView.ViewHolder(view.rootView) {}

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
    ): ImageViewPagerAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_image_viewer, viewGroup, false)

        return ViewHolder(view) {
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ImageViewPagerAdapter.ViewHolder, position: Int) {
        imageList[position].let {
            val imgView = viewHolder.view.findViewById<ImageView>(R.id.image)
            Glide.with(viewHolder.view)
                .load(it)
                .into(imgView)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = imageList.size
}