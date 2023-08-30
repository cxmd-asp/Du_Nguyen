package com.example.safarione

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (isDebugMode()) {
            Timber.plant(Timber.DebugTree())
        } else {
            //TODO add a crashlytics tree
        }
    }
}

fun Context.isDebugMode() = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0