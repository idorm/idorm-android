package org.appcenter.inudorm.presentation.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentBaseInformationBinding
import org.appcenter.inudorm.databinding.ItemQuestionListBinding
import org.appcenter.inudorm.model.OnboardQuestion
import java.util.*

class OnboardRVAdapter (private val _dataSet: ArrayList<OnboardQuestion>): RecyclerView.Adapter<OnboardRVAdapter.ViewHolder>()  {

    var dataSet: ArrayList<OnboardQuestion>
        get() = _dataSet
        set(value) {
            value
        }

    lateinit var binding: ItemQuestionListBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemQuestionListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.question = dataSet[position]
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(var view: ItemQuestionListBinding) : RecyclerView.ViewHolder(view.root){
    }
}