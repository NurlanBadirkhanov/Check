package com.check.checklist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.check.checklist.Adap.TaskAdapter
import com.check.checklist.R
import com.check.checklist.Data.TaskItem
import com.check.checklist.Helper.TaskStore
import com.check.checklist.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { TaskAdapter { item ->
        findNavController().navigate(
            R.id.checklistFragment,
            bundleOf(
                "listId" to item.id,
                "listTitle" to item.title
            )
        )
    } }

    private val itemImages = intArrayOf(
        R.drawable.dep,
        R.drawable.dep2,
        R.drawable.dep3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("add_item_result", this) { _, b ->
            val title = b.getString("title").orEmpty()
            val category = b.getString("category").orEmpty()
            if (title.isNotEmpty()) {
                val imageRes = itemImages.random()
                val item = TaskItem(
                    count = 0,
                    title = title,
                    time = "",
                    imageRes = imageRes,
                    category = category
                )
                TaskStore.add(item)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.recyclerTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTasks.adapter = adapter

        binding.addList.setOnClickListener {
            findNavController().navigate(R.id.newFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TaskStore.items.observe(viewLifecycleOwner) { list ->
            val sorted = when (com.check.checklist.Helper.SettingsState.sortMode) {
                com.check.checklist.Helper.SettingsState.SortMode.BY_DATE  -> list.sortedBy { it.id }           // id = время создания
                com.check.checklist.Helper.SettingsState.SortMode.BY_TITLE -> list.sortedBy { it.title.lowercase() }
            }
            adapter.submitList(sorted) {
                binding.emptyState.isVisible = sorted.isEmpty()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
