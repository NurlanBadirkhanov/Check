package com.check.checklist.Helper  // или твой пакет

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.check.checklist.Data.CheckItem
import org.json.JSONArray
import org.json.JSONObject

object CheckStore {
    private const val PREFS = "check_store"
    private const val KEY   = "checks_v1"

    private lateinit var prefs: SharedPreferences
    private val _items = MutableLiveData<List<CheckItem>>(emptyList())
    val items: LiveData<List<CheckItem>> get() = _items

    fun init(context: Context) {
        if (this::prefs.isInitialized) return
        prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        _items.value = load()
    }
    fun clearAll() {
        setAndSave(emptyList())
    }


    fun getSnapshot(): List<CheckItem> = _items.value.orEmpty()

    fun add(parentId: Long, text: String) {
        val next = getSnapshot().toMutableList().apply { add(CheckItem(parentId, text)) }
        setAndSave(next)
    }

    fun toggleDone(id: Long, checked: Boolean) {
        val next = getSnapshot().map { if (it.id == id) it.copy(isDone = checked) else it }
        setAndSave(next)
    }

    fun remove(id: Long) = setAndSave(getSnapshot().filterNot { it.id == id })

    fun clearFor(parentId: Long) = setAndSave(getSnapshot().filterNot { it.parentId == parentId })

    // --- private ---
    private fun setAndSave(list: List<CheckItem>) {
        _items.value = list
        if (this::prefs.isInitialized) {
            prefs.edit().putString(KEY, toJson(list)).apply()
        }
    }

    private fun load(): List<CheckItem> {
        val json = prefs.getString(KEY, null) ?: return emptyList()
        return fromJson(json)
    }

    private fun toJson(list: List<CheckItem>): String {
        val arr = JSONArray()
        list.forEach { c ->
            arr.put(JSONObject().apply {
                put("parentId", c.parentId)
                put("text", c.text)
                put("isDone", c.isDone)
                put("id", c.id)
            })
        }
        return arr.toString()
    }

    private fun fromJson(json: String): List<CheckItem> {
        val arr = JSONArray(json)
        val out = ArrayList<CheckItem>(arr.length())
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            out.add(
                CheckItem(
                    parentId = o.optLong("parentId"),
                    text = o.optString("text"),
                    isDone = o.optBoolean("isDone"),
                    id = o.optLong("id", System.currentTimeMillis() + i)
                )
            )
        }
        return out
    }
}
