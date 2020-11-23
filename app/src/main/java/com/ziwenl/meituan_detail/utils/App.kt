package com.ziwenl.meituan_detail.utils

import android.app.Application
import android.content.Context

class App : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        private var INSTANCE: App? = null

        @Synchronized
        fun get(): App = INSTANCE!!
    }
}