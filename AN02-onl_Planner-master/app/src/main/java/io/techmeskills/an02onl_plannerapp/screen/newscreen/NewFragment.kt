package io.techmeskills.an02onl_plannerapp.screen.newscreen

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.AddNewTextBinding
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class NewFragment : NavigationFragment<AddNewTextBinding>(R.layout.add_new_text) {

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    override val viewBinding: AddNewTextBinding by viewBinding()
    private val viewModel: NewFragmentViewModel by viewModel()
    private val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), DatePicker.OnDateChangedListener() {
            view, year, monthOfYear, dayOfMonth ->

        })

        viewBinding.addButton.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {
                viewModel.addNewNote(
                    Note(
                            title = viewBinding.etNote.text.toString(),
                            date = dateFormatter.format(viewBinding.datePicker.getSelectedDate()),
                            userName = ""
                    )
                )
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please, enter your note", Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

    private fun DatePicker.getSelectedDate(): Date {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(Calendar.YEAR, this.year)
        calendar.set(Calendar.MONTH, this.month)
        calendar.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
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
}