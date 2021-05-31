package io.techmeskills.an02onl_plannerapp.screen.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentSettingsBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : NavigationFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    override val viewBinding: FragmentSettingsBinding by viewBinding()
    private val viewModel: SettingsViewModel by viewModel()

    @ExperimentalCoroutinesApi
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
                    viewBinding.changeName.text.clear()
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
                .setTitle(R.string.fragment_settings_delete_user_request_title)
                .setMessage(R.string.fragment_settings_pick_delete_action)
                .setPositiveButton(R.string.fragment_settings_action_YES) { dialog, _ ->
                    viewModel.deleteUser()
                    dialog.cancel()
                    findNavController().navigateSafe(SettingsFragmentDirections.toLoginFragment())
                }.setNegativeButton(R.string.fragment_settings_action_NO) { dialog, _ ->
                    dialog.cancel()
                }.show()
    }

    private fun showDialogLogout() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.fragment_settings_logout_request_title)
                .setMessage(R.string.fragment_settings_login_request_title)
                .setPositiveButton(R.string.fragment_settings_action_YES) { dialog, _ ->
                    viewModel.logout()
                    findNavController().navigateSafe(SettingsFragmentDirections.toLoginFragment())
                    dialog.cancel()
                }.setNegativeButton(R.string.fragment_settings_action_NO) { dialog, _ ->
                    dialog.cancel()
                }.show()
    }
}