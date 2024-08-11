package com.example.fintrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text

class CreateOrUpdateMonyBottomSheet(
    private val catList: List<CatUiData>,
    private val mony: MonyUiData? = null,
    private val onCreateClicked: (MonyUiData) -> Unit
) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_or_update_mony_bottom_sheet, container, false)

        val bsTvTitle: TextView = view.findViewById<TextView>(R.id.tv_title_mony_bs)
        val btnMCreate = view.findViewById<Button>(R.id.btn_mony_create)
        val tieMonyName = view.findViewById<TextInputEditText>(R.id.tie_mony_name)
        val spinner: Spinner = view.findViewById<Spinner>(R.id.cat_list)

        var monyCat: String? = null //aqui ainda nao é inicializado
        val catStrs: List<String> = catList.map {
            it.name
        }

        ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_item,
            catStrs
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                monyCat = catStrs[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        if (mony == null) {
            bsTvTitle.setText(R.string.create_track_title)
            btnMCreate.setText(R.string.create)
        } else {
            bsTvTitle.setText(R.string.update_track_title)
            btnMCreate.setText(R.string.update)
            tieMonyName.setText(mony.name)
            val currentCat: CatUiData = catList.first { it.name == mony.category }
            val index = catList.indexOf(currentCat)
            spinner.setSelection(index)
        }


        btnMCreate.setOnClickListener {
            if (monyCat != null) {
                onCreateClicked.invoke(
                    MonyUiData(
                        id = 0,
                        name = tieMonyName.text.toString(),
                        category = requireNotNull(monyCat),
                        250.0 //preciso criar a regra completa pra pegar o valor tbm
                    )
                )
                dismiss()
            } else {
                Snackbar.make(btnMCreate, "Please select a category", Snackbar.LENGTH_LONG).show()
            }
        }


        return view
    }
}