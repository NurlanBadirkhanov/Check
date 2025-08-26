package com.check.checklist.Helper

object SettingsState {
    enum class SortMode { BY_DATE, BY_TITLE }

    var darkMode: Boolean = true
    var sortMode: SortMode = SortMode.BY_DATE
    var notificationsEnabled: Boolean = false

    fun reset() {
        darkMode = true
        sortMode = SortMode.BY_DATE
        notificationsEnabled = false
    }
}
