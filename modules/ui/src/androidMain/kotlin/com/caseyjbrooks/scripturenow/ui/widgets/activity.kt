package com.caseyjbrooks.scripturenow.ui.widgets

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjector

@SuppressLint("ComposableNaming")
@Composable
public fun finishActivityOnRouterEmptied(activity: Activity, injector: RepositoriesInjector) {
    DisposableEffect(activity, injector) {
        injector.registerBackstackEmptiedCallback(activity) {
            activity.finish()
        }
        onDispose {
            injector.unregisterBackstackEmptiedCallback(activity)
        }
    }
}
