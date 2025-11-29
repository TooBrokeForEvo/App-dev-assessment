package com.example.myapplicationfragments.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplicationfragments.db.DiaryDatabase
import com.example.myapplicationfragments.db.DiaryEntry
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {

    val selectedDate: MutableLiveData<String> = MutableLiveData()

    private val dao = DiaryDatabase.getDatabase(application).diaryDao()
    val diaryEntries: LiveData<List<DiaryEntry>> = dao.getAllEntriesLive()

    fun insert(entry: DiaryEntry) {
        viewModelScope.launch {
            dao.insert(entry)
        }
    }

    fun delete(entry: DiaryEntry) {
        viewModelScope.launch {
            dao.delete(entry)
        }
    }

    fun update(entry: DiaryEntry) = viewModelScope.launch {
        dao.update(entry)
    }
}