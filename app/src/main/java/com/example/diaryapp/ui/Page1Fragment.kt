package com.example.diaryapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.widget.Button
import com.example.diaryapp.viewModel.MyViewModel
import com.example.diaryapp.R
import java.util.Calendar
import android.widget.TextView

/**
 * A [Fragment] responsible for allowing the user to select a date.
 * This fragment displays the currently selected date and provides a button
 * to open a [DatePickerDialog]. The selected date is shared with other fragments
 * through a shared [MyViewModel].
 */
class Page1Fragment : Fragment() {
    private lateinit var viewModel: MyViewModel

    /**
     * Called to create the view for this fragment.
     * This is where the layout is inflated and UI components are initialized.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity for ViewModel")

        val view = inflater.inflate(R.layout.page1_fragment, container, false)

        val btnPickDate = view.findViewById<Button>(R.id.pickDateButton)
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val initialDate = "$day/${month + 1}/$year"
        dateTextView.text = initialDate
        viewModel.selectedDate.value = initialDate

        viewModel.selectedDate.observe(viewLifecycleOwner) { newDate ->
            if (dateTextView.text.toString() != newDate) {
                dateTextView.text = newDate
            }
        }

        btnPickDate.setOnClickListener {
            val c = Calendar.getInstance()
            val y = c.get(Calendar.YEAR)
            val m = c.get(Calendar.MONTH)
            val d = c.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, selectedYear, monthOfYear, dayOfMonth ->
                val selectedDateStr = "$dayOfMonth/${monthOfYear + 1}/$selectedYear"

                viewModel.selectedDate.value = selectedDateStr
            }, y, m, d).show()
        }

        return view
    }
}
