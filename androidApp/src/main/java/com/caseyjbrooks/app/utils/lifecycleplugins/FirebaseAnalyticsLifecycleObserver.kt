package com.caseyjbrooks.app.utils.lifecycleplugins

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.caseyjbrooks.app.utils.BaseComponent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import javax.inject.Inject

class FirebaseAnalyticsLifecycleObserver
@Inject
constructor(
    private val analytics: FirebaseAnalytics,
) : DefaultLifecycleObserver {

    // Due to technical limitations of Firebase, we get "Cannot log screen view event when the app
    // is in the background." logged on the initial "screen_view" unless we track it in onResume().
    // This results in "page views" for that first Fragment to be dropped, instead of tracking
    // properly as we'd expect. However, After the initial screen has loaded and the Activty is
    // resumes, any subsequent screens will track in onCreate just fine,
    //
    // To avoid checking any global state to see if the fragment can be tracked in onCreate or not,
    // we'll just track all screen_views in onResume, with the understanding that it may be slightly
    // higher than "true" page views, but not significantly so with the use of Compose (there are no
    // Dialogs or other things that would normally pause/resume a fragment, so it's pretty much only
    // when the user "backgrounds" the app and resumes it quickly, like viewing their "recent apps".
    override fun onResume(owner: LifecycleOwner) {
        check(owner is BaseComponent)

        if(owner is Fragment) {
            // only fragments are true "screen views"
            analytics.logEvent("screen_view") {
                param(FirebaseAnalytics.Param.SCREEN_NAME, owner.screenName)
                param(FirebaseAnalytics.Param.SCREEN_CLASS, owner::class.java.name)
            }
        } else {
            // activities are more "states" or "flows"
            analytics.logEvent("state") {
                param("flow_name", owner.screenName)
                param("flow_class", owner::class.java.name)
            }
        }
    }
}
