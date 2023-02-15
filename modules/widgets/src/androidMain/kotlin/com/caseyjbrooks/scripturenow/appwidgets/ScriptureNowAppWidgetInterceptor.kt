package com.caseyjbrooks.scripturenow.appwidgets

import android.content.Context
import com.caseyjbrooks.scripturenow.appwidgets.ScriptureNowAppWidget.Companion.WIDGET_JSON
import com.copperleaf.ballast.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

public abstract class ScriptureNowAppWidgetInterceptor<Inputs : Any, Events : Any, State : Any, Property : Any>(
    private val applicationContext: Context,
    private val getWidget: () -> ScriptureNowAppWidget<Property>,
    private val selectProperty: (State) -> Property,
) : BallastInterceptor<Inputs, Events, State> {

    final override fun BallastInterceptorScope<Inputs, Events, State>.start(
        notifications: Flow<BallastNotification<Inputs, Events, State>>
    ) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            notifications.awaitViewModelStart()
            notifications
                .states { it }
                .map { selectProperty(it) }
                .onEach {
                    val widget = getWidget()
                    val widgetJson = Json.encodeToString(widget.serializer, it)
                    widget.updateAllAppWidgetStates(applicationContext) {
                        this[WIDGET_JSON] = widgetJson
                    }
                }
                .collect()
        }
    }
}
