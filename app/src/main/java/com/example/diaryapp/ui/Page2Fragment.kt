package com.example.diaryapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.diaryapp.viewModel.MyViewModel
import com.example.diaryapp.R
import com.example.diaryapp.db.DiaryEntry
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * A [Fragment] where the user can write and save a new diary entry.
 * It observes the selected date from the shared [MyViewModel] and provides
 * fields to input text and buttons to save or clear the entry.
 */
class Page2Fragment : Fragment() {
    lateinit var viewModel: MyViewModel

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.newEntryImageUri.value = uri
    }


    /**
     * Called to create the view for this fragment. This is where the layout is inflated
     * and all UI logic, such as observers and click listeners, is set up.
     */
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
        val addImageButton = v.findViewById<Button>(R.id.addImageButton)
        val imagePreview = v.findViewById<ImageView>(R.id.image_preview)



        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            dateText.text = "Date: $date"
        }

        viewModel.newEntryImageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                imagePreview.visibility = View.VISIBLE
                Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .into(imagePreview)
            } else {
                imagePreview.visibility = View.GONE
            }
        }

        clearButton.setOnClickListener {
            entryInput.setText("")
            viewModel.newEntryImageUri.value = null
            Toast.makeText(context, "Cleared", Toast.LENGTH_SHORT).show()
        }

        addImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        saveButton.setOnClickListener {
            val text = entryInput.text.toString()
            if (text.isBlank()) {
                Toast.makeText(requireContext(), "Entry cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val date = viewModel.selectedDate.value ?: "Unknown"
            val imageUri = viewModel.newEntryImageUri.value

            val imagePath = if (imageUri != null) {
                saveImageToInternalStorage(imageUri)
            } else {
                null
            }

            val entry = DiaryEntry(date = date, text = text, imagePath = imagePath)
            viewModel.insert(entry)

            Toast.makeText(requireContext(), "Entry saved!", Toast.LENGTH_SHORT).show()
            entryInput.setText("")
            viewModel.newEntryImageUri.value = null
        }

        return v
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val fileName = "${UUID.randomUUID()}.jpg"
            val file = File(requireContext().filesDir, fileName)
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.newEntryImageUri.value = null
    }
}
