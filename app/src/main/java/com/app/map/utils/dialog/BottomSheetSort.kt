package com.app.map.utils.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.app.map.R
import com.app.map.databinding.BottomSheetSortBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSort(var callback : (Boolean) -> Unit) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetSortBinding
    private  var isAscending : Boolean? = null

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_sort, container, false)
        clickListener()
        return binding.root
    }

    private fun clickListener() {
        binding.apply {
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val id = group.checkedRadioButtonId
                isAscending = id == R.id.ascending
            }
            applyButton.setOnClickListener {
                isAscending?.let {
                    callback(isAscending!!)
                    dialog?.dismiss()
                }

            }
        }
    }
}