package com.check.checklist.Adap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.check.checklist.Data.CheckItem
import com.check.checklist.databinding.ItemCheckBinding

class CheckAdapter(
    private val onToggle: (CheckItem, Boolean) -> Unit,
    private val onRemove: (CheckItem) -> Unit = {}
) : ListAdapter<CheckItem, CheckAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<CheckItem>() {
        override fun areItemsTheSame(o: CheckItem, n: CheckItem) = o.id == n.id
        override fun areContentsTheSame(o: CheckItem, n: CheckItem) = o == n
    }

    inner class VH(val b: ItemCheckBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: CheckItem) {
            b.cb.isChecked = item.isDone
            b.title.text = item.text
            b.cb.setOnCheckedChangeListener(null)
            b.cb.isChecked = item.isDone
            b.cb.setOnCheckedChangeListener { _, checked -> onToggle(item, checked) }
            b.root.setOnLongClickListener { onRemove(item); true }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, vt: Int) =
        VH(ItemCheckBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
