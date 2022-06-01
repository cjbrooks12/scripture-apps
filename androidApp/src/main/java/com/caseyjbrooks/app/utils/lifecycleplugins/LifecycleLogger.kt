package com.caseyjbrooks.app.utils.lifecycleplugins

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.caseyjbrooks.app.utils.BaseComponent

class LifecycleLogger : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        check(owner is BaseComponent)
//        owner.logger.d("onCreate()")
    }

    override fun onStart(owner: LifecycleOwner) {
        check(owner is BaseComponent)
//        owner.logger.d("onStart()")
    }

    override fun onResume(owner: LifecycleOwner) {
        check(owner is BaseComponent)
//        owner.logger.d("onResume()")
    }

    override fun onPause(owner: LifecycleOwner) {
        check(owner is BaseComponent)
//        owner.logger.d("onPause()")
    }

    override fun onStop(owner: LifecycleOwner) {
        check(owner is BaseComponent)
//        owner.logger.d("onStop()")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        check(owner is BaseComponent)
//        owner.logger.d("onDestroy()")
    }
}
