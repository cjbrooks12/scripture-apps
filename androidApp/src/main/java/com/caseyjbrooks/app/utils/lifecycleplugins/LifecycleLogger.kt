package com.caseyjbrooks.app.utils.lifecycleplugins

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class LifecycleLogger : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        Log.d(owner::class.simpleName!!, "onCreate()")
    }

    override fun onStart(owner: LifecycleOwner) {
        Log.d(owner::class.simpleName!!, "onStart()")
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.d(owner::class.simpleName!!, "onResume()")
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d(owner::class.simpleName!!, "onPause()")
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.d(owner::class.simpleName!!, "onStop()")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(owner::class.simpleName!!, "onDestroy()")
    }
}
