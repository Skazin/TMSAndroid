package io.techmeskills.an02onl_plannerapp.screen.newscreen

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.EditNoteFragmentBinding
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class EditFragment : NavigationFragment<EditNoteFragmentBinding>(R.layout.edit_note_fragment) {

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    override val viewBinding: EditNoteFragmentBinding by viewBinding()
    private val calendar = Calendar.getInstance()
    private val args: EditFragmentArgs by navArgs()
    private val viewModel: EditFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, _, _, _ -> }
        viewBinding.timePicker.setIs24HourView(true)

        viewBinding.editButton.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {

                args.note?.let { viewModel.updateNote(
                        Note(
                                id = it.id,
                                title = viewBinding.etNote.text.toString(),
                                date = dateFormatter.format(viewBinding.datePicker.getSelectedDate(viewBinding.timePicker)),
                                userName = it.userName,
                                notificationOn = viewBinding.notificationCheck.isChecked
                        )
                )
                }
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please, enter your note", Toast.LENGTH_LONG)
                    .show()
            }
        }

        viewBinding.notificationOn.isVisible = viewBinding.notificationCheck.isChecked
        viewBinding.notificationOff.isVisible = viewBinding.notificationCheck.isChecked.not()
    }

    private fun DatePicker.getSelectedDate(timePicker: TimePicker): Date {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(Calendar.YEAR, this.year)
        calendar.set(Calendar.MONTH, this.month)
        calendar.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
        calendar.set(Calendar.HOUR, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)
        return calendar.time
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setVerticalMargin(marginTop = top)
        viewBinding.editButton.setVerticalMargin(marginBottom = bottom * 11 / 10)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}