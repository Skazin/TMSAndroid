package io.techmeskills.an02onl_plannerapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import io.techmeskills.an02onl_plannerapp.databinding.ActivityMainBinding
import io.techmeskills.an02onl_plannerapp.support.SupportActivityInset
import io.techmeskills.an02onl_plannerapp.support.setWindowTransparency

const val PREFS_NAME = "theme_prefs"
const val KEY_THEME = "prefs.theme"
const val THEME_UNDEFINED = -1
const val THEME_LIGHT = 0
const val THEME_DARK = 1

class MainActivity : SupportActivityInset<ActivityMainBinding>() {

    override lateinit var viewBinding: ActivityMainBinding

    private val navHostFragment by lazy { (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment) }

    private val navController: NavController by lazy { navHostFragment.navController }

    private val sharedPrefs by lazy {  getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTheme()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setWindowTransparency(this)
    }

    override fun getActiveFragment(): Fragment? {
        return navHostFragment.childFragmentManager.fragments[0]
    }

    override fun onBackPressed() {
        if (navController.previousBackStackEntry != null) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    private fun initTheme() {
        when (getSavedTheme()) {
            THEME_LIGHT ->  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun getSavedTheme() = sharedPrefs.getInt(KEY_THEME, THEME_UNDEFINED)
}