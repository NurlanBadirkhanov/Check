package com.check.checklist

import com.check.checklist.Helper.TaskStore

class App : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        TaskStore.init(this)
    }
}
