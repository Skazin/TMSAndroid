package io.techmeskills.an02onl_plannerapp.screen.usersettings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.EditUserFragmentBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.koin.android.viewmodel.ext.android.viewModel

class UserSettingsFragment : NavigationFragment<EditUserFragmentBinding>(R.layout.edit_user_fragment) {

    override val viewBinding: EditUserFragmentBinding by viewBinding()
    private val viewModel: UserSettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.deleteUser.setOnClickListener {
            showDeleteDialog()
        }

        viewBinding.exit.setOnClickListener {
            showDialogLogout()
        }

        viewModel.currentUserNameLiveData.observe(this.viewLifecycleOwner) {
            viewBinding.userName.text = it.toString()
        }

        viewBinding.btnChangeName.setOnClickListener {
            if(viewBinding.changeName.text.isNotBlank()) {
                    viewModel.updateUser(viewBinding.changeName.text.toString())
            } else {
                Toast.makeText(requireContext(), "Please, enter new user's name", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setVerticalMargin(marginTop = top)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }

        }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_user_request_title)
                .setMessage(R.string.pick_delete_action)
                .setPositiveButton(R.string.YES) { dialog, _ ->
                    viewModel.deleteUser()
                    dialog.cancel()
                    findNavController().navigateSafe(UserSettingsFragmentDirections.toLoginFragment())
                }.setNegativeButton(R.string.NO) { dialog, _ ->
                    dialog.cancel()
                }.show()
    }

    private fun showDialogLogout() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.logout_request_title)
                .setMessage(R.string.pick_action)
                .setPositiveButton(R.string.YES) { dialog, _ ->
                    viewModel.logout()
                    findNavController().navigateSafe(UserSettingsFragmentDirections.toLoginFragment())
                    dialog.cancel()
                }.setNegativeButton(R.string.NO) { dialog, _ ->
                    dialog.cancel()
                }.show()
    }
}