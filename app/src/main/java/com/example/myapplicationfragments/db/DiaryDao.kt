package com.example.myapplicationfragments.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DiaryDao {
    @Insert
    suspend fun insertEntry(entry: DiaryEntry)

    @Query("SELECT * FROM diary_table")
    suspend fun getAllEntries(): List<DiaryEntry>

    @Update
    suspend fun updateEntry(entry: DiaryEntry)

    @Delete
    suspend fun deleteEntry(entry: DiaryEntry)


}