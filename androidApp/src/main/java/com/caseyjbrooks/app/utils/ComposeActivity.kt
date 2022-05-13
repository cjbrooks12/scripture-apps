package com.caseyjbrooks.app.utils

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.DefaultLifecycleObserver
import com.caseyjbrooks.app.utils.theme.BrandTheme

abstract class ComposeActivity : AppCompatActivity() {

    protected open val plugins: List<DefaultLifecycleObserver> by lazy {
        listOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrandTheme {
                ScreenContent()
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
