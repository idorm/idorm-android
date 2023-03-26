package org.appcenter.inudorm.presentation.adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.databinding.ItemQuestionListBinding
import org.appcenter.inudorm.model.OnboardQuestion

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

        holder.view.onboardField.addTextChangedListener {
            _dataSet[position].answer = it.toString()
            if ((_dataSet[position].maxLen ?: 16) != 16)
                {
                    binding.currentLen.text = ((it ?: "").length.toString())
                }

        }

    }

    override fun getItemCount() = dataSet.size

    inner class ViewHolder(var view: ItemQuestionListBinding) : RecyclerView.ViewHolder(view.root){
    }
}