package com.copperleaf.ballast.router

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

public class RouterInputHandler : InputHandler<
    RouterContract.Inputs,
    RouterContract.Events,
    RouterContract.State> {
    override suspend fun InputHandlerScope<
        RouterContract.Inputs,
        RouterContract.Events,
        RouterContract.State>.handleInput(
        input: RouterContract.Inputs
    ): Unit = when (input) {
        is RouterContract.Inputs.GoToDestination -> {
            val eventsToSend = mutableListOf<RouterContract.Events>()

            val toAppendToBackstack = if (input.tag != null) {
                eventsToSend += RouterContract.Events.TagPushed(input.tag)
                eventsToSend += RouterContract.Events.DestinationPushed(input.destination)

                listOf(input.tag, input.destination)
            } else {
                eventsToSend += RouterContract.Events.DestinationPushed(input.destination)

                listOf(input.destination)
            }

            updateState {
                it.copy(backstack = it.backstack + toAppendToBackstack)
            }

            eventsToSend.forEach { postEvent(it) }
        }
        is RouterContract.Inputs.GoBack -> {
            val currentState = getCurrentState()
            if (!canGoBackOneStep(currentState)) {
                // error, backstack was empty
                error("Backstack was empty, cannot go back")
            } else {
                val eventsToSend = mutableListOf<RouterContract.Events>()
                val updatedState = goBackOneStep(currentState, eventsToSend)

                updateState { updatedState }
                eventsToSend.forEach { postEvent(it) }
            }
        }
    }

    private fun canGoBackOneStep(
        currentState: RouterContract.State,
    ): Boolean {
        return currentState.backstack.isNotEmpty()
    }

    private fun goBackOneStep(
        currentState: RouterContract.State,
        eventsToSend: MutableList<RouterContract.Events>,
    ): RouterContract.State {
        val backstack = currentState.backstack.toMutableList()

        val lastDestination = backstack.removeLast() as Destination
        if (backstack.lastOrNull() is Tag) {
            // remove the tag, too
            val removedTag = backstack.removeLast() as Tag
            val previousTag = backstack.lastOrNull { it is Tag } as? Tag

            eventsToSend += RouterContract.Events.TagPopped(removedTag, previousTag)
        }

        val previousDestination = backstack.lastOrNull() as? Destination?
        if (previousDestination == null) {
            // after removing the last destination, and any optional tag before it, the backstack is now empty
            eventsToSend.add(0, RouterContract.Events.DestinationPopped(lastDestination, previousDestination))
            eventsToSend.add(RouterContract.Events.OnBackstackEmptied)
        } else {
            eventsToSend.add(0, RouterContract.Events.DestinationPopped(lastDestination, previousDestination))
        }

        return currentState.copy(backstack = backstack.toList())
    }
}
