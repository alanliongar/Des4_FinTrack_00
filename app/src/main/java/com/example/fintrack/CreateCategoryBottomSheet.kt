package com.example.fintrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class CreateCategoryBottomSheet(
    private val onCreateClicked: (CatUiData) -> Unit
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
        var contColor: Int = 0
        var contIcon: Int = 0
        colorAdapter.onClick { corSel ->
            val corTemp = cores.map { cor ->
                if (cor.name == corSel.name) {
                    cor.copy(isSelected = true)
                } else {
                    cor.copy(isSelected = false)
                }
            }
            colorAdapter.submitList(corTemp)
            contColor = corSel.color
        }

        iconAdapter.onClick { icoSel ->
            val icoTemp = icones.map { ico ->
                if (ico.name == icoSel.name) {
                    ico.copy(isSelected = true)
                } else {
                    ico.copy(isSelected = false)
                }
            }
            iconAdapter.submitList(icoTemp)
            contIcon = icoSel.icone
        }

        btnCreate.setOnClickListener {
            if (contIcon != 0 && contColor != 0 && tieCatName.text.toString().trim().isNotEmpty()) {
                val newCat=CatUiData(name=tieCatName.text.toString().trim(),contColor,contIcon,false)
                onCreateClicked.invoke(newCat)
                dismiss()
            } else if(tieCatName.text.toString().trim().isEmpty()){
                Snackbar.make(view, "Escreva um nome pra sua categoria", Snackbar.LENGTH_SHORT).show()
            }else {
                if (contIcon == 0 && contColor == 0) {
                        Snackbar.make(view, "Selecione uma cor e um ícone para a sua categoria", Snackbar.LENGTH_SHORT).show()
                } else {
                    if (contIcon == 0) {
                        Snackbar.make(view, "Selecione um ícone para a sua categoria", Snackbar.LENGTH_SHORT).show()
                    }else{
                        Snackbar.make(view, "Selecione uma cor para a sua categoria", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return view
    }
}