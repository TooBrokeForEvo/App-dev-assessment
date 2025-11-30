package com.example.diaryapp.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

/**
 * Provides the SelectionTracker with details about the item at a given MotionEvent (touch event).
 */
class DiaryItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val viewHolder = recyclerView.getChildViewHolder(view)
            if (viewHolder is DiaryAdapter.DiaryViewHolder) {
                return viewHolder.getItemDetails()
            }
        }
        return null
    }
}
