package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginStart
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemPopularPostBinding
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.util.IDormLogger

class PopularPostAdapter(
    private var _dataSet: ArrayList<Post>,
    private val onClicked: (Post) -> Unit,
) :
    RecyclerView.Adapter<PopularPostAdapter.PostViewHolder>() {

    var dataSet: ArrayList<Post>
        get() = _dataSet
        set(value) {
            _dataSet = value
        }

    inner class PostViewHolder(
        var viewBinding: ItemPopularPostBinding,
        onClicked: (Post) -> Unit
    ) :
        RecyclerView.ViewHolder(viewBinding.root) {}


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = DataBindingUtil.inflate<ItemPopularPostBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_popular_post,
            viewGroup,
            false
        )

        return PostViewHolder(binding) {
            onClicked(it)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        _dataSet[position].let {
            viewHolder.viewBinding.post = it
            viewHolder.viewBinding.executePendingBindings()
        }
        viewHolder.viewBinding.popularPostParent.setOnClickListener {
            onClicked(_dataSet[position])
        }

        setPopularPostMargin(viewHolder, position)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    private fun setPopularPostMargin(viewHolder: PostViewHolder, position: Int) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        viewHolder.viewBinding.popularPostLinear.layoutParams = layoutParams

        layoutParams.setMargins(0,0,24,0)
        if(position == 0) layoutParams.setMargins(72, 0,24,0)
        if(position == _dataSet.size -1)  layoutParams.setMargins(0, 0,72,0)
    }

}