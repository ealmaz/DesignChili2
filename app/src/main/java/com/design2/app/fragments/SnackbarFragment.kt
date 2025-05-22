package com.design2.app.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.design2.app.MainActivity
import com.design2.app.R
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentSnackbarsBinding
import com.design2.chili2.extensions.showInfinitiveLoaderSnackbar
import com.design2.chili2.extensions.showSimpleSnackbar
import com.design2.chili2.extensions.showTimerActionBeforeSuccessCnackbar
import com.design2.chili2.extensions.showTimerSnackbar
import com.design2.chili2.view.snackbar.ChiliSnackBar

class SnackbarFragment : BaseFragment<FragmentSnackbarsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        vb.loadSnackbar.setOnClickListener {
            (requireActivity() as AppCompatActivity).showInfinitiveLoaderSnackbar(vb.root, "Snackbar meesage")
        }
        vb.timerActionSnackbar.setOnClickListener {
            showTimerSnackbar("Timer message bla bla bla", {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
                it.dismiss()
            }, {
                Toast.makeText(requireContext(), "Timer expired", Toast.LENGTH_SHORT).show()
            })
        }

        vb.simple.setOnClickListener {
            (requireActivity() as AppCompatActivity).showSimpleSnackbar(vb.root, "Hello")
        }
        vb.customWarining.setOnClickListener {
            ChiliSnackBar.Builder(vb.root)
                .setSnackbarIcon(R.drawable.ic_cat)
                .setSnackbarDurationMills(2000)
                .setSnackbarMessage("Warning message")
                .build()
                .show()

        }
        vb.onTopGravity.setOnClickListener {
            ChiliSnackBar.Builder(vb.root)
                .setSnackbarIcon(R.drawable.chili_ic_orange_warning)
                .setSnackbarMessage("Похоже пропал интернет. \nПроверьте интернет-соединение")
                .setSnackbarDurationMills(2000)
                .setGravity(Gravity.TOP)
                .build()
                .show()
        }
    }

    override fun inflateViewBinging(): FragmentSnackbarsBinding {
        return FragmentSnackbarsBinding.inflate(layoutInflater)
    }

}