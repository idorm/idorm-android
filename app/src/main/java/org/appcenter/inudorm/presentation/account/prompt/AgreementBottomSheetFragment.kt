package org.appcenter.inudorm.presentation.account.prompt

import AgreementBottomSheetAdapter
import CheckableItem
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
import org.appcenter.inudorm.util.IDormLogger

operator fun Boolean.inv() = !this

class AgreementBottomSheetFragment(
    private val items: ArrayList<CheckableItem>,
    private val onSubmit: (Boolean) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetAgreementBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: AgreementBottomSheetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_bottom_sheet_agreement,
                container,
                false
            )
        return binding.root
    }

    private var checkedItems = arrayListOf<String>()

    override fun onStart() {
        super.onStart()
        adapter = AgreementBottomSheetAdapter(items, { position, item ->
            item.toggle()
            if (!checkedItems.contains(item.id)) checkedItems.add(item.id)
            else checkedItems.remove(item.id)
            adapter.notifyItemChanged(position, item)
        }, { position ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(items[position].url))
            startActivity(intent)
        })
        binding.submit.setOnClickListener {
            IDormLogger.i(this@AgreementBottomSheetFragment, "checkedItems: $checkedItems")
            if (checkedItems.containsAll(items.map { it.id }))
                onSubmit(true)
            else onSubmit(false)
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
