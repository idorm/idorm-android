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
    private val onWriteSubCommentClicked: (Int, Comment) -> Unit,
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
        onWriteSubCommentClicked: (Int, Comment) -> Unit,
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
            { id, comment -> onWriteSubCommentClicked(id, comment) }
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: CommentViewHolder, position: Int) {

        fun setDeleted(deleted: Boolean) {
            viewHolder.viewBinding.validComment.visibility =
                if (deleted) View.GONE else View.VISIBLE
            viewHolder.viewBinding.deletedComment.visibility =
                if (deleted) View.VISIBLE else View.GONE
        }

        val subComments = _dataSet[position].subComments
        val validSubComments = subComments?.filter { !it.isDeleted } as ArrayList<Comment>
        if (validSubComments.size > 0) {
            setDeleted(_dataSet[position].isDeleted)
            subCommentAdapter = NestedCommentAdapter(validSubComments, onCommentInteractionOpened)
            viewHolder.viewBinding.subComment.apply {
                adapter = subCommentAdapter
                layoutManager = LinearLayoutManager(context)
            }
        } else {
            if (_dataSet[position].isDeleted)
                viewHolder.viewBinding.deletedComment.visibility = View.GONE
            else {
                setDeleted(false)
            }

            viewHolder.viewBinding.subCommentArea.visibility = View.GONE
        }

        _dataSet[position].let {
            viewHolder.viewBinding.comment = it
            viewHolder.viewBinding.executePendingBindings()
        }

        viewHolder.viewBinding.openCommentInteraction.setOnClickListener {
            onCommentInteractionOpened(_dataSet[position])
        }
        viewHolder.viewBinding.writeSubComment.visibility = View.VISIBLE
        viewHolder.viewBinding.writeSubComment.setOnClickListener {
            onWriteSubCommentClicked(position, _dataSet[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}