package com.check.checklist.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.check.checklist.R
import com.check.checklist.databinding.FragmentNewBinding

class NewFragment : Fragment() {

    private var _binding: FragmentNewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewBinding.inflate(inflater, container, false)

        val categories = listOf("Grocery", "Work", "Personal")
        binding.actCategory.setAdapter(
            ArrayAdapter(requireContext(), R.layout.list_item_dropdown, categories)
        )

        binding.btnClose.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.btnSave.setOnClickListener {
            val title = binding.etName.text?.toString()?.trim() ?: ""
            val category = binding.actCategory.text?.toString()?.trim() ?: ""

            if (title.isEmpty()) {
                binding.tilName.isErrorEnabled = true
                binding.tilName.error = getString(R.string.required)
                binding.etName.requestFocus()
                return@setOnClickListener
            } else {
                binding.tilName.isErrorEnabled = false
                binding.tilName.error = null
            }

            val result = Bundle().apply {
                putString("title", title)
                putString("category", category)
            }
            parentFragmentManager.setFragmentResult("add_item_result", result)

            (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(binding.root.windowToken, 0)
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
