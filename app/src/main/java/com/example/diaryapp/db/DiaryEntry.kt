package com.example.diaryapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single entry in the diary database.
 * This is a 'data class', which is Kotlin's concise way to create a class that just holds data.
 *
 * @Entity annotation marks this class as a database table.
 * 'tableName' defines the name of the table in the SQLite database.
 *
 * @property date The date the entry was created, stored as a String.
 * @property text The main content of the diary entry.
 */
@Entity(tableName = "diary_table")
data class DiaryEntry(
    var date: String,
    var text: String,
    var imagePath: String? = null
) {
    /**
     * The unique identifier for each diary entry.
     *
     * @PrimaryKey marks this property as the table's primary key.
     * 'autoGenerate = true' tells Room to automatically generate a unique ID for each new entry,
     * so we don't have to manage it manually.
     */
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
