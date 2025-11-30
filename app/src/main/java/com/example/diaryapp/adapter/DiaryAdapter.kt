package com.example.diaryapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diaryapp.R
import com.example.diaryapp.db.DiaryEntry

/**
 * An adapter for the RecyclerView that displays a list of [DiaryEntry] items.
 *
 * This adapter uses [ListAdapter] for efficient list updates and supports item selection
 * via the [SelectionTracker].
 *
 * @param onClick A lambda function to be invoked when a list item is clicked.
 * @param onEditClick A lambda function to be invoked when the edit button on an item is clicked.
 */
class DiaryAdapter(
    private val onClick: (DiaryEntry) -> Unit,
    private val onEditClick: (DiaryEntry) -> Unit
) : ListAdapter<DiaryEntry, DiaryAdapter.DiaryViewHolder>(DIFF_CALLBACK) {

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    /**
     * The ViewHolder that holds the view for a single diary entry item.
     * It is responsible for binding the data to the views and handling item-specific events.
     */
    inner class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateView: TextView = itemView.findViewById(R.id.itemDate)
        private val entryText: TextView = itemView.findViewById(R.id.entryText)
        private val editButton: ImageButton = itemView.findViewById(R.id.editButton)
        private val itemImage: ImageView = itemView.findViewById(R.id.itemImage)

        /**
         * Binds a [DiaryEntry] object to the views in the ViewHolder.
         * Also sets the item's appearance based on its selection state.
         *
         * @param entry The diary entry data to display.
         * @param isSelected True if the item is currently selected, false otherwise.
         */
        fun bind(entry: DiaryEntry, isSelected: Boolean) {
            dateView.text = entry.date
            entryText.text = entry.text

            itemView.isActivated = isSelected

            editButton.setOnClickListener {
                onEditClick(entry)
            }

            itemView.setOnClickListener {
                onClick(entry)
            }

            itemImage.visibility = View.GONE
        }



        /**
         * Provides the [SelectionTracker] with details about the item for selection purposes.
         */
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Long? = getItemId(bindingAdapterPosition)
            }
    }

    /**
     * Called by RecyclerView to create a new ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diary_item, parent, false)
        return DiaryViewHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at a specified position.
     * This method fetches the data, checks its selection state, and binds it to the ViewHolder.
     */
    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION && position < currentList.size) {
            val entry = getItem(position)
            val isSelected = tracker?.isSelected(getItemId(position)) ?: false
            holder.bind(entry, isSelected)
        }
    }

    override fun getItemId(position: Int): Long {
        if (position != RecyclerView.NO_POSITION && position < currentList.size) {
            return getItem(position).id.toLong()
        }
        return RecyclerView.NO_ID
    }
    /**
     * A companion object holding the DiffUtil.ItemCallback implementation.
     * This is used by ListAdapter to efficiently calculate differences between two lists.
     */
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DiaryEntry>() {
            /**
             * Checks if two items represent the same object (e.g., have the same ID).
             */
            override fun areItemsTheSame(oldItem: DiaryEntry, newItem: DiaryEntry): Boolean {
                return oldItem.id == newItem.id
            }

            /**
             * Checks if the contents of two items are the same.
             * This is only called if areItemsTheSame returns true.
             */
            override fun areContentsTheSame(oldItem: DiaryEntry, newItem: DiaryEntry): Boolean {
                return oldItem == newItem
            }
        }
    }
}
