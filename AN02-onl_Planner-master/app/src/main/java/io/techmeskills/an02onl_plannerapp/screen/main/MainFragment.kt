package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override val viewBinding: FragmentMainBinding by viewBinding()

    private val viewModel: MainViewModel by viewModel()

    private val adapter = MyRecyclerAdapter(
            onClick = :: onCardClick,
            onDelete = :: onCardDelete
    )

    private val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            viewBinding.recyclerView.scrollToPosition(0)
        }
    }

    private fun onCardClick(note: Note) {
        findNavController().navigateSafe(MainFragmentDirections.toEditFragment(note))
    }

    private fun onCardDelete(note: Note) {
        viewModel.deleteNote(note)
    }


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

        viewModel.progressLiveData.observe(this.viewLifecycleOwner) { success ->
            viewBinding.progressIndicator.isVisible = false
            val cloudResult = if (success)  R.string.fragment_main_cloud_success else R.string.fragment_main_cloud_failed
            Toast.makeText(requireContext(), cloudResult, Toast.LENGTH_LONG).show()
        }

        viewBinding.cloudImport.setOnClickListener {
            viewBinding.progressIndicator.isVisible = true
            viewModel.importNotes()
        }

        viewBinding.cloudExport.setOnClickListener {
            viewBinding.progressIndicator.isVisible = true
            viewModel.exportNotes()
        }

        viewBinding.addNewNote.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toNewFragment())
        }

        viewBinding.userSettings.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toSettingsFragment())
        }

        adapter.registerAdapterDataObserver(dataObserver)
    }

    override fun onDestroyView() {
        adapter.unregisterAdapterDataObserver(dataObserver)
        super.onDestroyView()
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbarImage.setPadding(0, top, 0, 0)
        viewBinding.toolbar.setPadding(0, top, 0, 0)
        viewBinding.recyclerView.setPadding(0, 0, 0, bottom)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}