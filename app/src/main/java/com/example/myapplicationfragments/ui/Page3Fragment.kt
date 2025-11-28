package com.example.myapplicationfragments.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.widget.ListView;
import androidx.lifecycle.lifecycleScope
import com.example.myapplicationfragments.viewModel.MyViewModel
import com.example.myapplicationfragments.R
import com.example.myapplicationfragments.db.DiaryDatabase
import kotlinx.coroutines.launch

class Page3Fragment : Fragment() {

    lateinit var viewModel: MyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]
        val view = inflater.inflate(R.layout.page3_fragment, container, false)

        val listView = view.findViewById<ListView>(R.id.diaryList)

        viewModel.diaryEntries.observe(viewLifecycleOwner) { entries ->
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, entries)
            listView.adapter = adapter
        }

        lifecycleScope.launch {
            val db = DiaryDatabase.getDatabase(requireContext())
            val list = db.diaryDao().getAllEntries()

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                list.map { "${it.date}: ${it.text}" }
            )
            listView.adapter = adapter
        }

        return view
    }
}