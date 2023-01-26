package org.appcenter.inudorm.presentation

import BottomSheetListAdapter
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentBottomSheetBinding
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.util.IDormLogger

class ListBottomSheet(private val items: ArrayList<SelectItem>, private val onClick: (SelectItem) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: BottomSheetListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapter = BottomSheetListAdapter(items) { menu ->
            this@ListBottomSheet.dismiss()
            // Todo: dismiss 되어도 리사이클러가 살아남아 메모리 누수가 발생허가나 의도치 않은 동작을 할 가능성은?
            onClick(menu)
        }
        binding.imageButton.setOnClickListener { dismiss() }
        binding.itemRecyclerView.adapter = adapter
        layoutManager = LinearLayoutManager(requireContext())
        binding.itemRecyclerView.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}
