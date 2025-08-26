package com.check.checklist.Adap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.check.checklist.Data.TaskItem
import com.check.checklist.databinding.ItemTaskBinding

class TaskAdapter(
    private val onClick: (TaskItem) -> Unit = {}
) : ListAdapter<TaskItem, TaskAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<TaskItem>() {
        override fun areItemsTheSame(old: TaskItem, new: TaskItem) =
            old.title == new.title && old.time == new.time
        override fun areContentsTheSame(old: TaskItem, new: TaskItem) = old == new
    }

    inner class VH(private val b: ItemTaskBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: TaskItem) {
            b.tvCount.text = "${item.count} tasks"
            b.tvTitle.text = item.title
            b.tvTime.text = item.time
            b.preview.setImageResource(item.imageRes)
            b.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))

    fun setData(items: List<TaskItem>) {
        submitList(items.toList())
    }

    fun addItem(item: TaskItem) {
        val newList = currentList.toMutableList()
        newList.add(item)
        submitList(newList)
    }
}
