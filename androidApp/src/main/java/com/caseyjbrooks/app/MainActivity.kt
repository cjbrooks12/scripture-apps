package com.caseyjbrooks.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.DefaultLifecycleObserver
import com.caseyjbrooks.app.di.ViewModelsInjectorImpl
import com.caseyjbrooks.scripturenow.ui.ApplicationRoot

class MainActivity : ComponentActivity() {

    private val plugins: List<DefaultLifecycleObserver> by lazy {
        listOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val deepLinkUrl: String? = intent?.data?.toString()?.removePrefix("scripture://now")
        intent?.data = null

        setContent {
            val mainApplication = LocalContext.current.applicationContext as MainApplication
            val mainInjector = remember(mainApplication) { mainApplication.appInjector }
            val router = remember(mainInjector) { mainInjector.repositoriesInjector.getScriptureNowRouter(deepLinkUrl) }
            ApplicationRoot(router, ViewModelsInjectorImpl(mainInjector))
        }
    }

    override fun onStart() {
        plugins.forEach { lifecycle.addObserver(it) }
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        plugins.forEach { lifecycle.removeObserver(it) }
    }
}
