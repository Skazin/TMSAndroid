package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.screen.newscreen.NewFragment
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override val viewBinding: FragmentMainBinding by viewBinding()

    private val viewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listLiveData.observe(this.viewLifecycleOwner, {
            viewBinding.recyclerView.adapter = CustomRecyclerAdapter(it)
        })

        setFragmentResultListener(NewFragment.ADD_NEW_RESULT) { key, bundle ->
            val note = bundle.getString(NewFragment.TEXT)
            val date = bundle.getString(NewFragment.DATE)
            note?.let {
                viewModel.adding(it, date)
            }
        }

        viewBinding.btnAdd.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toNewFragment())
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, top, 0, 0)
        viewBinding.recyclerView.setPadding(0, 0, 0, bottom)
        viewBinding.btnAdd.setVerticalMargin(marginBottom = bottom)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}