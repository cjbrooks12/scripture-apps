package com.caseyjbrooks.app

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class MainAndroidApplication : Application() {

    private val appCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
//        FirebaseApp.initializeApp(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        appCoroutineScope.cancel()
    }
}
