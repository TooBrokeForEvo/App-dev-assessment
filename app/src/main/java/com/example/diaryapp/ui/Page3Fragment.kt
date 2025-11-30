package com.example.diaryapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diaryapp.R
import com.example.diaryapp.adapter.DiaryAdapter
import com.example.diaryapp.adapter.DiaryItemDetailsLookup
import com.example.diaryapp.adapter.DiaryItemKeyProvider
import com.example.diaryapp.db.DiaryEntry
import com.example.diaryapp.viewModel.MyViewModel

/**
 * A fragment that displays a list of diary entries with search and multi-select functionality.
 * Users can view, edit, and delete entries from this screen.
 */
class Page3Fragment : Fragment() {

    private lateinit var viewModel: MyViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DiaryAdapter
    private var tracker: SelectionTracker<Long>? = null
    private lateinit var searchView: SearchView
    private var allEntries: List<DiaryEntry> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.page3_fragment, container, false)
        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        setupRecyclerView(view)
        setupSelectionTracker()
        setupButtons(view)
        setupSearchView(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.diaryEntries.observe(viewLifecycleOwner) { entries ->
            allEntries = entries
            filterEntries(searchView.query.toString())
        }
    }

    /**
     * Sets up the SearchView and its listeners to filter the diary entry list.
     * @param view The fragment's root view.
     */
    private fun setupSearchView(view: View) {
        searchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterEntries(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterEntries(newText)
                return true
            }
        })
    }

    /**
     * Filters the `allEntries` list based on the user's query and submits the result to the adapter.
     * The search matches against the entry's text and date. It also supports the "has:image" keyword.
     * @param query The text to search for.
     */
    private fun filterEntries(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            allEntries
        } else {
            val lowerCaseQuery = query.lowercase()
            allEntries.filter { entry ->
                val matchesText = entry.text.lowercase().contains(lowerCaseQuery)
                val matchesDate = entry.date.lowercase().contains(lowerCaseQuery)
                val matchesImage = if ("has:image".contains(lowerCaseQuery)) {
                    entry.imagePath != null
                } else {
                    false
                }
                matchesText || matchesDate || matchesImage
            }
        }
        adapter.submitList(filteredList)
        tracker?.clearSelection()
    }

    /**
     * Initializes the RecyclerView, its LayoutManager, and the DiaryAdapter.
     * Sets up the click listeners for viewing and editing individual entries.
     * @param view The fragment's root view containing the RecyclerView.
     */
    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.diaryList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = DiaryAdapter(
            onClick = { entry ->
                val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_view_entry, null)
                val titleTextView = dialogView.findViewById<TextView>(R.id.dialogTitle)
                val textTextView = dialogView.findViewById<TextView>(R.id.dialogText)
                val imageView = dialogView.findViewById<ImageView>(R.id.dialogImage)

                titleTextView.text = entry.date
                textTextView.text = entry.text

                if (entry.imagePath != null) {
                    imageView.visibility = View.VISIBLE
                    Glide.with(this).load(entry.imagePath).into(imageView)
                } else {
                    imageView.visibility = View.GONE
                }

                AlertDialog.Builder(requireContext())
                    .setView(dialogView)
                    .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
                    .show()
            },
            onEditClick = { entryToEdit ->
                val editText = EditText(requireContext())
                editText.setText(entryToEdit.text)

                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Edit Entry")
                    .setView(editText)
                    .setPositiveButton("Save") { _, _ ->
                        val newText = editText.text.toString()
                        if (newText.isNotBlank()) {
                            viewModel.update(entryToEdit.copy(text = newText))
                        } else {
                            Toast.makeText(context, "Entry cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancel", null)

                if (entryToEdit.imagePath != null) {
                    builder.setNeutralButton("Remove Image") { _, _ ->
                        val newText = editText.text.toString()
                        if (newText.isNotBlank()) {
                            val updatedEntry = entryToEdit.copy(text = newText, imagePath = null)
                            viewModel.update(updatedEntry)
                            Toast.makeText(context, "Image removed and text saved", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Entry text cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                builder.show()
            }
        )
        recyclerView.adapter = adapter
    }

    /**
     * Initializes the SelectionTracker for multi-select functionality in the RecyclerView.
     */
    private fun setupSelectionTracker() {
        tracker = SelectionTracker.Builder(
            "diary-selection-id",
            recyclerView,
            DiaryItemKeyProvider(adapter),
            DiaryItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.tracker = tracker
    }

    /**
     * Sets up the click listeners for the global "Delete" and "Edit" buttons.
     * @param view The fragment's root view containing the buttons.
     */
    private fun setupButtons(view: View) {
        view.findViewById<View>(R.id.deleteButton).setOnClickListener {
            handleDeleteSelection()
        }
        view.findViewById<View>(R.id.editButton).setOnClickListener {
            handleEditSelection()
        }
    }

    /**
     * Handles the deletion of one or more selected diary entries.
     */
    private fun handleDeleteSelection() {
        val selection = tracker?.selection
        if (selection == null || selection.isEmpty) {
            Toast.makeText(context, "Please select an entry to delete", Toast.LENGTH_SHORT).show()
            return
        }
        val entriesToDelete = allEntries.filter { selection.contains(it.id.toLong()) }
        entriesToDelete.forEach { viewModel.delete(it) }

        Toast.makeText(context, "Deleted ${selection.size()} entries", Toast.LENGTH_SHORT).show()
        tracker?.clearSelection()
    }

    /**
     * Handles the editing of a single selected diary entry.
     */
    private fun handleEditSelection() {
        val selection = tracker?.selection
        if (selection == null || selection.size() != 1) {
            Toast.makeText(context, "Select ONE entry to edit", Toast.LENGTH_SHORT).show()
            return
        }
        val selectedId = selection.first()
        val entry = allEntries.find { it.id.toLong() == selectedId }
        entry?.let { entryToEdit ->
            val editText = EditText(requireContext())
            editText.setText(entryToEdit.text)

            val builder = AlertDialog.Builder(requireContext())
                .setTitle("Edit Entry")
                .setView(editText)
                .setPositiveButton("Save") { _, _ ->
                    val newText = editText.text.toString()
                    if (newText.isNotBlank()) {
                        viewModel.update(entryToEdit.copy(text = newText))
                    } else {
                        Toast.makeText(context, "Entry cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)

            if (entryToEdit.imagePath != null) {
                builder.setNeutralButton("Remove Image") { _, _ ->
                    val newText = editText.text.toString()
                    if (newText.isNotBlank()) {
                        val updatedEntry = entryToEdit.copy(text = newText, imagePath = null)
                        viewModel.update(updatedEntry)
                        Toast.makeText(context, "Image removed and text saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Entry text cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            builder.show()
        }
    }
}
