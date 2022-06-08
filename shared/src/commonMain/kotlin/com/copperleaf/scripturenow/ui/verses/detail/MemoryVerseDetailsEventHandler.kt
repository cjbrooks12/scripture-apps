package com.copperleaf.scripturenow.ui.verses.detail

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

class MemoryVerseDetailsEventHandler : EventHandler<
    MemoryVerseDetailsContract.Inputs,
    MemoryVerseDetailsContract.Events,
    MemoryVerseDetailsContract.State> {
    override suspend fun EventHandlerScope<
        MemoryVerseDetailsContract.Inputs,
        MemoryVerseDetailsContract.Events,
        MemoryVerseDetailsContract.State>.handleEvent(
        event: MemoryVerseDetailsContract.Events
    ) = when (event) {
        is MemoryVerseDetailsContract.Events.NavigateUp -> {

        }
    }
}
