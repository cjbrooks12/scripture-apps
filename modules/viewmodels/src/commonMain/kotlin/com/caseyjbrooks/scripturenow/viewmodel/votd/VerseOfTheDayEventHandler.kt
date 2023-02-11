package com.caseyjbrooks.scripturenow.viewmodel.votd

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

public class VerseOfTheDayEventHandler(
//    private val routerViewModel: MainRouterViewModel,
) : EventHandler<
        VerseOfTheDayContract.Inputs,
        VerseOfTheDayContract.Events,
        VerseOfTheDayContract.State> {
    override suspend fun EventHandlerScope<
            VerseOfTheDayContract.Inputs,
            VerseOfTheDayContract.Events,
            VerseOfTheDayContract.State>.handleEvent(
        event: VerseOfTheDayContract.Events
    ): Unit = when (event) {
        is VerseOfTheDayContract.Events.NavigateUp -> {
//            routerViewModel.send(RouterContract.Inputs.GoBack)
        }
    }
}
