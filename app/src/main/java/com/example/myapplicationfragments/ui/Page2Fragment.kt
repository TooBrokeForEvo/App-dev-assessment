package com.example.myapplicationfragments.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.myapplicationfragments.viewModel.MyViewModel
import com.example.myapplicationfragments.R
import com.example.myapplicationfragments.db.DiaryDatabase
import com.example.myapplicationfragments.db.DiaryEntry
import kotlinx.coroutines.launch

class Page2Fragment : Fragment() {
    lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.page2_fragment, container, false)

        // Use the inflated view 'v' to find your UI elements
        //val valueView = v.findViewById<TextView>(R.id.textView)
        val dateText = v.findViewById<TextView>(R.id.dateText)
        val entryInput = v.findViewById<EditText>(R.id.entryInput)
        val clearButton = v.findViewById<Button>(R.id.clearButton)
        val saveButton = v.findViewById<Button>(R.id.saveButton)

        val valueObserver = Observer<String> { newValue ->
            //valueView.text = newValue.toString()
        }

        // display date from ViewModel
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            dateText.text = "Date: $date"
        }

        clearButton.setOnClickListener {
            entryInput.setText("")
        }

        saveButton.setOnClickListener {
            val text = entryInput.text.toString()
            val date = viewModel.selectedDate.value ?: "Unknown"

            val entry = DiaryEntry(date = date, text = text)
            val db = DiaryDatabase.getDatabase(requireContext())

            lifecycleScope.launch {
                db.diaryDao().insertEntry(entry)
            }
        }

        viewModel.value.observe(viewLifecycleOwner, valueObserver)

        // Return the inflated view
        return v
    }
}