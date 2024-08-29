package com.example.fintrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBottomSheet(
    private val title: String, private val desc: String, private val btnText: String,
    private val onClicked: () -> Unit
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_bottom_sheet, container, false)
        val btnAction = view.findViewById<Button>(R.id.btn_info_del)
        val tvITitle = view.findViewById<TextView>(R.id.tv_info_title)
        val tvIDesc = view.findViewById<TextView>(R.id.tv_info_desc)
        tvITitle.setText(title)
        tvIDesc.setText(desc)
        btnAction.text = btnText
        btnAction.setOnClickListener {
            onClicked.invoke()
            dismiss()
        }
        return view
    }
}