package com.example.myapplicationfragments.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class ItemLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
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
