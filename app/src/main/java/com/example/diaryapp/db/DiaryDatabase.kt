package com.example.diaryapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * The main database class for the application, built using Room.
 * This class is abstract and extends RoomDatabase.
 *
 * @Database annotation marks this class as a Room database.
 * 'entities' lists all the data classes that are tables in this database.
 * 'version' is the database version, which must be incremented on schema changes.
 */
@Database(entities = [DiaryEntry::class], version = 2)
abstract class DiaryDatabase : RoomDatabase() {

    /**
     * An abstract function that returns an instance of the DiaryDao.
     * Room will generate the implementation for this method.
     */
    abstract fun diaryDao(): DiaryDao

    /**
     * A companion object to hold the singleton instance of the database.
     * This ensures that only one instance of the database is ever created.
     */
    companion object {
        /**
         * The @Volatile annotation ensures that the INSTANCE variable is always up-to-date
         * and the same to all execution threads. It guarantees that changes made by one
         * thread to INSTANCE are visible to all other threads immediately.
         */
        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE diary_table ADD COLUMN imagePath TEXT")
            }
        }

        /**
         * A synchronized function to get the singleton instance of the database.
         * If an instance already exists, it returns it.
         * If not, it creates a new database instance inside a synchronized block
         * to prevent multiple threads from creating it at the same time.
         *
         * @param context The application context.
         * @return The singleton instance of [DiaryDatabase].
         */
        fun getDatabase(context: Context): DiaryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diary_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
