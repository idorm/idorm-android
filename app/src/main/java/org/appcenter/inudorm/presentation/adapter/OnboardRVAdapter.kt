package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.model.OnboardQuestion
import java.util.*

class OnboardRVAdapter (private val _dataSet: ArrayList<OnboardQuestion>):
    RecyclerView.Adapter<OnboardRVAdapter.ViewHolder>()  {

    var dataSet: ArrayList<OnboardQuestion>
        get() = _dataSet
        set(value) {
            value
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_question_list, viewGroup, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.question.text = dataSet[position].question
        holder.required?.text = dataSet[position].required
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val question: TextView = view.findViewById(R.id.questionText)
        val required: TextView? = view.findViewById(R.id.required)

    }
}