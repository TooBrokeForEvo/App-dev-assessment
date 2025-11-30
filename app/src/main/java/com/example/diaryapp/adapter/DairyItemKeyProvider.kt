package com.example.diaryapp.adapter

import androidx.recyclerview.selection.ItemKeyProvider

/**
 * Provides the SelectionTracker with the key for a given item position.
 * The keys are the stable Long IDs from our DiaryEntry entities.
 */
class DiaryItemKeyProvider(private val adapter: DiaryAdapter) :
    ItemKeyProvider<Long>(SCOPE_CACHED) {

    override fun getKey(position: Int): Long? {
        return adapter.currentList.getOrNull(position)?.id?.toLong()
    }

    override fun getPosition(key: Long): Int {
        return adapter.currentList.indexOfFirst { it.id.toLong() == key }
    }
}
