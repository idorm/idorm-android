import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.util.IDormLogger

class CheckableItem(
    val id: String,
    val text: String,
    val required: Boolean,
    var checked: Boolean,
    val url: String
) {
    fun toggle() {
        checked = !this.checked
    }
}

class AgreementBottomSheetAdapter(
    private val _itemList: ArrayList<CheckableItem>,
    private val onItemClick: (Int, CheckableItem) -> Unit,
    private val onAgreementOpen: (Int) -> Unit,
) :
    RecyclerView.Adapter<AgreementBottomSheetAdapter.ViewHolder>() {

    private var itemList: ArrayList<CheckableItem>
        get() = _itemList
        set(value) {
            value
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_bottom_sheet_agreement, viewGroup, false)

        return ViewHolder(view, {
            IDormLogger.i(this@AgreementBottomSheetAdapter, "clicked")
            onItemClick(it, itemList[it])
        }, {
            IDormLogger.i(this@AgreementBottomSheetAdapter, "opened")
            onAgreementOpen(it)
        })
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

    class ViewHolder(view: View, onItemClicked: (Int) -> Unit, onAgreementOpen: (Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val radioButton: RadioButton = view.findViewById(R.id.radioButton)
        val menuItem: ConstraintLayout = view.findViewById(R.id.menuItem)
        val detail: TextView = view.findViewById(R.id.detail)
        init {
            radioButton.setOnClickListener {
                IDormLogger.i(this@ViewHolder, "clicked inside")
                onItemClicked(adapterPosition)
            }
            detail.setOnClickListener {
                IDormLogger.i(this@ViewHolder, "clicked inside")
                onAgreementOpen(adapterPosition)
            }
        }
    }

}