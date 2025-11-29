package com.example.myapplicationfragments.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_table")
data class DiaryEntry(
    val date: String,
    var text: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
