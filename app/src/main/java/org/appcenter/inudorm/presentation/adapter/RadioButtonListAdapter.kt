import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.util.IDormLogger

class RadioButtonListAdapter(
    private val _itemList: ArrayList<CheckableItem>,
    private val onItemClick: (Int, CheckableItem) -> Unit,
) :
    RecyclerView.Adapter<RadioButtonListAdapter.ViewHolder>() {

    var itemList: ArrayList<CheckableItem>
        get() = _itemList
        set(value) {
            value
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_bottom_sheet_agreement, viewGroup, false)

        return ViewHolder(view) {
            IDormLogger.i(this@RadioButtonListAdapter, "clicked")
            onItemClick(it, itemList[it])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            for (payload in payloads) {
                val item = payload as CheckableItem
                holder.title.text = item.text
                holder.radioButton.isChecked = item.checked
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = _itemList[position]
        holder.title.text = item.text
        holder.radioButton.isChecked = item.checked

    }

    override fun getItemCount() = _itemList.size

    class ViewHolder(view: View, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val radioButton: RadioButton = view.findViewById(R.id.radioButton)
        private val link: TextView = view.findViewById(R.id.detail)
        private val menuItem: View = view.findViewById(R.id.menuItem)

        init {
            menuItem.setOnClickListener {
                IDormLogger.i(this@ViewHolder, "clicked inside")
                onItemClicked(adapterPosition)
            }
            link.visibility = View.GONE

        }
    }

}