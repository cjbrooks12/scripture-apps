package com.caseyjbrooks.app.utils

import androidx.lifecycle.DefaultLifecycleObserver

/**
 * A common set of properties and objects used by the [DefaultLifecycleObserver] plugins attached to Activies or
 * Fragments. Since most logic is not handled in the Activity/Fragment itself, but rather in the Ballast InputHandler,
 * this should not contain other "commonly used" properties used in normal application logic.
 */
interface BaseComponent {
    val componentType: String
    val screenName: String
}
