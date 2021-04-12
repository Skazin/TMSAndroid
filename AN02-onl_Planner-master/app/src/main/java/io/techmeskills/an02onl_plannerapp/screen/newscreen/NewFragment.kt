package io.techmeskills.an02onl_plannerapp.screen.newscreen

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
import io.techmeskills.an02onl_plannerapp.screen.main.Note
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import java.util.*


class NewFragment : NavigationFragment<AddNewTextBinding>(R.layout.add_new_text) {
    override val viewBinding: AddNewTextBinding by viewBinding()
    private val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), DatePicker.OnDateChangedListener() {
            view, year, monthOfYear, dayOfMonth ->

        })

        viewBinding.addButton.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {
                setFragmentResult(ADD_NEW_RESULT, Bundle().apply {
                    putParcelable(NOTE, Note(-1,
                            viewBinding.etNote.text.toString(),
                            "${viewBinding.datePicker.dayOfMonth}.${viewBinding.datePicker.month + 1}.${viewBinding.datePicker.year}"))
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
        const val NOTE = "NOTE"
    }
}