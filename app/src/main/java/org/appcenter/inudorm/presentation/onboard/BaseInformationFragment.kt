package org.appcenter.inudorm.presentation.onboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentBaseInformationBinding

class BaseInformationFragment : Fragment() {

    companion object {
        fun newInstance() = BaseInformationFragment()
    }

    private val viewModel: BaseInformationViewModel by viewModels()
    private lateinit var binding: FragmentBaseInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_base_information, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.age1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) { // 한자리가 채워지면 age2로 focus 옮기기
                    binding.age2.requestFocus()
                }
            }
        })
    }
}