package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override val viewBinding: FragmentMainBinding by viewBinding()

    private val viewModel: MainViewModel by viewModel()

    var count1: Int = 0
        set(value) {
            field = value
            viewBinding.tvCount1.text = field.toString()
        }

    var count2: Int = 0
        set(value) {
            field = value
            viewBinding.tvCount2.text = field.toString()
        }

    var count3: Int = 0
        set(value) {
            field = value
            viewBinding.tvCount3.text = field.toString()
        }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.btnClicker1.setOnClickListener {
            count1++
        }
        viewBinding.btnClicker2.setOnClickListener {
            count2++
        }
        viewBinding.btnClicker3.setOnClickListener {
            count3 = count1 + count2
        }
    }
}