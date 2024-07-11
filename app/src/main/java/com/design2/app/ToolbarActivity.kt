package com.design2.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.design2.app.databinding.ActivityToolbarBinding
import com.design2.chili2.view.navigation_components.ChiliToolbar
import com.design2.chili2.view.navigation_components.StartIconChiliToolbar
import kg.devcats.chili3.view.toolbar.TailedToolbarView
import kg.devcats.chili3.view.navigation_components.CircleStartIconChiliToolbar

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

        vb.tailedToolbar.apply {
            initToolbar(
                TailedToolbarView.Configuration(
                    hostActivity = this@ToolbarActivity,
                    isNavigateUpButtonEnabled = true,
                    onNavigateUpClick = { onBackPressed() },
                )
            )
            setupDividerVisibility(false)
            setTagTitle("Бонусы ддддддд")
            setOnTagClickListener {
                Toast.makeText(context, "Clicked on tag", Toast.LENGTH_SHORT).show()
            }
        }

        vb.toolbar9.initToolbar(
            CircleStartIconChiliToolbar.Configuration(
                hostActivity = this,
                title = "Title",
                startIcon = R.drawable.cat_204_192,
                endIconPrimary = R.drawable.ic_search_filled,
                endIconSecondary = R.drawable.ic_notification_with_events,
                onClick = {
                    when (it) {
                        CircleStartIconChiliToolbar.ClickableElementType.PROFILE_CONTAINER -> {}
                        CircleStartIconChiliToolbar.ClickableElementType.END_ICON -> {}
                        CircleStartIconChiliToolbar.ClickableElementType.ADDITIONAL_END_ICON -> {}
                    }
                },
            )
        )

    }
}