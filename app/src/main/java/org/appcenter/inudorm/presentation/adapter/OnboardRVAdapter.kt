package org.appcenter.inudorm.presentation.adapter
import android.content.Context
import android.content.res.ColorStateList
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemQuestionListBinding
import org.appcenter.inudorm.model.OnboardQuestion

class OnboardRVAdapter (private val _dataSet: ArrayList<OnboardQuestion>, context: Context): RecyclerView.Adapter<OnboardRVAdapter.ViewHolder>()  {

    var dataSet: ArrayList<OnboardQuestion>
        get() = _dataSet
        set(value) {
            value
        }

    lateinit var binding: ItemQuestionListBinding


    private val iDormBlueTint = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.iDorm_blue))
    private val iDormGrayTint = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.iDorm_gray_400))

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemQuestionListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup,false)

        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ViewHolder, position : Int) {

        holder.view.question = dataSet[position]



        holder.view.onboardField.addTextChangedListener {
            _dataSet[position].answer = it.toString()
            binding.currentLen.text = ((it ?: "").length.toString())
            validator(holder, it.toString().length)
        }


    }

    override fun getItemCount() = dataSet.size

    inner class ViewHolder(var view: ItemQuestionListBinding) : RecyclerView.ViewHolder(view.root){
    }

    private fun validator(holder: ViewHolder, inputLength : Int){
        if (inputLength > 0){
            setSuccessIcon(holder)
        }
        else {
            setNoneIcon(holder)
        }


    }

    private fun setSuccessIcon(holder: ViewHolder) {
        holder.view.inputOnboard.setEndIconTintList(iDormBlueTint)
        holder.view.inputOnboard.setEndIconDrawable(R.drawable.ic_textinput_check_true)
        holder.view.inputOnboard.endIconMode = TextInputLayout.END_ICON_CUSTOM
    }

    private fun setClearIcon(holder: ViewHolder) {
        holder.view.inputOnboard.setEndIconTintList(iDormGrayTint)
        holder.view.inputOnboard.setEndIconDrawable(R.drawable.ic_textinput_clear_text)
        holder.view.inputOnboard.endIconMode = END_ICON_CLEAR_TEXT
    }

    private fun setNoneIcon(holder: ViewHolder) {
        holder.view.inputOnboard.endIconMode = TextInputLayout.END_ICON_NONE
    }
}

