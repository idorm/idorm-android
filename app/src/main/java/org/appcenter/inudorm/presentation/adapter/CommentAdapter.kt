package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemCommentListBinding
import org.appcenter.inudorm.model.board.Comment

class CommentAdapter(
    private val _dataSet: ArrayList<Comment>,
    private val onCommentInteractionOpened: (Comment) -> Unit,
    private val onWriteSubCommentClicked: (Comment) -> Unit,
) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private lateinit var subCommentAdapter: NestedCommentAdapter


    var dataSet: ArrayList<Comment>
        get() = _dataSet
        set(value) {
            value
        }

    inner class CommentViewHolder(
        var viewBinding: ItemCommentListBinding,
        onCommentDetailClicked: (Comment) -> Unit,
        onWriteSubCommentClicked: (Comment) -> Unit,
    ) :
        RecyclerView.ViewHolder(viewBinding.root)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CommentViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = DataBindingUtil.inflate<ItemCommentListBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_comment_list,
            viewGroup,
            false
        )

        return CommentViewHolder(binding,
            { onCommentInteractionOpened(it) },
            { onWriteSubCommentClicked(it) }
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: CommentViewHolder, position: Int) {
        val subComments = _dataSet[position].subComments
        if (subComments != null) {
            subCommentAdapter = NestedCommentAdapter(subComments, onCommentInteractionOpened)
            viewHolder.viewBinding.subComment.apply {
                adapter = subCommentAdapter
                layoutManager = LinearLayoutManager(context)
            }
        } else {
            viewHolder.viewBinding.subCommentArea.visibility = View.GONE
        }

        _dataSet[position].let {
            viewHolder.viewBinding.comment = it
            viewHolder.viewBinding.executePendingBindings()
        }
        viewHolder.viewBinding.openCommentInteraction.setOnClickListener {
            onCommentInteractionOpened(_dataSet[position])
        }
        viewHolder.viewBinding.writeSubComment.setOnClickListener {
            onWriteSubCommentClicked(_dataSet[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}