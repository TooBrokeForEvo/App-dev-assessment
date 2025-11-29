package com.example.myapplicationfragments.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationfragments.R
import com.example.myapplicationfragments.adapter.DiaryAdapter
import com.example.myapplicationfragments.adapter.ItemLookup
import com.example.myapplicationfragments.viewModel.MyViewModel

class Page3Fragment : Fragment() {

    private lateinit var viewModel: MyViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DiaryAdapter
    private var tracker: SelectionTracker<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.page3_fragment, container, false)
        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        recyclerView = view.findViewById(R.id.diaryList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = DiaryAdapter { entry ->
            AlertDialog.Builder(requireContext())
                .setTitle("Diary Entry")
                .setMessage(entry.text)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
        }
        recyclerView.adapter = adapter

        tracker = SelectionTracker.Builder(
            "diarySelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            ItemLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.tracker = tracker

        // Buttons
        view.findViewById<View>(R.id.deleteButton).setOnClickListener {
            handleDeleteSelection()
        }
        view.findViewById<View>(R.id.editButton).setOnClickListener {
            handleEditSelection()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.diaryEntries.observe(viewLifecycleOwner) { entries ->
            adapter.submitList(entries) {
                tracker?.clearSelection() // <--- THIS FIXES THE CRASH
            }
        }
    }

    private fun handleDeleteSelection() {
        val selection = tracker?.selection
        if (selection == null || selection.isEmpty) {
            Toast.makeText(context, "Please select an entry to delete", Toast.LENGTH_SHORT).show()
            return
        }
        selection.forEach { id ->
            val entry = adapter.currentList.find { it.id.toLong() == id }
            entry?.let { viewModel.delete(entry) }
        }
        Toast.makeText(context, "Deleted ${selection.size()} entries", Toast.LENGTH_SHORT).show()
        tracker?.clearSelection()
    }

    private fun handleEditSelection() {
        val selection = tracker?.selection
        if (selection == null || selection.size() != 1) {
            Toast.makeText(context, "Select ONE entry to edit", Toast.LENGTH_SHORT).show()
            return
        }
        val selectedId = selection.first()
        val entry = adapter.currentList.find { it.id.toLong() == selectedId }
        entry?.let {
            val editText = android.widget.EditText(requireContext())
            editText.setText(it.text)
            AlertDialog.Builder(requireContext())
                .setTitle("Edit Entry")
                .setView(editText)
                .setPositiveButton("Save") { dialog, _ ->
                    val newText = editText.text.toString()
                    if (newText.isNotBlank()) {
                        it.text = newText
                        viewModel.update(it)
                    } else {
                        Toast.makeText(context, "Entry cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}
