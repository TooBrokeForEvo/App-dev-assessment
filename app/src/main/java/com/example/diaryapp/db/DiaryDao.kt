package com.example.diaryapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object (DAO) for the DiaryEntry entity.
 * This interface defines the database interactions for diary entries,
 * including inserting, updating, deleting, and querying.
 */
@Dao
interface DiaryDao {

    /**
     * Inserts a new diary entry into the database.
     * The 'suspend' keyword means this function can be paused and resumed,
     * making it non-blocking and safe to call from a coroutine.
     * @param entry The [DiaryEntry] to be inserted.
     */
    @Insert
    suspend fun insert(entry: DiaryEntry)

    /**
     * Updates an existing diary entry in the database.
     * The entry is identified by its primary key.
     * @param entry The [DiaryEntry] with updated values.
     */
    @Update
    suspend fun update(entry: DiaryEntry)

    /**
     * Deletes a diary entry from the database.
     * @param entry The [DiaryEntry] to be deleted.
     */
    @Delete
    suspend fun delete(entry: DiaryEntry)

    /**
     * Retrieves all diary entries from the table as [LiveData].
     * The results are ordered by ID in descending order (newest first).
     * LiveData will automatically update the UI when the data changes.
     * @return A LiveData list of all [DiaryEntry] objects.
     */
    @Query("SELECT * FROM diary_table ORDER BY id DESC")
    fun getAllEntriesLive(): LiveData<List<DiaryEntry>>

    /**
     * Retrieves all diary entries from the table as a simple [List].
     * This is a suspend function, intended for one-shot data retrieval within a coroutine.
     * The results are ordered by ID in descending order (newest first).
     * @return A list of all [DiaryEntry] objects.
     */
    @Query("SELECT * FROM diary_table ORDER BY id DESC")
    suspend fun getAllEntries(): List<DiaryEntry>
}
