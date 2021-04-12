package io.techmeskills.an02onl_plannerapp.screen.newscreen

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.EditCardBinding
import io.techmeskills.an02onl_plannerapp.screen.main.Note
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import java.text.SimpleDateFormat
import java.util.*

class EditFragment : NavigationFragment<EditCardBinding>(R.layout.edit_card) {

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    override val viewBinding: EditCardBinding by viewBinding()
    private val calendar = Calendar.getInstance()
    private val args: EditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), DatePicker.OnDateChangedListener() {
            view, year, monthOfYear, dayOfMonth ->

        })

        viewBinding.editButton.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {
                setFragmentResult(EDIT_RESULT, Bundle().apply {
                    putParcelable(NOTE, Note(
                            if (args.note == null) -1 else args.note!!.id,
                            viewBinding.etNote.text.toString(),
                            dateFormatter.format(viewBinding.datePicker.getSelectedDate())))
                })
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), " Please, enter your note", Toast.LENGTH_LONG)
                        .show()
            }
        }

        args.note?.let { note ->
            viewBinding.etNote.setText(note.title)
            viewBinding.datePicker.setSelectedDate(note.date)
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

    private fun DatePicker.setSelectedDate(date: String?) {
        date?.let {
            dateFormatter.parse(it)?.let { date ->
                val calendar = Calendar.getInstance(Locale.getDefault())
                calendar.time = date
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                this.updateDate(year, month, day)
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
        const val EDIT_RESULT = "EDIT_RESULT"
        const val NOTE = "NOTE"
    }
}