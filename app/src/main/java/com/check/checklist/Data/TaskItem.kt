package com.check.checklist.Data

data class TaskItem(
    val count: Int,
    val title: String,
    val time: String,
    val imageRes: Int,
    val category: String,
    val id: Long = System.currentTimeMillis()
)