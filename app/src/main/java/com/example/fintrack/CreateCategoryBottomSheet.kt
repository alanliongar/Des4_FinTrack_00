package com.example.fintrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class CreateCategoryBottomSheet(
    private val onCreateClicked: (String) -> Unit
) :
    BottomSheetDialogFragment() {
        private lateinit var icones: List<Icon>
        private lateinit var cores: List<Cor>

    fun setData(cores: List<Cor>, icones: List<Icon>) {
        this.cores = cores
        this.icones = icones
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_cat_bottom_sheet, container, false)
        val btnCreate = view.findViewById<Button>(R.id.btn_cat_create)
        val tieCatName = view.findViewById<TextInputEditText>(R.id.tie_cat_name)
        val colorAdapter = ColorAdapter()
        val iconAdapter = IconAdapter()
        val rvIcon = view.findViewById<RecyclerView>(R.id.rv_icon)
        val rvColor = view.findViewById<RecyclerView>(R.id.rv_color)
        rvIcon.adapter = iconAdapter
        rvColor.adapter = colorAdapter
        iconAdapter.submitList(icones)
        colorAdapter.submitList(cores)

        btnCreate.setOnClickListener {
            val name = tieCatName.text.toString().trim()
            onCreateClicked.invoke(name)
            dismiss()
        }
        return view
    }
}