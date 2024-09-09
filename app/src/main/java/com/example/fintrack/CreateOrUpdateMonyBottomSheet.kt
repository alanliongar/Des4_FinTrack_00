package com.example.fintrack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class CreateOrUpdateMonyBottomSheet(
    private val catList: List<CatEntity>,
    private val mony: MonyUiData? = null,
    private val onCreateClicked: (MonyUiData) -> Unit,
    private val onUpdateClicked: (MonyUiData) -> Unit,
    private val onDeleteClicked: (MonyUiData) -> Unit
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_or_update_mony_bottom_sheet, container, false)
        val bsTvTitle: TextView = view.findViewById<TextView>(R.id.tv_title_mony_bs)
        val btnMCreateOrUpdate = view.findViewById<Button>(R.id.btn_mony_create_or_update)
        val btnMDelete = view.findViewById<Button>(R.id.btn_mony_delete)
        val tieMonyName = view.findViewById<TextInputEditText>(R.id.tie_mony_name)
        val tieMonyValue = view.findViewById<TextInputEditText>(R.id.tie_mony_value)
        val spinner: Spinner = view.findViewById<Spinner>(R.id.cat_list)

        var monyCat: String? = null

        val catTemp = mutableListOf<String>("Select")

        catTemp.addAll(
            catList.map {
                it.name
            }
        )

        val catStrs: List<String> = catTemp

        ArrayAdapter(
            requireActivity().baseContext, R.layout.item_spinner, catStrs
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                monyCat = catStrs[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        if (mony == null) {
            btnMDelete.isVisible = false
            bsTvTitle.setText(R.string.create_track_title)
            btnMCreateOrUpdate.setText(R.string.create)
        } else {
            btnMDelete.isVisible = true
            bsTvTitle.setText(R.string.update_track_title)
            btnMCreateOrUpdate.setText(R.string.update)
            tieMonyName.setText(mony.name)
            tieMonyValue.setText(mony.value.toString())

            val currentCat = mony.category
            val index = catTemp.indexOf(currentCat)
            if (index >= 0) {
                spinner.setSelection(index)
            }
        }

        btnMDelete.setOnClickListener {
            if (mony == null) {
                Log.d("CrOrUpMonyBottomSheet", "Task not found")
            } else {
                onDeleteClicked.invoke(mony)
                dismiss()
            }
        }
        btnMCreateOrUpdate.setOnClickListener {
            if (monyCat != "Select" && tieMonyName.text.toString().trim().isNotEmpty() && tieMonyValue.text.toString().trim().isNotEmpty()) {
                if (mony == null) {
                    onCreateClicked.invoke(
                        MonyUiData(
                            id = 0,
                            name = tieMonyName.text.toString().trim(),
                            category = requireNotNull(monyCat),
                            tieMonyValue.text.toString().trim().toDouble()
                        )
                    )
                } else {
                    onUpdateClicked.invoke(
                        MonyUiData(
                            id = mony.id,
                            name = tieMonyName.text.toString().trim(),
                            category = requireNotNull(monyCat),
                            value = tieMonyValue.text.toString().trim().toDouble()
                        )
                    )
                }
                dismiss()
            } else {
                if (monyCat == null && tieMonyName.text.toString().trim().isEmpty()) {
                    Snackbar.make(
                        btnMCreateOrUpdate,
                        "Please select a category and create an entry name",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else if (monyCat == null && tieMonyName.text.toString().trim().isNotEmpty()) {
                    Snackbar.make(
                        btnMCreateOrUpdate,
                        "Please select a category",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else if (monyCat != null && tieMonyName.text.toString().trim().isEmpty()) {
                    Snackbar.make(
                        btnMCreateOrUpdate,
                        "Create an entry name",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
        return view
    }
}