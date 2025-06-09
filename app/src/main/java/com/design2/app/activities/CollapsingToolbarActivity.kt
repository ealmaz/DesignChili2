package com.design2.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.design2.app.databinding.ActivityCollapsingToolbarBinding
import kg.devcats.chili3.view.toolbar.CollapsingChiliToolbar

class CollapsingToolbarActivity : AppCompatActivity() {

    private var _binding: ActivityCollapsingToolbarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCollapsingToolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            collapsingToolbar.setupCollapsingToolbar(
                collapseToolbarConfig = CollapsingChiliToolbar.Configuration(
                    hostActivity = this@CollapsingToolbarActivity,
                    title = "Collapsing Toolbar",
                    scrollView = scrollView,
                    triggerTextView = amountText,
                    isNavigateUpButtonEnabled = true,
                    onNavigateUpClick = { onBackPressed() },
                )
            )
        }
    }
}