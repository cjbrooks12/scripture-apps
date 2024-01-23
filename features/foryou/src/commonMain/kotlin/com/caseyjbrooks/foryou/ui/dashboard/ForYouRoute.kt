package com.caseyjbrooks.foryou.ui.dashboard

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import com.caseyjbrooks.di.GlobalScriptureNowKoinApplication
import com.caseyjbrooks.notifications.NotificationService
import com.caseyjbrooks.routing.ListPane
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

public object ForYouRoute : ScriptureNowScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/home")
    override val annotations: Set<RouteAnnotation> = setOf(ListPane)

    public object Directions

    @Composable
    override fun Content(destination: Destination.Match<ScriptureNowScreen>) {
        Text("For You")

        val notificationService = remember {
            GlobalScriptureNowKoinApplication.koinApplication.koin.get<NotificationService>()
        }
        val isPermissionGranted: Boolean? by produceState(null as Boolean?, notificationService) {
            value = notificationService.isPermissionGranted()
        }

        Button({
            GlobalScriptureNowKoinApplication.koinApplication.koin.get<NotificationService>()
                .showNotification("FYP", "FYP Clicked")
        }) {
            Text("Notify!")
        }

        if(isPermissionGranted != null && isPermissionGranted!!) {
            Text("Notifications are enabled")
        } else {
            Button({
                GlobalScriptureNowKoinApplication.koinApplication.koin.get<NotificationService>()
                    .showNotification("FYP", "FYP Clicked")
            }) {
                Text("Grant notificaton permissions")
            }
        }
    }
}
