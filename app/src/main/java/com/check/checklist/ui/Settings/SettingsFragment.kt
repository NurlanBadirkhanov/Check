package com.check.checklist.ui.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.check.checklist.Helper.SettingsState
import com.check.checklist.Helper.TaskStore
import com.check.checklist.Helper.CheckStore
import com.check.checklist.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _b: FragmentSettingsBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentSettingsBinding.inflate(inflater, container, false)

        b.btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        // Текущая подпись сортировки
        b.tvSortValue.text = if (SettingsState.sortMode == SettingsState.SortMode.BY_DATE) "By Date" else "By Title"

        b.rowSort.setOnClickListener {
            val items = arrayOf("By Date", "By Title")
            val current = if (SettingsState.sortMode == SettingsState.SortMode.BY_DATE) 0 else 1
            AlertDialog.Builder(requireContext())
                .setTitle("Default Sorting")
                .setSingleChoiceItems(items, current) { d, which ->
                    SettingsState.sortMode =
                        if (which == 0) SettingsState.SortMode.BY_DATE else SettingsState.SortMode.BY_TITLE
                    b.tvSortValue.text = items[which]

                    TaskStore.notifyChanged()
                    d.dismiss()
                }
                .show()
        }

        b.btnReset.setOnClickListener {
            TaskStore.clear()
            try { CheckStore.clearAll() } catch (_: Throwable) {}
        }

        return b.root
    }

    override fun onDestroyView() {
        super.onDestroyView(); _b = null
    }
}
