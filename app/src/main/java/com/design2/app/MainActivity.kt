package com.design2.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import com.design2.app.base.BaseFragment
import com.design2.app.fragments.AllViewsFragment
import com.design2.chili2.view.navigation_components.ChiliToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: ChiliToolbar

    private var isShimmering = false
    private var isDarkTheme = false
        @Synchronized get() = field
        @Synchronized set(value) {field = value}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AllViewsFragment())
                .commit()
        }
    }

    private fun setupToolbar() {
        toolbar = findViewById<ChiliToolbar>(R.id.toolbar)
        toolbar.initToolbar(ChiliToolbar.Configuration(
            this,
            onNavigateUpClick = { onBackPressed() },
            isNavigateUpButtonEnabled = false
        ))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.theme -> setupDarkTheme()
            R.id.shimmer -> setupShimmer(!isShimmering)
        }
        return true
    }

    private fun setupDarkTheme() {
        when (!isDarkTheme) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                isDarkTheme = true
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                isDarkTheme = false
            }
        }
    }

    private fun setupShimmer(isShimmering: Boolean) {
        this.isShimmering = isShimmering
        (supportFragmentManager.findFragmentById(R.id.container) as BaseFragment<*>).apply {
            if (isShimmering) startShimmering()
            else stopShimmering()
        }
    }

    fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun setTitle(title: String) {
        toolbar.setTitle(title)
    }

    fun setUpHomeEnabled(isEnabled: Boolean) {
        toolbar.isUpHomeEnabled(this, isEnabled)
    }
}