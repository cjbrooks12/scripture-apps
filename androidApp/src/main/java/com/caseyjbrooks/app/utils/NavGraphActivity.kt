package com.caseyjbrooks.app.utils

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver

abstract class NavGraphActivity(
    @LayoutRes
    private val navGraphLayoutId: Int
) : AppCompatActivity() {

    protected open val plugins: List<DefaultLifecycleObserver> by lazy {
        listOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(navGraphLayoutId)
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
