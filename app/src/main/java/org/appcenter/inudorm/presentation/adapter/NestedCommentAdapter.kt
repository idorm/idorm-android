package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemCommentListBinding
import org.appcenter.inudorm.databinding.ItemPostListBinding
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.util.IDormLogger
import java.util.*

class NestedCommentAdapter(
    private val _dataSet: ArrayList<Comment>,
    private val onCommentInteractionOpened: (Comment) -> Unit,
) :
    RecyclerView.Adapter<NestedCommentAdapter.NestedCommentViewHolder>() {

    init {
        IDormLogger.i(this, "NestedCommentAdatper Init")
        IDormLogger.i(this, _dataSet.toString())
    }

    var dataSet: ArrayList<Comment>
        get() = _dataSet
        set(value) {
            value
        }

    inner class NestedCommentViewHolder(
        var viewBinding: ItemCommentListBinding,
        onCommentDetailClicked: (Comment) -> Unit,
    ) :
        RecyclerView.ViewHolder(viewBinding.root) {
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NestedCommentViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = DataBindingUtil.inflate<ItemCommentListBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_comment_list,
            viewGroup,
            false
        )

        return NestedCommentViewHolder(
            binding,
        ) { onCommentInteractionOpened(it) }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: NestedCommentViewHolder, position: Int) {
        _dataSet[position].let {
            viewHolder.viewBinding.comment = it
            viewHolder.viewBinding.executePendingBindings()
        }
        viewHolder.viewBinding.subCommentArea.visibility = View.GONE
        viewHolder.viewBinding.openCommentInteraction.setOnClickListener {
            onCommentInteractionOpened(_dataSet[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}