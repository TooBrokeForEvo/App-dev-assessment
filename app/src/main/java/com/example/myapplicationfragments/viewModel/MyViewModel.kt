package com.example.myapplicationfragments.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    private val _value = MutableLiveData<String>()

    val selectedDate = MutableLiveData<String>()
    val diaryText = MutableLiveData<String>()
    val diaryEntries = MutableLiveData<MutableList<String>>(mutableListOf())

    val value: MutableLiveData<String>
        get() = _value
    init {
        _value.value = "default"
    }
}