package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.ShapeAppearanceModel
import com.yuyakaido.android.cardstackview.CardStackView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.model.Mate

class RoomMateAdapter(private val _dataSet: ArrayList<Mate>) :
    RecyclerView.Adapter<RoomMateAdapter.ViewHolder>() {

    var dataSet: ArrayList<Mate>
        get() = _dataSet
        set(value) {
            value
        }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val card: FrameLayout

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.textView)
            card = view.findViewById(R.id.card)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.matching_item, viewGroup, false)


        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position].nickname
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}