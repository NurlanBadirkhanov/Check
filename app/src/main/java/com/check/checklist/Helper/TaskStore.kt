package com.check.checklist.Helper

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.check.checklist.Data.TaskItem
import org.json.JSONArray
import org.json.JSONObject

object TaskStore {
    private const val PREFS = "task_store"
    private const val KEY   = "items_v1"

    private lateinit var prefs: SharedPreferences

    private val _items = MutableLiveData<List<TaskItem>>(emptyList())
    val items: LiveData<List<TaskItem>> get() = _items

    fun notifyChanged() {
        _items.value = _items.value.orEmpty().toList()
    }


    fun init(context: Context) {
        if (this::prefs.isInitialized) return
        prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        _items.value = loadFromPrefs()
    }

    fun getSnapshot(): List<TaskItem> = _items.value.orEmpty()

    fun add(item: TaskItem) {
        val next = getSnapshot().toMutableList().apply { add(item) }
        setAndPersist(next)
    }

    fun removeById(id: Long): Boolean {
        val cur = getSnapshot()
        val next = cur.filterNot { it.id == id }
        val changed = next.size != cur.size
        if (changed) setAndPersist(next)
        return changed
    }

    fun update(item: TaskItem): Boolean {
        val cur = getSnapshot()
        val idx = cur.indexOfFirst { it.id == item.id }
        if (idx == -1) return false
        val mut = cur.toMutableList()
        mut[idx] = item
        setAndPersist(mut)
        return true
    }

    fun clear() = setAndPersist(emptyList())

    // --- private ---
    private fun setAndPersist(list: List<TaskItem>) {
        _items.value = list
        if (this::prefs.isInitialized) {
            prefs.edit().putString(KEY, toJson(list)).apply()
        }
    }

    private fun loadFromPrefs(): List<TaskItem> {
        val json = prefs.getString(KEY, null) ?: return emptyList()
        return fromJson(json)
    }

    private fun toJson(list: List<TaskItem>): String {
        val arr = JSONArray()
        list.forEach { it ->
            arr.put(JSONObject().apply {
                put("count", it.count)
                put("title", it.title)
                put("time", it.time)
                put("imageRes", it.imageRes)
                put("category", it.category)
                put("id", it.id)
            })
        }
        return arr.toString()
    }

    private fun fromJson(json: String): List<TaskItem> {
        val arr = JSONArray(json)
        val out = ArrayList<TaskItem>(arr.length())
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            out.add(
                TaskItem(
                    count = o.optInt("count"),
                    title = o.optString("title"),
                    time = o.optString("time"),
                    imageRes = o.optInt("imageRes"),
                    category = o.optString("category"),
                    id = o.optLong("id", System.currentTimeMillis() + i)
                )
            )
        }
        return out
    }
}
