package org.appcenter.inudorm.presentation.onboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.appcenter.inudorm.R

class AdditionalInformationFragment : Fragment() {

    companion object {
        fun newInstance() = AdditionalInformationFragment()
    }

    private lateinit var viewModel: AdditionalInformationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_additional_information, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdditionalInformationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}