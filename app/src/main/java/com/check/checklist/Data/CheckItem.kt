package com.check.checklist.Data

data class CheckItem(
    val parentId: Long,          // id списка (TaskItem.id)
    val text: String,
    val isDone: Boolean = false,
    val id: Long = System.currentTimeMillis()
)
