package com.caseyjbrooks.app.utils

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.DefaultLifecycleObserver
import com.caseyjbrooks.app.MainApplication
import com.caseyjbrooks.app.utils.theme.BrandTheme
import com.caseyjbrooks.app.utils.theme.LocalInjector
import com.copperleaf.scripturenow.repositories.router.BackstackEmptiedCallback
import org.kodein.di.bind
import org.kodein.di.provider

abstract class ComposeActivity : AppCompatActivity() {

    protected open val plugins: List<DefaultLifecycleObserver> by lazy {
        listOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val applicationContext = this.applicationContext as MainApplication
        val activityInjector = applicationContext.injector + {
            bind<AppCompatActivity> {
                provider { this@ComposeActivity }
            }
            bind<BackstackEmptiedCallback>(overrides = true) {
                provider { BackstackEmptiedCallback { this@ComposeActivity.finish() } }
            }
        }

        setContent {
            CompositionLocalProvider(LocalInjector providesDefault activityInjector) {
                BrandTheme {
                    ScreenContent()
                }
            }
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

    @Composable
    abstract fun ScreenContent()
}
