package io.techmeskills.an02onl_plannerapp.screen.newscreen

import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.AddNewTextBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel


class NewFragment : NavigationFragment<AddNewTextBinding>(R.layout.add_new_text) {
    override val viewBinding: AddNewTextBinding by viewBinding()
    private val viewModel: AddNewViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.datePicker.init(2021, 3,3, DatePicker.OnDateChangedListener {
            view, year, monthOfYear, dayOfMonth ->

        })

        viewBinding.btnConfirm.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {
                setFragmentResult(ADD_NEW_RESULT, Bundle().apply {
                    putString(TEXT, viewBinding.etNote.text.toString())
                    putString(DATE, "${viewBinding.datePicker.dayOfMonth}.${viewBinding.datePicker.month + 1}.${viewBinding.datePicker.year}")
                })
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), " Please, enter your note", Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, top, 0, 0)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

    companion object {
        const val ADD_NEW_RESULT = "ADD_NEW_RESULT"
        const val TEXT = "TEXT"
        const val DATE = "DATE"
    }
}