package org.appcenter.inudorm.presentation.board

import BottomSheetListAdapter
import CheckableItem
import RadioButtonListAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentBottomSheetAgreementBinding
import org.appcenter.inudorm.databinding.FragmentBottomSheetBinding
import org.appcenter.inudorm.databinding.FragmentBottomSheetRadiobuttonBinding
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.util.IDormLogger

operator fun Boolean.inv() = !this

class RadioButtonListBottomSheet(
    private val items: ArrayList<CheckableItem>,
    private val onSubmit: (String?, String?) -> Unit,
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetRadiobuttonBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: RadioButtonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_bottom_sheet_radiobutton,
                container,
                false
            )
        return binding.root
    }

    private var checkedItem: String? = null

    override fun onStart() {
        super.onStart()
        adapter = RadioButtonListAdapter(items) { position, item ->
            // 체크 되지 않은 요소를 체크하려 할때
            val items = ArrayList(adapter.itemList)
            if (checkedItem != item.id) {
                // 그 항목을 체크하고
                item.checked = true
                checkedItem = item.id
                // 다른 요소 모두 체크 해제
                items.forEachIndexed { idx, it ->
                    if (checkedItem == it.id) return@forEachIndexed
                    // deep copy.
                    items[idx].checked = false
                }
            }
            adapter.itemList = items
            adapter.notifyDataSetChanged()

        }
        binding.submit.setOnClickListener {
            IDormLogger.i(this@RadioButtonListBottomSheet, "checkedItem: $checkedItem")
            onSubmit(checkedItem, "상세사유 더미")
        }
        binding.itemRecyclerView.adapter = adapter
        layoutManager = LinearLayoutManager(requireContext())
        binding.itemRecyclerView.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}
