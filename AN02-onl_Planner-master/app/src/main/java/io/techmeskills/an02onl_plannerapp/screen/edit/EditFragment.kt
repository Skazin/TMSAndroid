package io.techmeskills.an02onl_plannerapp.screen.edit

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentEditBinding
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class EditFragment : NavigationFragment<FragmentEditBinding>(R.layout.fragment_edit) {

    override val viewBinding: FragmentEditBinding by viewBinding()
    private val calendar = Calendar.getInstance()
    private var selectedDate: Calendar = Calendar.getInstance().apply { time = Date() }
    private val args: EditFragmentArgs by navArgs()
    private val viewModel: EditFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, _, _, _ -> }

        viewBinding.timePicker.setIs24HourView(true)

        viewBinding.etNote.setText(args.note?.title)

        viewBinding.editButton.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {

                args.note?.let { viewModel.updateNote(
                        Note(
                                id = it.id,
                                title = viewBinding.etNote.text.toString(),
                                date = viewBinding.datePicker.getSelectedDate(viewBinding.timePicker).time,
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
    }

    private fun DatePicker.getSelectedDate(timePicker: TimePicker): Date {
        selectedDate.set(Calendar.YEAR, this.year)
        selectedDate.set(Calendar.MONTH, this.month)
        selectedDate.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
        selectedDate.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        selectedDate.set(Calendar.MINUTE, timePicker.minute)
        return selectedDate.time
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