package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemPopularPostBinding
import org.appcenter.inudorm.model.board.Post

class PopularPostAdapter(
    private val _dataSet: ArrayList<Post>,
    private val onClicked: (Post) -> Unit,
) :
    RecyclerView.Adapter<PopularPostAdapter.PostViewHolder>() {

    var dataSet: ArrayList<Post>
        get() = _dataSet
        set(value) {
            value
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
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}