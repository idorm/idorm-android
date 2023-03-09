package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemCommentListBinding
import org.appcenter.inudorm.databinding.ItemCommentMypageListBinding
import org.appcenter.inudorm.model.board.Comment
import org.appcenter.inudorm.util.IDormLogger


class CommentListAdapter(
    private val _dataSet: ArrayList<Comment>,
    private val onViewPostClicked: (Comment) -> Unit,
) :
    RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>() {

    var dataSet: ArrayList<Comment>
        get() = _dataSet
        set(value) {
            value
        }

    inner class CommentViewHolder(
        var viewBinding: ItemCommentMypageListBinding,
        onViewPostClicked: (Int, Comment) -> Unit,
    ) :
        RecyclerView.ViewHolder(viewBinding.root)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CommentViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = DataBindingUtil.inflate<ItemCommentMypageListBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_comment_mypage_list,
            viewGroup,
            false
        )

        return CommentViewHolder(
            binding
        ) { id, comment -> onViewPostClicked(comment) }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: CommentViewHolder, position: Int) {

        fun setDeleted(deleted: Boolean) {
            viewHolder.viewBinding.validComment.visibility =
                if (deleted) View.GONE else View.VISIBLE
            viewHolder.viewBinding.deletedComment.visibility =
                if (deleted) View.VISIBLE else View.GONE
        }


        _dataSet[position].let {
            viewHolder.viewBinding.comment = it
            viewHolder.viewBinding.executePendingBindings()
            setDeleted(it.isDeleted)
        }

        viewHolder.viewBinding.viewPost.setOnClickListener {
            onViewPostClicked(_dataSet[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}