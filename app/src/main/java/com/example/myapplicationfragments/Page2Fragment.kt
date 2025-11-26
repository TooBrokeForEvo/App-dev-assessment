package com.example.myapplicationfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView

class Page2Fragment : Fragment() {
    lateinit var viewModel: MyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = activity?.run { ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        val v = inflater.inflate(R.layout.page2_fragment, container, false)
        val valueView = v.findViewById<TextView>(R.id.textView)
        val valueObserver = Observer<String> { newValue ->
            valueView.text = newValue.toString()
        }
        viewModel.value.observe(viewLifecycleOwner, valueObserver)
        return v;
    }
}