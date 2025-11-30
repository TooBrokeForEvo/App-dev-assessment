package com.example.diaryapp.db

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.diaryapp.adapter.DiaryAdapter

/**
 * Provides the [SelectionTracker] with information about the item located at a
 * given [MotionEvent]. This class is essential for linking touch events on the
 * screen to the specific RecyclerView items that should be selected.
 *
 * @param recyclerView The RecyclerView instance that this lookup class is inspecting.
 */
class ItemLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

    /**
     * This method is called by the [SelectionTracker] to determine which item, if any,
     * is under the user's finger during a touch event.
     *
     * @param e The [MotionEvent] representing the user's touch.
     * @return The [ItemDetails] for the item at the event's coordinates, or null if no
     *         item is found. The key type is [Long] because we use the item's stable ID.
     */
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
