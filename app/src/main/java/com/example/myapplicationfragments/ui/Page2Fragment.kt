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
import android.widget.Toast
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

        val v = inflater.inflate(R.layout.page2_fragment, container, false)


        val dateText = v.findViewById<TextView>(R.id.dateText)
        val entryInput = v.findViewById<EditText>(R.id.entryInput)
        val clearButton = v.findViewById<Button>(R.id.clearButton)
        val saveButton = v.findViewById<Button>(R.id.saveButton)

        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            dateText.text = "Date: $date"
        }

        clearButton.setOnClickListener {
            entryInput.setText("")
        }

        saveButton.setOnClickListener {
            val text = entryInput.text.toString()
            if (text.isBlank()) {
                Toast.makeText(requireContext(), "Entry cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val date = viewModel.selectedDate.value ?: "Unknown"
            val entry = DiaryEntry(date = date, text = text)
            viewModel.insert(entry)
            Toast.makeText(requireContext(), "Entry saved!", Toast.LENGTH_SHORT).show()
            entryInput.setText("")
        }

        return v
    }
}