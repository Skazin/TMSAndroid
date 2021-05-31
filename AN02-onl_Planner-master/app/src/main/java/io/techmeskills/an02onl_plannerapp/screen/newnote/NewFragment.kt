package io.techmeskills.an02onl_plannerapp.screen.newnote

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentNewNoteBinding
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.CustomDayPicker
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class NewFragment : NavigationFragment<FragmentNewNoteBinding>(R.layout.fragment_new_note) {

    override val viewBinding: FragmentNewNoteBinding by viewBinding()

    private val viewModel: NewFragmentViewModel by viewModel()

    private var selectedDate: Calendar = Calendar.getInstance().apply { time = Date() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.timePicker.setIs24HourView(true)

        viewBinding.addButton.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {
                viewModel.addNewNote(
                    Note( //при добавлении id можно не указывать
                            title = viewBinding.etNote.text.toString(),
                            date = selectedDate.timeInMillis,
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

        viewBinding.datePicker.onDayChangeCallback = object : CustomDayPicker.DateChangeListener {
            override fun onDateChanged(date: Date) {
                val hour = selectedDate.get(Calendar.HOUR_OF_DAY)
                val minute = selectedDate.get(Calendar.MINUTE)
                selectedDate.time = date
                selectedDate.set(Calendar.HOUR_OF_DAY, hour)
                selectedDate.set(Calendar.MINUTE, minute)
            }
        }

        viewBinding.timePicker.setOnTimeChangedListener { _, hour, minute ->
            selectedDate.set(Calendar.HOUR_OF_DAY, hour)
            selectedDate.set(Calendar.MINUTE, minute)
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
}