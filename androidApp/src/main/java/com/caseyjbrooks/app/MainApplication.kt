package com.caseyjbrooks.app

import android.app.Application
import com.caseyjbrooks.app.di.AppInjector
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjector
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjectorProvider
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class MainApplication : Application(), RepositoriesInjectorProvider {

    private val appCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    public val appInjector = AppInjector(this, appCoroutineScope)

    override fun getRepositoriesInjector(): RepositoriesInjector {
        return appInjector.repositoriesInjector
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        appCoroutineScope.cancel()
    }
}
