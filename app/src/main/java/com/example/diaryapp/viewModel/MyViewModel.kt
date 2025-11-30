package com.example.diaryapp.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.db.DiaryDatabase
import com.example.diaryapp.db.DiaryEntry
import kotlinx.coroutines.launch


/**
 * A ViewModel that provides data to the UI and survives configuration changes.
 * It acts as a communication center between the database (via the DAO) and the UI (Fragments).
 *
 * Inheriting from [AndroidViewModel] allows us to get an application context,
 * which is needed to initialize the database.
 *
 * @param application The application context provided by the framework.
 */
class MyViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * [MutableLiveData] to hold the currently selected date.
     * This allows the date to be shared and observed between Page1Fragment and Page2Fragment.
     * It is 'mutable' because its value can be changed.
     */
    val selectedDate: MutableLiveData<String> = MutableLiveData()

    val newEntryImageUri = MutableLiveData<Uri?>(null)

    private val dao = DiaryDatabase.getDatabase(application).diaryDao()

    /**
     * [LiveData] holding the list of all diary entries from the database.
     * Fragments (like Page3Fragment) can observe this LiveData.
     * When the data in the 'diary_table' changes, LiveData will automatically notify
     * the observers with the new list. This is an immutable LiveData exposed to the UI.
     */
    val diaryEntries: LiveData<List<DiaryEntry>> = dao.getAllEntriesLive()


    /**
     * A public function to insert a new diary entry into the database.
     * It launches a coroutine in the [viewModelScope], ensuring the database operation
     * happens on a background thread, not the main UI thread.
     *
     * @param entry The [DiaryEntry] to be inserted.
     */
    fun insert(entry: DiaryEntry) {
        viewModelScope.launch {
            dao.insert(entry)
        }
    }


    /**
     * A public function to delete a diary entry from the database.
     * It uses a coroutine in the [viewModelScope] for background execution.
     *
     * @param entry The [DiaryEntry] to be deleted.
     */
    fun delete(entry: DiaryEntry) {
        viewModelScope.launch {
            dao.delete(entry)
        }
    }


    /**
     * A public function to update an existing diary entry in the database.
     * It uses a coroutine in the [viewModelScope] for background execution.
     *
     * @param entry The [DiaryEntry] with updated data to be saved.
     */
    fun update(entry: DiaryEntry) = viewModelScope.launch {
        dao.update(entry)
    }
}