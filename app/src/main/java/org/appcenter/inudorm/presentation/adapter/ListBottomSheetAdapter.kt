import android.os.Build.VERSION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R

class SelectItem(
    val title: String,
    val value: String,
    val iconResourceId: Int? = null,
    val desc: String? = null
)

class BottomSheetListAdapter(
    private val _itemList: ArrayList<SelectItem>,
    private val onItemClick: (SelectItem) -> Unit
) :
    RecyclerView.Adapter<BottomSheetListAdapter.ViewHolder>() {

    private var itemList: ArrayList<SelectItem>
        get() = _itemList
        set(value) {
            value
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_bottom_sheet_list, viewGroup, false)

        return ViewHolder(view) {
            onItemClick(itemList[it])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = _itemList[position]
        if (VERSION.SDK_INT >= 29) {
            holder.menuItem.background = ContextCompat.getDrawable(
                holder.menuItem.context,
                R.color.selector_bottom_sheet_background_color
            )
        }
        holder.title.text = item.title
        if (item.desc != null) holder.desc.text = item.desc
        else holder.desc.visibility = View.GONE
        if (item.iconResourceId != null) {
            val drawable = AppCompatResources.getDrawable(holder.icon.context, item.iconResourceId)
            holder.icon.setImageDrawable(drawable);
        } else holder.icon.visibility = View.GONE
        holder.menuItem.setOnClickListener {
            onItemClick(_itemList[position])
        }
    }

    override fun getItemCount() = _itemList.size

    class ViewHolder(view: View, onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val desc: TextView = view.findViewById(R.id.desc)
        val icon: ImageView = view.findViewById(R.id.icon)
        val menuItem: ConstraintLayout = view.findViewById(R.id.menuItem)

        init {
            itemView.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }
    }

}