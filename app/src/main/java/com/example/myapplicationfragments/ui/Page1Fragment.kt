package com.example.myapplicationfragments.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.widget.Button
import android.widget.EditText
import com.example.myapplicationfragments.viewModel.MyViewModel
import com.example.myapplicationfragments.R
import java.util.Calendar


class Page1Fragment : Fragment() {
    lateinit var viewModel: MyViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        val view = inflater.inflate(R.layout.page1_fragment, container, false)
        val btnPickDate = view.findViewById<Button>(R.id.pickDateButton)

        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, y, m, d ->
                viewModel.selectedDate.value = "$d/${m + 1}/$y"
            }, year, month, day).show()
        }

        return view
    }
}