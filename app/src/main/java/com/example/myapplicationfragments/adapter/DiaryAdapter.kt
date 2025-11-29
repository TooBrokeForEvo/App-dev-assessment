package com.example.myapplicationfragments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationfragments.R
import com.example.myapplicationfragments.db.DiaryEntry

class DiaryAdapter(
    private val onClick: (DiaryEntry) -> Unit
) : ListAdapter<DiaryEntry, DiaryAdapter.DiaryViewHolder>(DIFF_CALLBACK) {

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    inner class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val entryText: TextView = itemView.findViewById(R.id.entryText)

        fun bind(entry: DiaryEntry, isSelected: Boolean) {
            entryText.text = entry.text
            itemView.isActivated = isSelected
            itemView.setOnClickListener {
                onClick(entry)
            }
            itemView.isSelected = isSelected
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Long? = getItemId(bindingAdapterPosition)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diary_item, parent, false)
        return DiaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val entry = getItem(position)
        val isSelected = tracker?.isSelected(getItemId(position)) ?: false
        holder.bind(entry, isSelected)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DiaryEntry>() {
            override fun areItemsTheSame(oldItem: DiaryEntry, newItem: DiaryEntry): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DiaryEntry, newItem: DiaryEntry): Boolean {
                return oldItem == newItem
            }
        }
    }
}