package com.design2.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.design2.app.databinding.ActivityToolbarBinding
import com.design2.chili2.view.navigation_components.ChiliToolbar
import com.design2.chili2.view.navigation_components.StartIconChiliToolbar

class ToolbarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vb = ActivityToolbarBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.toolbar.initToolbar(ChiliToolbar.Configuration(
            this,
            title = "Transparent Toolbar",
            onNavigateUpClick = { onBackPressed() },
            isNavigateUpButtonEnabled = true
        ))

        vb.toolbar2.initToolbar(ChiliToolbar.Configuration(
            this,
            onNavigateUpClick = { onBackPressed() },
        ))

        vb.toolbar4.initToolbar(ChiliToolbar.Configuration(
            this,
            onNavigateUpClick = { onBackPressed() },
            isNavigateUpButtonEnabled = true
        ))

        vb.toolbar5.initToolbar(ChiliToolbar.Configuration(
            this,
            onNavigateUpClick = { onBackPressed() },
        ))

        vb.toolbar6.initToolbar(ChiliToolbar.Configuration(
            this,
            onNavigateUpClick = { onBackPressed() },
        ))

        vb.toolbar7.initToolbar(ChiliToolbar.Configuration(
            this,
            onNavigateUpClick = { onBackPressed() },
        ))

        vb.toolbar3.initToolbar(ChiliToolbar.Configuration(
            this,
            isNavigateUpButtonEnabled = true,
        ))

        vb.toolbar8.initToolbar(StartIconChiliToolbar.Configuration(
            hostActivity = this,
            onNavigateUpClick = { onBackPressed() },
        ))
    }
}