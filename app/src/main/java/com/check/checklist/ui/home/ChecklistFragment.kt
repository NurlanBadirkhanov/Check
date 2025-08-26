package com.check.checklist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.check.checklist.Adap.CheckAdapter
import com.check.checklist.Helper.CheckStore
import com.check.checklist.databinding.FragmentChecklistBinding

class ChecklistFragment : Fragment() {

    private var _binding: FragmentChecklistBinding? = null
    private val binding get() = _binding!!

    private var listId: Long = 0L
    private var listTitle: String = ""

    private val adapter by lazy {
        CheckAdapter(
            onToggle = { item, checked -> CheckStore.toggleDone(item.id, checked) },
            onRemove = { item -> CheckStore.remove(item.id) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listId = requireArguments().getLong("listId")
        listTitle = requireArguments().getString("listTitle").orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChecklistBinding.inflate(inflater, container, false)

        binding.title.text = listTitle
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        CheckStore.items.observe(viewLifecycleOwner) { all ->
            adapter.submitList(all.filter { it.parentId == listId })
        }

        binding.btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.fabAdd.setOnClickListener { showAddDialog() }

        return binding.root
    }

    private fun showAddDialog() {
        val et = EditText(requireContext())
        et.hint = "New item"
        AlertDialog.Builder(requireContext())
            .setTitle("Add item")
            .setView(et)
            .setPositiveButton("Add") { _, _ ->
                val text = et.text.toString().trim()
                if (text.isNotEmpty()) CheckStore.add(listId, text)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView(); _binding = null
    }
}
