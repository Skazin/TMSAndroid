package io.techmeskills.an02onl_plannerapp.screen.newscreen

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.AddNewNoteFragmentBinding
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class NewFragment : NavigationFragment<AddNewNoteFragmentBinding>(R.layout.add_new_note_fragment) {

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    override val viewBinding: AddNewNoteFragmentBinding by viewBinding()
    private val viewModel: NewFragmentViewModel by viewModel()
    private val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, _, _, _ -> }
        viewBinding.timePicker.setIs24HourView(true)


        viewBinding.addButton.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {
                viewModel.addNewNote(
                    Note(
                            title = viewBinding.etNote.text.toString(),
                            date = dateFormatter.format(viewBinding.datePicker.getSelectedDate(viewBinding.timePicker)),
                            userName = "",
                            notificationOn = viewBinding.notificationCheck.isChecked
                    )
                )
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please, enter your note", Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

    private fun DatePicker.getSelectedDate(timePicker: TimePicker): Date {
        timePicker.setIs24HourView(true)
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(Calendar.YEAR, this.year)
        calendar.set(Calendar.MONTH, this.month)
        calendar.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)
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