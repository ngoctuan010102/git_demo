package com.ngoctuan.randomcolor.payment

import android.app.Application
import android.content.Context


class App : Application() {
    var cnt: Context? = null
    override fun onCreate() {
        super.onCreate()
        cnt = this
        QuizPref.init(this)
    }
}