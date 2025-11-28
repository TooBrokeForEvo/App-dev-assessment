package com.example.myapplicationfragments.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_table")
data class DiaryEntry(
    // Properties are declared here in the primary constructor
    val date: String,
    val text: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // It's also good practice to make the auto-generated ID a 'var'
}
