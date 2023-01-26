package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentBoardBinding
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.util.IDormLogger

class BoardFragment : Fragment() {

    companion object {
        fun newInstance() = BoardFragment()
    }

    private lateinit var viewModel: BoardViewModel
    private lateinit var binding: FragmentBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false)
        return binding.root
    }

    val dorms = Dorm.values().map { SelectItem("제${it.text}기숙사", it.name) } as ArrayList<SelectItem>


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BoardViewModel::class.java)
        binding.viewModel = viewModel
        binding.fragment = this
        binding.lifecycleOwner = requireActivity()
        // TODO: Use the ViewModel
    }

    fun openSelector() {
        ListBottomSheet(dorms) {
            viewModel.setDorm(it)
        }.show(
            parentFragmentManager,
            ListBottomSheet.TAG
        )
    }

}