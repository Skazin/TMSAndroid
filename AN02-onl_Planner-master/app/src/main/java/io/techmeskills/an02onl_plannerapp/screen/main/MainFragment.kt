package io.techmeskills.an02onl_plannerapp.screen.main

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.repositories.Result
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override val viewBinding: FragmentMainBinding by viewBinding()

    @ExperimentalCoroutinesApi
    private val viewModel: MainViewModel by viewModel()

    @ExperimentalCoroutinesApi
    private val adapter = MyRecyclerAdapter(
            onClick = :: onCardClick,
            onDelete = :: onCardDelete,
            onPin = :: onCardPin
    )

    private var cloudResult = 0

    private val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            viewBinding.recyclerView.scrollToPosition(0)
        }
    }

    private fun onCardClick(note: Note) {
        findNavController().navigateSafe(MainFragmentDirections.toEditFragment(note))
    }

    @ExperimentalCoroutinesApi
    private fun onCardPin(note: Note) {
        viewModel.pinNote(note)
    }

    @ExperimentalCoroutinesApi
    private fun onCardDelete(note: Note) {
        viewModel.deleteNote(note)
    }


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         viewBinding.recyclerView.adapter = adapter
         viewModel.listLiveData.observe(this.viewLifecycleOwner) {
             adapter.submitList(it)
         }

         val swipeHandler = object : SwipeToDeleteCallback(
            ContextCompat.getDrawable(requireContext(), R.drawable.delete_background),
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.swipeDelete(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(viewBinding.recyclerView)

        viewBinding.sortCards.setOnClickListener { it ->
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.AlphabetAZ -> {
                        viewModel.filterByAlphabetAZ()
                        true
                    }
                    R.id.AlphabetZA -> {
                        viewModel.filterByAlphabetZA()
                        true
                    }
                    R.id.Date19 -> {
                        viewModel.filterByDate19()
                        true
                    }
                    R.id.Date91 -> {
                        viewModel.filterByDate91()
                        true
                    }
                    R.id.ByAdding19 -> {
                        viewModel.filterByAdding19()
                        true
                    }
                    R.id.ByAdding91 -> {
                        viewModel.filterByAdding91()
                        true
                    }
                    else -> false
                    }
                }
            popupMenu.show()
            }

        viewBinding.navigationBar.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.Add -> {
                    findNavController().navigateSafe(MainFragmentDirections.toNewFragment())
                    true
                }
                R.id.Settings -> {
                    findNavController().navigateSafe(MainFragmentDirections.toSettingsFragment())
                    true
                }
                R.id.Cloud -> {
                    showCloudDialog()
                    true
                }
                else -> false
            }
        }

        viewBinding.searchCard.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterNotes(newText)
                return false
            }
        })

        adapter.registerAdapterDataObserver(dataObserver)

        viewModel.transferLiveData.observe(this.viewLifecycleOwner) { success ->
            viewBinding.progressIndicator.isVisible = false
            cloudResult = when(success) {
                Result.NO_NOTES_IMPORT -> R.string.fragment_main_cloud_import_no_notes
                Result.NO_NEW_NOTES_IMPORT -> R.string.fragment_main_cloud_import_no_new_notes
                Result.ALL_GOOD_IMPORT -> R.string.fragment_main_cloud_import_success
                Result.NO_NOTES_EXPORT -> R.string.fragment_main_cloud_export_no_notes
                Result.ALL_GOOD_EXPORT -> R.string.fragment_main_cloud_export_success
            }
            Toast.makeText(requireContext(), cloudResult, Toast.LENGTH_LONG).show()
        }
    }

    @ExperimentalCoroutinesApi
    private fun showCloudDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.fragment_main_cloud_dialog)
            .setMessage(R.string.fragment_main_cloud_choose_cloud_action)
            .setPositiveButton(R.string.fragment_main_cloud_export) { dialog, _ ->
                viewBinding.progressIndicator.isVisible = true
                viewModel.exportNotes()
                dialog.cancel()
            }.setNegativeButton(R.string.fragment_main_cloud_import) { dialog, _ ->
                viewBinding.progressIndicator.isVisible = true
                viewModel.importNotes()
                dialog.cancel()
            }.show()
    }

    @ExperimentalCoroutinesApi
    override fun onDestroyView() {
        adapter.unregisterAdapterDataObserver(dataObserver)
        super.onDestroyView()
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbarImage.setVerticalMargin(marginTop = top)
        viewBinding.toolbar.setPadding(0, top, 0, 0)
        viewBinding.navigationBar.setPadding(0, 0, 0, bottom)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}