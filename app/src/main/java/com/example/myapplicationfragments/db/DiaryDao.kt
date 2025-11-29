package com.example.myapplicationfragments.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DiaryDao {
    @Insert
    suspend fun insert(entry: DiaryEntry)

    @Update
    suspend fun update(entry: DiaryEntry)

    @Delete
    suspend fun delete(entry: DiaryEntry)

    @Query("SELECT * FROM diary_table ORDER BY id DESC")
    fun getAllEntriesLive(): LiveData<List<DiaryEntry>>

    @Query("SELECT * FROM diary_table ORDER BY id DESC")
    suspend fun getAllEntries(): List<DiaryEntry>
}