package com.caseyjbrooks.app.utils.lifecycleplugins

import com.copperleaf.ballast.BallastLogger
import com.copperleaf.ballast.BallastNotification
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.scripturenow.repositories.router.RouterInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

class RouterScreenAnalyticsInterceptor(
    private val firebaseAnalytics: FirebaseAnalytics
) : RouterInterceptor {
    override suspend fun onNotify(
        logger: BallastLogger,
        notification: BallastNotification<RouterContract.Inputs, RouterContract.Events, RouterContract.State>
    ) {
        if (notification is BallastNotification.EventEmitted) {
            when (val ev = notification.event) {
                is RouterContract.Events.NewDestination -> {
                    ev.currentDestination?.originalRoute?.originalRoute?.let { route ->
                        firebaseAnalytics.logEvent("screen_view") {
                            param(FirebaseAnalytics.Param.SCREEN_NAME, route)
                            param(FirebaseAnalytics.Param.SCREEN_CLASS, route)
                        }
                    }
                }
                is RouterContract.Events.OnBackstackEmptied -> {

                }
            }
        }
    }
}
