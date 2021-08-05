package io.techmeskills.an02onl_plannerapp.screen.edit

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentEditBinding
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class EditFragment : NavigationFragment<FragmentEditBinding>(R.layout.fragment_edit) {


    override val viewBinding: FragmentEditBinding by viewBinding()
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    private var selectedColor: String = ""
    private var selectedDate: Calendar = Calendar.getInstance().apply { time = Date() }
    private val args: EditFragmentArgs by navArgs()
    private val viewModel: EditFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.colorSet.attachBrightnessSlider(viewBinding.brightnessSlider)

        viewBinding.timePicker.setIs24HourView(true)

        viewBinding.colorSet.setColorListener(ColorEnvelopeListener {
                envelope, _ ->  selectedColor = "#" + envelope.hexCode
        })

        viewBinding.etNote.setText(args.note?.title)

        viewBinding.editButton.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {

                args.note?.let { viewModel.updateNote(
                        Note(
                                id = it.id,
                                title = viewBinding.etNote.text.toString(),
                                date = selectedDate.timeInMillis,
                                userName = it.userName,
                                notificationOn = viewBinding.notificationCheck.isChecked,
                                dateOfBirth = calendar.timeInMillis,
                                backgroundColor = selectedColor,
                                notePinned = it.notePinned
                        )
                )
                }
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please, enter your note", Toast.LENGTH_LONG)
                    .show()
            }
        }

        viewBinding.shareButton.setColorFilter(Color.WHITE)

        viewBinding.shareButton.setOnClickListener {
            if (viewBinding.etNote.text.isNotBlank()) {
                val shareIntent = Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Заметка: ${viewBinding.etNote.text}, дата: ${dateFormatter.format(selectedDate.timeInMillis)}")
                    type = "text/plain"
                }, null)
                startActivity(shareIntent)
            } else {
                Toast.makeText(requireContext(), "Please, enter your note", Toast.LENGTH_LONG)
                    .show()
            }
        }

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

